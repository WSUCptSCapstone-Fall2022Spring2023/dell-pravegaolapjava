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
import com.google.common.collect.Maps;
import io.pravega.client.ClientConfig;
import io.pravega.client.EventStreamClientFactory;
import io.pravega.client.admin.ReaderGroupManager;
import io.pravega.client.admin.StreamManager;
import io.pravega.client.stream.*;
import io.pravega.client.stream.impl.ByteBufferSerializer;
import org.apache.druid.data.input.impl.ByteEntity;
import org.apache.druid.indexing.pravega.supervisor.PravegaSupervisorIOConfig;
import org.apache.druid.indexing.seekablestream.common.OrderedPartitionableRecord;
import org.apache.druid.indexing.seekablestream.common.OrderedSequenceNumber;
import org.apache.druid.indexing.seekablestream.common.RecordSupplier;
import org.apache.druid.indexing.seekablestream.common.StreamException;
import org.apache.druid.indexing.seekablestream.common.StreamPartition;
import org.apache.druid.indexing.seekablestream.extension.PravegaConfigOverrides;
import org.apache.druid.metadata.DynamicConfigProvider;
import org.apache.druid.metadata.PasswordProvider;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.Deserializer;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class PravegaEventSupplier implements RecordSupplier<String, StreamCut, ByteEntity>
{
  private EventStreamReader<ByteBuffer> consumer;
  private final String readerName = "readerOne";
  private ReaderGroup readerGroup;
  private StreamManager streamManager;
  private Stream stream;
  private boolean closed;
  private ClientConfig config;
  private String readerGroupName;
  private StreamCut streamCut;

  private final Map<StreamPartition<String>, Long> partitionTimes = new HashMap<>();

  public PravegaEventSupplier(
          Map<String, Object> consumerProperties,
          ObjectMapper sortingMapper,
          PravegaConfigOverrides configOverrides
  )
  {
    // NOTE: we may want to delay the instantiation of the consumer
    // we know that we need a readergroup to inst. the consumer, but we don't know where to start yet,
    // we find out where to start once seek() is called [seekablestreamsupervisor]
    // maybe we can have a dummy supervisor right here for now?
    // consumer = getPravegaReader(sortingMapper, consumerProperties, configOverrides);
  }

  @VisibleForTesting
  public PravegaEventSupplier(EventStreamReader<ByteBuffer> consumer)
  {
    this.consumer = consumer;
  }

  // assign a set of stream partitions to the consumer, contains the partition IDs
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
  public void seek(StreamPartition<String> partition, StreamCut sequenceNumber)
  {
    // Close reader at a pos. then set offline - invokes readerOffline() automatically, do we need to use readeroffline() instead of closeat?
    // "move away from closeAt(), reconfigure the reader group to start from a streamcut?  closeAt() resets readergroups to the begininning (of a stream?)
    // NOTE: we may need to recreate the reader group [this is after the streamCut option was decided]
    consumer.closeAt(Position.fromBytes(sequenceNumber.toBytes()));

    // Create the reader at the new position
    // NOTE: this needs change, we'd need to close the OLD readergroup above and then open a new readergroup with a new name
    consumer = EventStreamClientFactory.withScope(stream.getScope(), config)
            .createReader(readerName, readerGroupName, new ByteBufferSerializer(), ReaderConfig.builder().build());
  }

  // called from RecordSupplierInputSource.java
  // called from Seekablestreamsupervisor
  // utilize streammanager to fetch the tail, we want to seek to tail so we'd need to modify something
  @Override
  public void seekToEarliest(Set<StreamPartition<String>> partitions) {
    try {
      // NOTE: can we use streamcut.unbounded within startfromstreamcuts() to refer to the head without having to use a stream manager?
      streamCut = streamManager.fetchStreamInfo(stream.getScope(), stream.getStreamName()).get().getHeadStreamCut();

      readerGroup.resetReaderGroup(ReaderGroupConfig.builder()
              .startFromStreamCuts(Collections.singletonMap(stream, streamCut)) // create a map of Stream,StreamCut
              .build());
      // force all readers to this new pos. by resetting
    }
    catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }
  @Override
  public void seekToLatest(Set<StreamPartition<String>> partitions)
  {
    //concern: if we have 5 readers, is this called 5 times? or just once and applied to all readers
    try {
      streamCut = streamManager.fetchStreamInfo(stream.getScope(), stream.getStreamName()).get().getTailStreamCut();

      readerGroup.resetReaderGroup(ReaderGroupConfig.builder()
              .startFromStreamCuts(Collections.singletonMap(stream, streamCut)) // create a map of Stream,StreamCut
              .build());
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  // druid might use this function to recognize which partitions exist
  // called from Seekablestreamsupervisor - returning all partitions
  @Override
  public Set<StreamPartition<String>> getAssignment()
  {
    return Collections.singleton(new StreamPartition<>(stream.getStreamName(), readerName));
  }

  @Nonnull
  @Override
  public List<OrderedPartitionableRecord<String, StreamCut, ByteEntity>> poll(long timeout)
  {
    List<OrderedPartitionableRecord<String, StreamCut, ByteEntity>> polledEvents = new ArrayList<>();

    try {
      EventRead<ByteBuffer> event;
      boolean hasCheckpoint = false;

      // Initial loop to grab the first batch of events since the first batch can be null
      while ((event = consumer.readNextEvent(timeout)) == null)
      {
        // set up time stamp for this event, where is the timestamp?
        ;        // Log the timeout/communication failure
      }
      do {
        if (event.getEvent() != null) {
          if (hasCheckpoint) {
            streamCut = readerGroup.getStreamCuts().get(stream);
            hasCheckpoint = false;
          }
          polledEvents.add(new OrderedPartitionableRecord<>(
                  event.getEventPointer().getStream().getStreamName(),
                  readerGroupName,
                  streamCut,
                  ImmutableList.of(new ByteEntity((event.getEvent())))
          ));
        }
        else if (event.isCheckpoint()){
          hasCheckpoint = true;
        }

        // partition contains scopedStream name
        // constructor takes in stream, partitionID
        StreamPartition<String> streamPartition = new StreamPartition<>(stream.getStreamName(), event.getEventPointer().asImpl().getStream().getScope());

        // insert the streampartition and the time that it was polled
        partitionTimes.put(streamPartition, consumer.getCurrentTimeWindow(stream).getUpperTimeBound());

      } while ((event = consumer.readNextEvent(0)) != null) ;
    } catch (ReinitializationRequiredException e){
      //There are certain circumstances where the reader needs to be reinitialized, reinit the readergroup?
      //Not sure what to do yet, we dont want to ignore this - reinstantiate our consumer or reader?
      // Occurs when readergroup is reset
      e.printStackTrace();
      // init consumer() -> re create the reader to overwrite our consumer
    } catch (TruncatedDataException e) { //We'd want to skip to the next event for this exception
      e.printStackTrace();
    }
    return polledEvents;
  }

  @Override
  public StreamCut getLatestSequenceNumber(StreamPartition<String> partition)
  {
    StreamCut currPos = getPosition(partition);
    seekToLatest(Collections.singleton(partition));
    StreamCut nextPos = getPosition(partition);
    seek(partition, currPos);
    return nextPos;
  }

  @Override
  public StreamCut getEarliestSequenceNumber(StreamPartition<String> partition)
  {
    StreamCut currPos = getPosition(partition);
    seekToEarliest(Collections.singleton(partition));
    StreamCut nextPos = getPosition(partition);
    seek(partition, currPos);
    return nextPos;
  }

  @Override
  public boolean isOffsetAvailable(StreamPartition<String> partition, OrderedSequenceNumber<StreamCut> offset)
  {
    final StreamCut earliestOffset = getEarliestSequenceNumber(partition);
    return earliestOffset != null
            && offset.isAvailableWithEarliest(PravegaSequenceNumber.of(earliestOffset));
  }

  @Override
  public StreamCut getPosition(StreamPartition<String> partition)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public Set<String> getPartitionIds(String stream)
  {
    return readerGroup.getOnlineReaders();
  }

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
          PravegaConfigOverrides configOverrides
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
    props.putAll(consumerConfigs);

    ClassLoader currCtxCl = Thread.currentThread().getContextClassLoader();

    // start from earliest? -> check config to start with either a head or a tail
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

  public Map<String, Long> getPartitionsTimeLag(String stream, Map<String, StreamCut> currentOffsets) {
    Map<String, Long> partitionLag = Maps.newHashMapWithExpectedSize(currentOffsets.size());
    for (Map.Entry<String, StreamCut> partitionOffset : currentOffsets.entrySet()) {
      StreamPartition<String> partition = new StreamPartition<>(stream, partitionOffset.getKey());
      long currentLag = 0L;
      if (PravegaSequenceNumber.isValidPravegaSequence(partitionOffset.getValue())) {
        currentLag = getPartitionTimeLag(partition, partitionOffset.getValue());
      }
      partitionLag.put(partitionOffset.getKey(), currentLag);
    }
    return partitionLag;
  }

  private Long getPartitionTimeLag(StreamPartition<String> partition, StreamCut offset) {
    TimeWindow timeWindow = consumer.getCurrentTimeWindow(stream);
    Long latestTime = timeWindow.getUpperTimeBound();
    Long timeLag = 0L;
    try {
      timeLag = latestTime - partitionTimes.get(partition);
    }
    catch (Exception e) {
      return -1L;
    }
    return timeLag;
  }

  // added for test
  private static Deserializer getPravegaDeserializer(Properties properties, String pravegaConfigKey, boolean isKey)
  {
    Deserializer deserializerObject;
    try {
      Class deserializerClass = Class.forName(properties.getProperty(
              pravegaConfigKey,
              ByteArrayDeserializer.class.getTypeName()
      ));
      Method deserializerMethod = deserializerClass.getMethod("deserialize", String.class, byte[].class);

      Type deserializerReturnType = deserializerMethod.getGenericReturnType();

      if (deserializerReturnType == byte[].class) {
        deserializerObject = (Deserializer) deserializerClass.getConstructor().newInstance();
      } else {
        throw new IllegalArgumentException("Pravega deserializers must return a byte array (byte[]), " +
                deserializerClass.getName() + " returns " +
                deserializerReturnType.getTypeName());
      }
    }
    catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
           InvocationTargetException e) {
      throw new StreamException(e);
    }

    Map<String, Object> configs = new HashMap<>();
    for (String key : properties.stringPropertyNames()) {
      configs.put(key, properties.get(key));
    }

    deserializerObject.configure(configs, isKey);
    return deserializerObject;
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
