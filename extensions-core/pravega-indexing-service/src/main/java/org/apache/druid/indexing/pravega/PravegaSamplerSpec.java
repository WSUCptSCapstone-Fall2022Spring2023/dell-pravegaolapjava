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

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.druid.common.utils.IdUtils;
import org.apache.druid.indexing.pravega.supervisor.PravegaSupervisorIOConfig;
import org.apache.druid.indexing.pravega.supervisor.PravegaSupervisorSpec;
import org.apache.druid.indexing.overlord.sampler.InputSourceSampler;
import org.apache.druid.indexing.overlord.sampler.SamplerConfig;
import org.apache.druid.indexing.seekablestream.SeekableStreamSamplerSpec;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PravegaSamplerSpec extends SeekableStreamSamplerSpec
{
  private final ObjectMapper objectMapper;

  @JsonCreator
  public PravegaSamplerSpec(
      @JsonProperty("spec") final PravegaSupervisorSpec ingestionSpec,
      @JsonProperty("samplerConfig") @Nullable final SamplerConfig samplerConfig,
      @JacksonInject InputSourceSampler inputSourceSampler,
      @JacksonInject ObjectMapper objectMapper
  )
  {
    super(ingestionSpec, samplerConfig, inputSourceSampler);

    this.objectMapper = objectMapper;
  }

  // our pravegasamplerspec will instantiate our pravegaeventsupplier
  @Override
  protected PravegaEventSupplier createRecordSupplier()
  {
    ClassLoader currCtxCl = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader(getClass().getClassLoader());

      final Map<String, Object> props = new HashMap<>(((PravegaSupervisorIOConfig) ioConfig).getConsumerProperties());

      props.put("scopedStreamName", ioConfig.getStream());
      props.put("readerGroupName", IdUtils.getRandomIdWithPrefix(dataSchema.getDataSource())); // set our own prop, user supplies this uniqueName in getDataSrc, add random uuid. This uuid gives us an option, whether we want to passforward readergroup name, also task replicas need separate readergroup names.

      return new PravegaEventSupplier(props, objectMapper, ((PravegaSupervisorIOConfig) ioConfig).getConfigOverrides());
    }
    finally {
      Thread.currentThread().setContextClassLoader(currCtxCl);
    }
  }
}
