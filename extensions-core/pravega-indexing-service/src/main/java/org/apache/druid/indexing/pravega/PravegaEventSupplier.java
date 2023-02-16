/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.druid.indexing.pravega;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import io.pravega.client.ClientConfig;
import io.pravega.client.EventStreamClientFactory;
import io.pravega.client.admin.ReaderGroupManager;
import io.pravega.client.admin.StreamManager;
import io.pravega.client.stream.*;
import io.pravega.client.stream.impl.ByteBufferSerializer;
import org.apache.druid.common.utils.IdUtils;
import org.apache.druid.data.input.impl.ByteEntity;
import org.apache.druid.data.input.pravega.PravegaEventEntity;
import org.apache.druid.indexing.pravega.supervisor.PravegaSupervisorIOConfig;
import org.apache.druid.indexing.seekablestream.common.OrderedPartitionableRecord;
import org.apache.druid.indexing.seekablestream.common.OrderedSequenceNumber;
import org.apache.druid.indexing.seekablestream.common.RecordSupplier;
import org.apache.druid.indexing.seekablestream.common.StreamException;
import org.apache.druid.indexing.seekablestream.common.StreamPartition;
import org.apache.druid.indexing.seekablestream.extension.KafkaConfigOverrides;
import org.apache.druid.java.util.common.StringUtils;
import org.apache.druid.metadata.DynamicConfigProvider;
import org.apache.druid.metadata.PasswordProvider;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.Deserializer;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class PravegaEventSupplier implements RecordSupplier<String, ByteBuffer, ByteEntity>
{
  private EventStreamReader<ByteBuffer> consumer;
  private String readerName = "readerOne";
  private ReaderGroup readerGroup;
  private StreamManager streamManager;

  private Stream stream;

  private boolean closed;

  ClientConfig config;

  String readerGroupName;

  public PravegaEventSupplier(
      Map<String, Object> consumerProperties,
      ObjectMapper sortingMapper,
      KafkaConfigOverrides configOverrides
  )
  {
    consumer = getPravegaReader(sortingMapper, consumerProperties, configOverrides);
  }

  @VisibleForTesting
  public PravegaEventSupplier(EventStreamReader<ByteBuffer> consumer)
  {
    this.consumer = consumer;
  }

  // assign a set of stream partitions to the consumer, contains the partition IDs
  // we'd get all the segments from a reader group
  // also called from seekablestreamindextaskrunner.java and seeakablestreamsupervisor -> assignRecordSupplierToPartitionIDs()
    // supervisor can grab latest and earliest offsets to be stored in druid metadata (as well as partitions)
  @Override
  public void assign(Set<StreamPartition<String>> streamPartitions)
  {
    // Assign our one partitionID
    // We think this is a no op for us for now, since when we create pravega reader we assign our partition already
    // Once we have multiple readers we might have to come back
  }

  // called from seekablestreamindextaskrunner.java
  // partition contains scopedStream name and sequence num is the pos.
  @Override
  public void seek(StreamPartition<String> partition, ByteBuffer sequenceNumber)
  {
    // Close reader at a pos. then set offline - invokes readerOffline() automatically
    consumer.closeAt(Position.fromBytes(sequenceNumber));

    // Create the reader
    consumer = EventStreamClientFactory.withScope(stream.getScope(), config)
            .createReader(readerName, readerGroupName, new ByteBufferSerializer(), ReaderConfig.builder().build());
  }

  // called from RecordSupplierInputSource.java
  // called from Seekablestreamsupervisor
  // utilize streammanager to fetch the tail, we want to seek to tail so we'd need to modify something
  @Override
  public void seekToEarliest(Set<StreamPartition<String>> partitions)
  {

    StreamCut head = streamManager.getStreamInfo(stream.getScope(), stream.getStreamName()).getHeadStreamCut();

    readerGroup.resetReaderGroup(ReaderGroupConfig.builder()
            .startFromStreamCuts(Collections.singletonMap(stream, head)) // create a map of Stream,StreamCut
            .build());
    // force all readers to this new pos.
  }

  // called from RecordSupplierInputSource.java
  @Override
  public void seekToLatest(Set<StreamPartition<String>> partitions)
  {
    //concern: if we have 5 readers, is this called 5 times? or just once and applied to all readers
    StreamCut tail = streamManager.getStreamInfo(stream.getScope(), stream.getStreamName()).getTailStreamCut();

    readerGroup.resetReaderGroup(ReaderGroupConfig.builder()
            .startFromStreamCuts(Collections.singletonMap(stream, tail)) // create a map of Stream,StreamCut
            .build());
  }

  // druid might use this function to recognize which partitions exist?
  // for us: how do we get all segment names for a reader group OR a single stream(can be multiple)
    // for a reader group we want to grab stream(s), then associated segments, grab events, get partition IDs
  // called from Seekablestreamsupervisor - returning all partitions
  @Override
  public Set<StreamPartition<String>> getAssignment()
  {
    // Stream name likely comes from the spec
    return Collections.singleton(new StreamPartition<>(stream.getStreamName(), readerName));
  }

  // called from recordsupplierinputsource -> return value is turned into an iterator
  @Nonnull
  @Override
  public List<OrderedPartitionableRecord<String, ByteBuffer, ByteEntity>> poll(long timeout)
  {
    // do we need a start() or init() to initialize our EventReader to be connected to pravega?
    List<OrderedPartitionableRecord<String, ByteBuffer, ByteEntity>> polledEvents = new ArrayList<>();

    try {
      EventRead<ByteBuffer> event;

      // Initial loop to grab the first batch of events since the first batch can be null
      while ((event = consumer.readNextEvent(timeout)) == null)
      {
        // Log the timeout/communication failure
        ;
      }
      do {

        if (event.getEvent() != null) {
          // Calling the constructor
          polledEvents.add(new OrderedPartitionableRecord<>(
                  event.getEventPointer().getStream().getStreamName(),   //stream is our topic name
                  readerName,                                           // partition id [use reader group IDs instead] need a class var for reader group -> sequence offset,storing position
                  event.getPosition().toBytes(),                         // getting all offsets and partition IDs for the current reader, there may be a length
                  ImmutableList.of(new ByteEntity((event.getEvent())))
          ));
        }
        else if (event.isCheckpoint()){
          // empty body, allow next event to be read to pass the checkpoint
          // coord. checkpoints with druid maybe
          // initial plan is to let druid record its checkpoints so we might not do anything here manually
        }
      } while ((event = consumer.readNextEvent(0)) != null) ;
    } catch (ReinitializationRequiredException e){
      //There are certain circumstances where the reader needs to be reinitialized, reinit the readergroup?
      //Not sure what to do yet, we dont want to ignore this - reinstantiate our consumer or reader?
      e.printStackTrace();
      // init consumer() -> re create the reader to overwrite our consumer
    } catch (TruncatedDataException e) { //We'd want to skip to the next event for this exception
      e.printStackTrace();
    }
    return polledEvents;
  }


  // could be equivalent to ReaderGroup.Config -> get ending stream cuts. patitionID should be string
  // called from Seekablestreamsupervisor
  // our position refers to all the offsets that a reader has, ByteBuffer for us? Might not be implementable for us
  @Override
  public ByteBuffer getLatestSequenceNumber(StreamPartition<String> partition)
  {
    ByteBuffer currPos = getPosition(partition);
    seekToLatest(Collections.singleton(partition));
    ByteBuffer nextPos = getPosition(partition);
    seek(partition, currPos);
    return nextPos;
  }

  // could be equivalent to ReaderGroup.Config -> get starting stream cuts
  // called from Seekablestreamsupervisor
  @Override
  public ByteBuffer getEarliestSequenceNumber(StreamPartition<String> partition)
  {
    ByteBuffer currPos = getPosition(partition);
    seekToEarliest(Collections.singleton(partition));
    ByteBuffer nextPos = getPosition(partition);
    seek(partition, currPos);
    return nextPos;
  }

  @Override
  public boolean isOffsetAvailable(StreamPartition<String> partition, OrderedSequenceNumber<ByteBuffer> offset)
  {
    final ByteBuffer earliestOffset = getEarliestSequenceNumber(partition);
    return earliestOffset != null
           && offset.isAvailableWithEarliest(PravegaSequenceNumber.of(earliestOffset));
  }

  // not called anywhere else except in the kafka/pravega indexing service
  @Override
  public ByteBuffer getPosition(StreamPartition<String> partition)
  {
    throw new UnsupportedOperationException();
  }

  // returns set of unique stream partition ids
  // "how do we get all segment names from a single stream"
  // "druid calls getPartitionIDs" to create stream and partitionId key pairs to assign them to the record supplier
  // druid does this in prep. for multi threads
  // called from Seekablestreamsupervisor
  @Override
  public Set<String> getPartitionIds(String stream)
  {
    return readerGroup.getOnlineReaders();
  }

  // recordsupplierinputsource and seekablestreamsupervisor call close()
  @Override
  public void close()
  {
    if (closed) {
      return;
    }
    closed = true;
    consumer.close();
  }

  public static void addConsumerPropertiesFromConfig(
      Properties properties,
      ObjectMapper configMapper,
      Map<String, Object> consumerProperties
  )
  {
    // Extract passwords before SSL connection to Kafka
    for (Map.Entry<String, Object> entry : consumerProperties.entrySet()) {
      String propertyKey = entry.getKey();

      if (!PravegaSupervisorIOConfig.DRUID_DYNAMIC_CONFIG_PROVIDER_KEY.equals(propertyKey)) {
        if (propertyKey.equals(PravegaSupervisorIOConfig.TRUST_STORE_PASSWORD_KEY)
            || propertyKey.equals(PravegaSupervisorIOConfig.KEY_STORE_PASSWORD_KEY)
            || propertyKey.equals(PravegaSupervisorIOConfig.KEY_PASSWORD_KEY)) {
          PasswordProvider configPasswordProvider = configMapper.convertValue(
              entry.getValue(),
              PasswordProvider.class
          );
          properties.setProperty(propertyKey, configPasswordProvider.getPassword());
        } else {
          properties.setProperty(propertyKey, String.valueOf(entry.getValue()));
        }
      }
    }

    // Additional DynamicConfigProvider based extensible support for all consumer properties
    Object dynamicConfigProviderJson = consumerProperties.get(PravegaSupervisorIOConfig.DRUID_DYNAMIC_CONFIG_PROVIDER_KEY);
    if (dynamicConfigProviderJson != null) {
      DynamicConfigProvider dynamicConfigProvider = configMapper.convertValue(dynamicConfigProviderJson, DynamicConfigProvider.class);
      Map<String, String> dynamicConfig = dynamicConfigProvider.getConfig();
      for (Map.Entry<String, String> e : dynamicConfig.entrySet()) {
        properties.setProperty(e.getKey(), e.getValue());
      }
    }
  }

  public EventStreamReader<ByteBuffer> getPravegaReader(
      ObjectMapper sortingMapper,
      Map<String, Object> consumerProperties,
      KafkaConfigOverrides configOverrides
  )
  {
    final Map<String, Object> consumerConfigs = PravegaConsumerConfigs.getConsumerProperties();
    final Properties props = new Properties();
    Map<String, Object> effectiveConsumerProperties;
    if (configOverrides != null) {
      effectiveConsumerProperties = configOverrides.overrideConfigs(consumerProperties);
    } else {
      effectiveConsumerProperties = consumerProperties;
    }
    addConsumerPropertiesFromConfig(
        props,
        sortingMapper,
        effectiveConsumerProperties
    );
    props.putIfAbsent("isolation.level", "read_committed");
    props.putIfAbsent("group.id", StringUtils.format("kafka-supervisor-%s", IdUtils.getRandomId()));
    props.putAll(consumerConfigs);

    ClassLoader currCtxCl = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader(PravegaEventSupplier.class.getClassLoader());

      // Utilize props to fetch members for initializeation
      URI controllerURI = URI.create(props.getProperty("controllerURI"));
      String scopedStreamName = props.getProperty("scopedStreamName");
      readerGroupName = props.getProperty("readerGroupName");

      config = ClientConfig.builder().controllerURI(controllerURI).build();

      stream = Stream.of(scopedStreamName);

      ReaderGroupConfig readerGroupConfig = ReaderGroupConfig.builder().stream(stream).build();
      ReaderGroupManager readerGroupManager = ReaderGroupManager.withScope(stream.getScope(), config);

      streamManager = StreamManager.create(config);

      readerGroupManager.createReaderGroup(readerGroupName, readerGroupConfig); // returns a bool
      readerGroup = readerGroupManager.getReaderGroup(readerGroupName);

      return EventStreamClientFactory.withScope(stream.getScope(), config)
              .createReader(readerName, readerGroupName, new ByteBufferSerializer(), ReaderConfig.builder().build());
    }
    finally {
      Thread.currentThread().setContextClassLoader(currCtxCl);
    }
  }

  private static <T> T wrapExceptions(Callable<T> callable)
  {
    try {
      return callable.call();
    }
    catch (Exception e) {
      throw new StreamException(e);
    }
  }

  private static void wrapExceptions(Runnable runnable)
  {
    wrapExceptions(() -> {
      runnable.run();
      return null;
    });
  }
}
