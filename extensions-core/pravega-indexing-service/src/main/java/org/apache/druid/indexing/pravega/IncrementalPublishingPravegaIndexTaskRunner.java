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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.pravega.client.stream.StreamCut;
import org.apache.druid.data.input.impl.ByteEntity;
import org.apache.druid.data.input.impl.InputRowParser;
import org.apache.druid.indexing.common.LockGranularity;
import org.apache.druid.indexing.common.TaskToolbox;
import org.apache.druid.indexing.seekablestream.SeekableStreamDataSourceMetadata;
import org.apache.druid.indexing.seekablestream.SeekableStreamEndSequenceNumbers;
import org.apache.druid.indexing.seekablestream.SeekableStreamIndexTaskRunner;
import org.apache.druid.indexing.seekablestream.SeekableStreamSequenceNumbers;
import org.apache.druid.indexing.seekablestream.SequenceMetadata;
import org.apache.druid.indexing.seekablestream.common.OrderedPartitionableRecord;
import org.apache.druid.indexing.seekablestream.common.OrderedSequenceNumber;
import org.apache.druid.indexing.seekablestream.common.RecordSupplier;
import org.apache.druid.indexing.seekablestream.common.StreamPartition;
import org.apache.druid.java.util.common.ISE;
import org.apache.druid.java.util.emitter.EmittingLogger;
import org.apache.druid.server.security.AuthorizerMapper;
import org.apache.druid.utils.CollectionUtils;
import org.apache.kafka.clients.consumer.OffsetOutOfRangeException;
import org.apache.kafka.common.TopicPartition;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class IncrementalPublishingPravegaIndexTaskRunner extends SeekableStreamIndexTaskRunner<String, StreamCut, ByteEntity>
{
  private static final EmittingLogger log = new EmittingLogger(IncrementalPublishingPravegaIndexTaskRunner.class);
  private final PravegaIndexTask task;

  IncrementalPublishingPravegaIndexTaskRunner(
      PravegaIndexTask task,
      @Nullable InputRowParser<ByteBuffer> parser,
      AuthorizerMapper authorizerMapper,
      LockGranularity lockGranularityToUse
  )
  {
    super(
        task,
        parser,
        authorizerMapper,
        lockGranularityToUse
    );
    this.task = task;
  }

  // same behavior here currently as the kinesis connector since we can't do +1 in pravega like kafka does
  @Override
  protected StreamCut getNextStartOffset(@NotNull StreamCut sequenceNumber)
  {
    return sequenceNumber;
  }

  @Nonnull
  @Override
  protected List<OrderedPartitionableRecord<String, StreamCut, ByteEntity>> getRecords(
      RecordSupplier<String, StreamCut, ByteEntity> recordSupplier,
      TaskToolbox toolbox
  )
  {
    return recordSupplier.poll(task.getIOConfig().getPollTimeout());
  }

  @Override
  protected SeekableStreamEndSequenceNumbers<String, StreamCut> deserializePartitionsFromMetadata(
      ObjectMapper mapper,
      Object object
  )
  {
    return mapper.convertValue(object, mapper.getTypeFactory().constructParametrizedType(
        SeekableStreamEndSequenceNumbers.class,
        SeekableStreamEndSequenceNumbers.class,
        String.class,
        ByteBuffer.class
    ));
  }

  @Override
  protected SeekableStreamDataSourceMetadata<String, StreamCut> createDataSourceMetadata(
      SeekableStreamSequenceNumbers<String, StreamCut> partitions
  )
  {
    return new PravegaDataSourceMetadata(partitions);
  }

  @Override
  protected OrderedSequenceNumber<StreamCut> createSequenceNumber(StreamCut sequenceNumber)
  {
    // take in the byte buffer to transform to a stream cut
    // allows for comparisons inside here
    // we'd return orderedseqNumber that is a version of our wrapper that can do comparisons
    return PravegaSequenceNumber.of(sequenceNumber);
  }

  @Override
  protected void possiblyResetDataSourceMetadata(
      TaskToolbox toolbox,
      RecordSupplier<String, StreamCut, ByteEntity>recordSupplier,
      Set<StreamPartition<String>> assignment
  )
  {
    // do nothing
    // TODO: revisit this because kinesis calls this, but kafka connector calls a different "possibly" method
  }

  @Override
  protected boolean isEndOffsetExclusive()
  {
    return true;
  }

  @Override
  protected boolean isEndOfShard(StreamCut seqNum)
  {
    return false;
  }

  @Override
  public TypeReference<List<SequenceMetadata<String, StreamCut>>> getSequenceMetadataTypeReference()
  {
    return new TypeReference<List<SequenceMetadata<String, StreamCut>>>()
    {
    };
  }

  // Right now we think that the integer refers to a task, so since we just have one, we think integer == 1
  @Nullable
  @Override
  protected TreeMap<Integer, Map<String, StreamCut>> getCheckPointsFromContext(
      TaskToolbox toolbox,
      String checkpointsString
  ) throws IOException
  {
    if (checkpointsString != null) {
      log.debug("Got checkpoints from task context[%s].", checkpointsString);
      return toolbox.getJsonMapper().readValue(
          checkpointsString,
          new TypeReference<TreeMap<Integer, Map<String, StreamCut>>>()
          {
          }
      );
    } else {
      return null;
    }
  }
}

