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

package org.apache.druid.data.input.kafka;

import org.apache.druid.data.input.InputFormat;
import org.apache.druid.data.input.impl.ByteEntity;
import org.apache.druid.indexing.kafka.PravegaEventSupplier;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * A {@link ByteEntity} generated by {@link PravegaEventSupplier} and fed to any {@link InputFormat} used by Kafka
 * indexing tasks.
 * <p>
 * It can be used as a regular ByteEntity, in which case the Kafka record value is returned, but the {@link #getRecord}
 * method also allows Kafka-aware {@link InputFormat} implementations to read the full Kafka record, including headers,
 * key, and timestamp.
 * <p>
 * NOTE: Any records with null values will be skipped, even if they contain non-null keys, or headers
 * <p>
 * This functionality is not yet exposed through any built-in InputFormats, but is available for use in extensions.
 */
public class KafkaRecordEntity extends ByteEntity
{
  private final ConsumerRecord<byte[], byte[]> record;

  public KafkaRecordEntity(ConsumerRecord<byte[], byte[]> record)
  {
    super(record.value());
    this.record = record;
  }

  public ConsumerRecord<byte[], byte[]> getRecord()
  {
    return record;
  }
}
