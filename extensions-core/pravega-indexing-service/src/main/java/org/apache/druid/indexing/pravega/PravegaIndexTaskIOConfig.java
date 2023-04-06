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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import io.pravega.client.stream.StreamCut;
import org.apache.druid.data.input.InputFormat;
import org.apache.druid.indexing.pravega.supervisor.PravegaSupervisorIOConfig;
import org.apache.druid.indexing.seekablestream.SeekableStreamEndSequenceNumbers;
import org.apache.druid.indexing.seekablestream.SeekableStreamIndexTaskIOConfig;
import org.apache.druid.indexing.seekablestream.SeekableStreamStartSequenceNumbers;
import org.apache.druid.indexing.seekablestream.extension.PravegaConfigOverrides;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.util.Map;

public class PravegaIndexTaskIOConfig extends SeekableStreamIndexTaskIOConfig<String, StreamCut>
{
  private final Map<String, Object> consumerProperties;
  private final long pollTimeout;
  private final PravegaConfigOverrides configOverrides;

  @JsonCreator
  public PravegaIndexTaskIOConfig(
      @JsonProperty("taskGroupId") @Nullable Integer taskGroupId, // can be null for backward compabitility
      @JsonProperty("baseSequenceName") String baseSequenceName,
      // startPartitions and endPartitions exist to be able to read old ioConfigs in metadata store
      @JsonProperty("startPartitions") @Nullable
      @Deprecated SeekableStreamEndSequenceNumbers<String, StreamCut> startPartitions,
      @JsonProperty("endPartitions") @Nullable
      @Deprecated SeekableStreamEndSequenceNumbers<String, StreamCut> endPartitions,
      // startSequenceNumbers and endSequenceNumbers must be set for new versions
      @JsonProperty("startSequenceNumbers")
      @Nullable SeekableStreamStartSequenceNumbers<String, StreamCut> startSequenceNumbers,
      @JsonProperty("endSequenceNumbers")
      @Nullable SeekableStreamEndSequenceNumbers<String, StreamCut> endSequenceNumbers,
      @JsonProperty("consumerProperties") Map<String, Object> consumerProperties,
      @JsonProperty("pollTimeout") Long pollTimeout,
      @JsonProperty("useTransaction") Boolean useTransaction,
      @JsonProperty("minimumMessageTime") DateTime minimumMessageTime,
      @JsonProperty("maximumMessageTime") DateTime maximumMessageTime,
      @JsonProperty("inputFormat") @Nullable InputFormat inputFormat,
      @JsonProperty("configOverrides") @Nullable PravegaConfigOverrides configOverrides
  )
  {
    super(
        taskGroupId,
        baseSequenceName,
        startSequenceNumbers == null
        ? Preconditions.checkNotNull(startPartitions, "startPartitions").asStartPartitions(true)
        : startSequenceNumbers,
        endSequenceNumbers == null ? endPartitions : endSequenceNumbers,
        useTransaction,
        minimumMessageTime,
        maximumMessageTime,
        inputFormat
    );

    this.consumerProperties = Preconditions.checkNotNull(consumerProperties, "consumerProperties");
    this.pollTimeout = pollTimeout != null ? pollTimeout : PravegaSupervisorIOConfig.DEFAULT_POLL_TIMEOUT_MILLIS;
    this.configOverrides = configOverrides;

    final SeekableStreamEndSequenceNumbers<String, StreamCut> myEndSequenceNumbers = getEndSequenceNumbers();
    for (String partition : myEndSequenceNumbers.getPartitionSequenceNumberMap().keySet()) {
      Preconditions.checkArgument(
          myEndSequenceNumbers.getPartitionSequenceNumberMap()
                              .get(partition)
                              .compareTo(getStartSequenceNumbers().getPartitionSequenceNumberMap().get(partition)) >= 0,
          "end offset must be >= start offset for partition[%s]",
          partition
      );
    }
  }

  public PravegaIndexTaskIOConfig(
      int taskGroupId,
      String baseSequenceName,
      SeekableStreamStartSequenceNumbers<String, StreamCut> startSequenceNumbers,
      SeekableStreamEndSequenceNumbers<String, StreamCut> endSequenceNumbers,
      Map<String, Object> consumerProperties,
      Long pollTimeout,
      Boolean useTransaction,
      DateTime minimumMessageTime,
      DateTime maximumMessageTime,
      InputFormat inputFormat,
      PravegaConfigOverrides configOverrides
  )
  {
    this(
        taskGroupId,
        baseSequenceName,
        null,
        null,
        startSequenceNumbers,
        endSequenceNumbers,
        consumerProperties,
        pollTimeout,
        useTransaction,
        minimumMessageTime,
        maximumMessageTime,
        inputFormat,
        configOverrides
    );
  }

  @JsonProperty
  public Map<String, Object> getConsumerProperties()
  {
    return consumerProperties;
  }

  @JsonProperty
  public long getPollTimeout()
  {
    return pollTimeout;
  }

  @JsonProperty
  public PravegaConfigOverrides getConfigOverrides()
  {
    return configOverrides;
  }

  @Override
  public String toString()
  {
    return "PravegaIndexTaskIOConfig{" +
           "taskGroupId=" + getTaskGroupId() +
           ", baseSequenceName='" + getBaseSequenceName() + '\'' +
           ", startSequenceNumbers=" + getStartSequenceNumbers() +
           ", endSequenceNumbers=" + getEndSequenceNumbers() +
           ", consumerProperties=" + consumerProperties +
           ", pollTimeout=" + pollTimeout +
           ", useTransaction=" + isUseTransaction() +
           ", minimumMessageTime=" + getMinimumMessageTime() +
           ", maximumMessageTime=" + getMaximumMessageTime() +
           ", configOverrides=" + getConfigOverrides() +
           '}';
  }
}
