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
import io.pravega.client.stream.StreamCut;
import org.apache.druid.indexing.overlord.DataSourceMetadata;
import org.apache.druid.indexing.seekablestream.SeekableStreamDataSourceMetadata;
import org.apache.druid.indexing.seekablestream.SeekableStreamEndSequenceNumbers;
import org.apache.druid.indexing.seekablestream.SeekableStreamSequenceNumbers;

import java.nio.ByteBuffer;

public class PravegaDataSourceMetadata extends SeekableStreamDataSourceMetadata<String, StreamCut>
{

  @JsonCreator
  public PravegaDataSourceMetadata(
      @JsonProperty("partitions") SeekableStreamSequenceNumbers<String, StreamCut> pravegaPartitions
  )
  {
    super(pravegaPartitions);
  }

  @Override
  public DataSourceMetadata asStartMetadata()
  {
    final SeekableStreamSequenceNumbers<String, StreamCut> sequenceNumbers = getSeekableStreamSequenceNumbers();
    if (sequenceNumbers instanceof SeekableStreamEndSequenceNumbers) {
      return createConcreteDataSourceMetaData(
          ((SeekableStreamEndSequenceNumbers<String, StreamCut>) sequenceNumbers).asStartPartitions(true)
      );
    } else {
      return this;
    }
  }

  @Override
  protected SeekableStreamDataSourceMetadata<String, StreamCut> createConcreteDataSourceMetaData(
      SeekableStreamSequenceNumbers<String, StreamCut> seekableStreamSequenceNumbers
  )
  {
    return new PravegaDataSourceMetadata(seekableStreamSequenceNumbers);
  }
}
