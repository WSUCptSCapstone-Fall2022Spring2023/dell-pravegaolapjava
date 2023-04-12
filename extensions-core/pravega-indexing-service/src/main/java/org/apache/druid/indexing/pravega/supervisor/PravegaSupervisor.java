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

package org.apache.druid.indexing.pravega.supervisor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import io.pravega.client.stream.StreamCut;
import org.apache.druid.common.utils.IdUtils;
import org.apache.druid.data.input.impl.ByteEntity;
import org.apache.druid.indexing.common.task.Task;
import org.apache.druid.indexing.common.task.TaskResource;
import org.apache.druid.indexing.pravega.PravegaDataSourceMetadata;
import org.apache.druid.indexing.pravega.PravegaIndexTask;
import org.apache.druid.indexing.pravega.PravegaIndexTaskClientFactory;
import org.apache.druid.indexing.pravega.PravegaIndexTaskIOConfig;
import org.apache.druid.indexing.pravega.PravegaIndexTaskTuningConfig;
import org.apache.druid.indexing.pravega.PravegaEventSupplier;
import org.apache.druid.indexing.pravega.PravegaSequenceNumber;
import org.apache.druid.indexing.overlord.DataSourceMetadata;
import org.apache.druid.indexing.overlord.IndexerMetadataStorageCoordinator;
import org.apache.druid.indexing.overlord.TaskMaster;
import org.apache.druid.indexing.overlord.TaskStorage;
import org.apache.druid.indexing.overlord.supervisor.autoscaler.LagStats;
import org.apache.druid.indexing.seekablestream.SeekableStreamEndSequenceNumbers;
import org.apache.druid.indexing.seekablestream.SeekableStreamIndexTask;
import org.apache.druid.indexing.seekablestream.SeekableStreamIndexTaskIOConfig;
import org.apache.druid.indexing.seekablestream.SeekableStreamIndexTaskTuningConfig;
import org.apache.druid.indexing.seekablestream.SeekableStreamStartSequenceNumbers;
import org.apache.druid.indexing.seekablestream.common.OrderedSequenceNumber;
import org.apache.druid.indexing.seekablestream.common.RecordSupplier;
import org.apache.druid.indexing.seekablestream.common.StreamException;
import org.apache.druid.indexing.seekablestream.common.StreamPartition;
import org.apache.druid.indexing.seekablestream.supervisor.SeekableStreamSupervisor;
import org.apache.druid.indexing.seekablestream.supervisor.SeekableStreamSupervisorIOConfig;
import org.apache.druid.indexing.seekablestream.supervisor.SeekableStreamSupervisorReportPayload;
import org.apache.druid.java.util.common.StringUtils;
import org.apache.druid.java.util.emitter.EmittingLogger;
import org.apache.druid.java.util.emitter.service.ServiceEmitter;
import org.apache.druid.segment.incremental.RowIngestionMetersFactory;
import org.apache.druid.server.metrics.DruidMonitorSchedulerConfig;
import org.joda.time.DateTime;

import javax.annotation.Nullable;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Supervisor responsible for managing the PravegaIndexTasks for a single dataSource. At a high level, the class accepts a
 * {@link PravegaSupervisorSpec} which includes the Pravega topic and configuration as well as an ingestion spec which will
 * be used to generate the indexing tasks. The run loop periodically refreshes its view of the Pravega topic's partitions
 * and the list of running indexing tasks and ensures that all partitions are being read from and that there are enough
 * tasks to satisfy the desired number of replicas. As tasks complete, new tasks are queued to process the next range of
 * Pravega offsets.
 *
 * Through ReaderGroup abstraction we are presenting a single virtual partition. There can only be one index task for
 * the one reader within the ReaderGroup.
 */
public class PravegaSupervisor extends SeekableStreamSupervisor<String, StreamCut, ByteEntity>
{
  public static final TypeReference<TreeMap<Integer, Map<String, StreamCut>>> CHECKPOINTS_TYPE_REF =
          new TypeReference<TreeMap<Integer, Map<String, StreamCut>>>()
          {
          };

  private static final EmittingLogger log = new EmittingLogger(PravegaSupervisor.class);
  private static final StreamCut NOT_SET = StreamCut.UNBOUNDED; // mark this and come back to it, what does kafka do
  // with the -1 it sets for NOT_SET?
  // is not_set ever passed into a seek()?
  private static final StreamCut END_OF_PARTITION = StreamCut.UNBOUNDED;

  private final ServiceEmitter emitter;
  private final DruidMonitorSchedulerConfig monitorSchedulerConfig;
  private volatile Map<String, StreamCut> latestSequenceFromStream;

  private final PravegaSupervisorSpec spec;

  public PravegaSupervisor(
          final TaskStorage taskStorage,
          final TaskMaster taskMaster,
          final IndexerMetadataStorageCoordinator indexerMetadataStorageCoordinator,
          final PravegaIndexTaskClientFactory taskClientFactory,
          final ObjectMapper mapper,
          final PravegaSupervisorSpec spec,
          final RowIngestionMetersFactory rowIngestionMetersFactory
  )
  {
    super(
            StringUtils.format("PravegaSupervisor-%s", spec.getDataSchema().getDataSource()),
            taskStorage,
            taskMaster,
            indexerMetadataStorageCoordinator,
            taskClientFactory,
            mapper,
            spec,
            rowIngestionMetersFactory,
            false
    );

    this.spec = spec;
    this.emitter = spec.getEmitter();
    this.monitorSchedulerConfig = spec.getMonitorSchedulerConfig();
  }


  @Override
  protected RecordSupplier<String, StreamCut, ByteEntity> setupRecordSupplier()
  {
    return new PravegaEventSupplier(
            spec.getIoConfig().getConsumerProperties(),
            sortingMapper,
            spec.getIoConfig().getConfigOverrides()
    );
  }

  @Override
  protected int getTaskGroupIdForPartition(String partitionId)
  {
    return getTaskGroupIdForPartitionWithProvidedList(partitionId, partitionIds);
  }

  private int getTaskGroupIdForPartitionWithProvidedList(String partitionId, List<String> availablePartitions)
  {
    int index = availablePartitions.indexOf(partitionId);
    if (index < 0) {
      return index;
    }
    return availablePartitions.indexOf(partitionId) % spec.getIoConfig().getTaskCount();
  }

  @Override
  protected boolean checkSourceMetadataMatch(DataSourceMetadata metadata)
  {
    return metadata instanceof PravegaDataSourceMetadata;
  }

  @Override
  protected boolean doesTaskTypeMatchSupervisor(Task task)
  {
    return task instanceof PravegaIndexTask;
  }

  @Override
  protected SeekableStreamSupervisorReportPayload<String, StreamCut> createReportPayload(
          int numPartitions,
          boolean includeOffsets
  )
  {
    PravegaSupervisorIOConfig ioConfig = spec.getIoConfig();
    Map<String, Long> partitionLag = getRecordLagPerPartitionInLatestSequences(getHighestCurrentOffsets());
    return new PravegaSupervisorReportPayload(
            spec.getDataSchema().getDataSource(),
            ioConfig.getTopic(),
            numPartitions,
            ioConfig.getReplicas(),
            ioConfig.getTaskDuration().getMillis() / 1000,
            includeOffsets ? latestSequenceFromStream : null,
            includeOffsets ? partitionLag : null,
            includeOffsets ? partitionLag.values().stream().mapToLong(x -> Math.max(x, 0)).sum() : null,
            includeOffsets ? sequenceLastUpdated : null,
            spec.isSuspended(),
            stateManager.isHealthy(),
            stateManager.getSupervisorState().getBasicState(),
            stateManager.getSupervisorState(),
            stateManager.getExceptionEvents()
    );
  }


  @Override
  protected SeekableStreamIndexTaskIOConfig createTaskIoConfig(
          int groupId,
          Map<String, StreamCut> startPartitions,
          Map<String, StreamCut> endPartitions,
          String baseSequenceName,
          DateTime minimumMessageTime,
          DateTime maximumMessageTime,
          Set<String> exclusiveStartSequenceNumberPartitions,
          SeekableStreamSupervisorIOConfig ioConfig
  )
  {
    PravegaSupervisorIOConfig pravegaIoConfig = (PravegaSupervisorIOConfig) ioConfig;
    return new PravegaIndexTaskIOConfig(
            groupId,
            baseSequenceName,
            new SeekableStreamStartSequenceNumbers<>(pravegaIoConfig.getTopic(), startPartitions, Collections.emptySet()),
            new SeekableStreamEndSequenceNumbers<>(pravegaIoConfig.getTopic(), endPartitions),
            pravegaIoConfig.getConsumerProperties(),
            pravegaIoConfig.getPollTimeout(),
            true,
            minimumMessageTime,
            maximumMessageTime,
            ioConfig.getInputFormat(),
            pravegaIoConfig.getConfigOverrides()
    );
  }

  @Override
  protected List<SeekableStreamIndexTask<String, StreamCut, ByteEntity>> createIndexTasks(
          int replicas,
          String baseSequenceName,
          ObjectMapper sortingMapper,
          TreeMap<Integer, Map<String, StreamCut>> sequenceOffsets,
          SeekableStreamIndexTaskIOConfig taskIoConfig,
          SeekableStreamIndexTaskTuningConfig taskTuningConfig,
          RowIngestionMetersFactory rowIngestionMetersFactory
  ) throws JsonProcessingException
  {
    final String checkpoints = sortingMapper.writerFor(CHECKPOINTS_TYPE_REF).writeValueAsString(sequenceOffsets);
    final Map<String, Object> context = createBaseTaskContexts();
    context.put(CHECKPOINTS_CTX_KEY, checkpoints);
    // Kafka index task always uses incremental handoff since 0.16.0.
    // The below is for the compatibility when you want to downgrade your cluster to something earlier than 0.16.0.
    // Kafka index task will pick up LegacyKafkaIndexTaskRunner without the below configuration.
    context.put("IS_INCREMENTAL_HANDOFF_SUPPORTED", true);

    List<SeekableStreamIndexTask<String, StreamCut, ByteEntity>> taskList = new ArrayList<>();
    for (int i = 0; i < replicas; i++) {
      String taskId = IdUtils.getRandomIdWithPrefix(baseSequenceName);
      taskList.add(new PravegaIndexTask(
              taskId,
              new TaskResource(baseSequenceName, 1),
              spec.getDataSchema(),
              (PravegaIndexTaskTuningConfig) taskTuningConfig,
              (PravegaIndexTaskIOConfig) taskIoConfig,
              context,
              sortingMapper
      ));
    }
    return taskList;
  }

  @Override
  protected Map<String, Long> getPartitionRecordLag()
  {
    Map<String, StreamCut> highestCurrentOffsets = getHighestCurrentOffsets();

    if (latestSequenceFromStream == null) {
      return null;
    }

    if (!latestSequenceFromStream.keySet().equals(highestCurrentOffsets.keySet())) {
      log.warn(
              "Lag metric: Pravega partitions %s do not match task partitions %s",
              latestSequenceFromStream.keySet(),
              highestCurrentOffsets.keySet()
      );
    }

    return getRecordLagPerPartitionInLatestSequences(highestCurrentOffsets);
  }

  @Nullable
  @Override
  protected Map<String, Long> getPartitionTimeLag()
  {
    // time lag not currently support with kafka
    return null;
  }

  // suppress use of CollectionUtils.mapValues() since the valueMapper function is dependent on map key here
  @SuppressWarnings("SSBasedInspection")
  // Used while calculating cummulative lag for entire stream
  private Map<String, Long> getRecordLagPerPartitionInLatestSequences(Map<String, StreamCut>currentOffsets)
  {
    if (latestSequenceFromStream == null) {
      return Collections.emptyMap();
    }

    return latestSequenceFromStream
            .entrySet()
            .stream()
            .collect(
                    Collectors.toMap(
                            Entry::getKey,
                            e -> e.getValue() != null
                                    ? e.getValue() - Optional.ofNullable(currentOffsets.get(e.getKey())).orElse(0L)
                                    : 0
                    )
            );
  }

  // Record lag per partition is the gap between the current consumer's position and the latest event in a stream
  @Override
  // suppress use of CollectionUtils.mapValues() since the valueMapper function is dependent on map key here
  @SuppressWarnings("SSBasedInspection")
  // Used while generating Supervisor lag reports per task
  protected Map<String, Long> getRecordLagPerPartition(Map<String, StreamCut> currentOffsets)
  {
    if (latestSequenceFromStream == null || currentOffsets == null) {
      return Collections.emptyMap();
    }

    return currentOffsets
            .entrySet()
            .stream()
            .filter(e -> latestSequenceFromStream.get(e.getKey()) != null)
            .collect(
                    Collectors.toMap(
                            Entry::getKey,
                            e -> e.getValue() != null
                                    ? latestSequenceFromStream.get(e.getKey()) - e.getValue()
                                    : 0
                    )
            );
  }

  @Override
  protected Map<String, Long> getTimeLagPerPartition(Map<String, StreamCut> currentOffsets)
  {
    return null;
  }

  @Override
  protected PravegaDataSourceMetadata createDataSourceMetaDataForReset(String topic, Map<String, StreamCut> map)
  {
    return new PravegaDataSourceMetadata(new SeekableStreamEndSequenceNumbers<>(topic, map));
  }

  @Override
  protected OrderedSequenceNumber<StreamCut> makeSequenceNumber(StreamCut seq, boolean isExclusive)
  {
    return PravegaSequenceNumber.of(seq);
  }

  @Override
  protected StreamCut getNotSetMarker()
  {
    return NOT_SET;
  }

  @Override
  protected StreamCut getEndOfPartitionMarker() { return END_OF_PARTITION; }

  @Override
  protected boolean isEndOfShard(StreamCut seqNum)
  {
    return false;
  }

  @Override
  protected boolean isShardExpirationMarker(StreamCut seqNum)
  {
    return false;
  }

  @Override
  protected boolean useExclusiveStartSequenceNumberForNonFirstSequence()
  {
    return false;
  }

  @Override
  public LagStats computeLagStats()
  {
    Map<String, Long> partitionRecordLag = getPartitionRecordLag();
    if (partitionRecordLag == null) {
      return new LagStats(0, 0, 0);
    }

    return computeLags(partitionRecordLag);
  }

  @Override
  protected void updatePartitionLagFromStream()
  {
    getRecordSupplierLock().lock();
    try {
      Set<String> partitionIds;
      try {
        partitionIds = recordSupplier.getPartitionIds(getIoConfig().getStream());
      }
      catch (Exception e) {
        log.warn("Could not fetch partitions for topic/stream [%s]", getIoConfig().getStream());
        throw new StreamException(e);
      }

      Set<StreamPartition<String>> partitions = partitionIds
              .stream()
              .map(e -> new StreamPartition<>(getIoConfig().getStream(), e))
              .collect(Collectors.toSet());

      recordSupplier.seekToLatest(partitions);

      // this method isn't actually computing the lag, just fetching the latests offsets from the stream. This is
      // because we currently only have record lag for kafka, which can be lazily computed by subtracting the highest
      // task offsets from the latest offsets from the stream when it is needed
      latestSequenceFromStream =
              partitions.stream().collect(Collectors.toMap(StreamPartition::getPartitionId, recordSupplier::getPosition));
    }
    catch (InterruptedException e) {
      throw new StreamException(e);
    }
    finally {
      getRecordSupplierLock().unlock();
    }
  }

  protected void updatePartitionLagFromStream2() {
    PravegaEventSupplier supplier = (PravegaEventSupplier) recordSupplier;
    latestSequenceFromStream = supplier.getPartitionsTimeLag(getIoConfig().getStream(), getHighestCurrentOffsets());
  }

  @Override
  protected Map<String, StreamCut> getLatestSequencesFromStream()
  {
    return latestSequenceFromStream != null ? latestSequenceFromStream : new HashMap<>();
  }

  @Override
  protected String baseTaskName()
  {
    return "index_pravega";
  }

  @Override
  @VisibleForTesting
  public PravegaSupervisorIOConfig getIoConfig()
  {
    return spec.getIoConfig();
  }

  @VisibleForTesting
  public PravegaSupervisorTuningConfig getTuningConfig()
  {
    return spec.getTuningConfig();
  }
}
