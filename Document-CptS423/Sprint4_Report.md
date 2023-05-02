# Sprint 4 Report (04/03/23 - 05/02/2023)

## What's New (User Facing)
 * Build a druid release version.

## Work Summary (Developer Facing)
Provide a one paragraph synposis of what your team accomplished this sprint. Don't repeat the "What's New" list of features. Instead, help the instructor understand how you went about the work described there, any barriers you overcame, and any significant learnings for your team.

## Unfinished Work
- Continue modifying the pravega plugin dependencies in order to create a succesful release/build version of our project - we ran out of time and did not anticipate so many issues creating a build/release
- Continue adding test cases - we ran out of time and did not anticipate as many dependency issues as we experienced
- Create a multi threaded plugin via the utilization of multiple pravega readers - future work
- Add druid command line interface support - future work
- Incorporate the Pravega Schema Registry to support the reading of streams containing data types defined by the schema registry - future work
- Bug fixes and optimization of the Pravega Plugin - future work


## Completed Issues/User Stories
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/135
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/184
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/185
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/186
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/187
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/188
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/189
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/190
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/191
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/192
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/193
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/194
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/195
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/196
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/197
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/198
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/199
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/200
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/201
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/202
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/203
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/204
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/205
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/206
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/207
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/208
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/209
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/210
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/211
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/212
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/213
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/214
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/215
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/216
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/217
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/218
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/219
 
 ## Incomplete Issues/User Stories
 Here are links to issues we worked on but did not complete in this sprint:
 
 * URL of issue 1 <<One sentence explanation of why issue was not completed>>
 * URL of issue 2 <<One sentence explanation of why issue was not completed>>
 * URL of issue n <<One sentence explanation of why issue was not completed>>
 
 Examples of explanations (Remove this section when you save the file):
  * "We ran into a complication we did not anticipate (explain briefly)." 
  * "We decided that the feature did not add sufficient value for us to work on it in this sprint (explain briefly)."
  * "We could not reproduce the bug" (explain briefly).
  * "We did not get to this issue because..." (explain briefly)

## Code Files for Review
### Web Console
- [ingestion-spec.tsx](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/web-console/src/druid-models/ingestion-spec/ingestion-spec.tsx)
- [load-data-view.tsx](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/web-console/src/views/load-data-view/load-data-view.tsx)
### Pravega
- [StreamCutImpl.java](https://github.com/derekm/pravega/blob/c76e8773cdf758301dd335e4c652a660acc7a0fd/client/src/main/java/io/pravega/client/stream/impl/StreamCutImpl.java)
- [StreamCut.java](https://github.com/derekm/pravega/blob/c76e8773cdf758301dd335e4c652a660acc7a0fd/client/src/main/java/io/pravega/client/stream/StreamCut.java)
- [StreamCutTest.java](https://github.com/derekm/pravega/blob/a606aa7bc47a3aa9b52f200a78673895e5e9b9d9/client/src/test/java/io/pravega/client/stream/StreamCutTest.java)
### druid-pravega-indexing-service
#### Supervisor
- [PravegaSupervisor.java](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/supervisor/PravegaSupervisor.java)
- [PravegaSupervisorIOConfig.java](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/supervisor/PravegaSupervisorIOConfig.java)
- [PravegaSupervisorIngestionSpec.java](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/supervisor/PravegaSupervisorIngestionSpec.java)
- [PravegaSupervisorReportPayload.java](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/supervisor/PravegaSupervisorReportPayload.java)
- [PravegaSupervisorSpec.java](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/supervisor/PravegaSupervisorSpec.java)
- [PravegaSupervisorTuningConfig.java](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/supervisor/PravegaSupervisorTuningConfig.java)
#### Indexing
- [IncrementalPublishingPravegaIndexTaskRunner.java](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/IncrementalPublishingPravegaIndexTaskRunner.java)
- [PravegaConsumerConfigs.java](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/PravegaConsumerConfigs.java)
- [PravegaDataSourceMetadata.java](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/PravegaDataSourceMetadata.java)
- [PravegaEventSupplier.java](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/PravegaEventSupplier.java)
- [PravegaIndexTask.java](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/PravegaIndexTask.java)
- [PravegaIndexTaskClientFactory.java](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/PravegaIndexTaskClientFactory.java)
- [PravegaIndexTaskIOConfig.java](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/PravegaIndexTaskIOConfig.java)
- [PravegaIndexTaskModule.java](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/PravegaIndexTaskModule.java)
- [PravegaIndexTaskTuningConfig.java](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/PravegaIndexTaskTuningConfig.java)
- [PravegaSamplerSpec.java](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/PravegaSamplerSpec.java)
- [PravegaSequenceNumber.java](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/PravegaSequenceNumber.java)
 
## Retrospective Summary
Here's what went well: Having multiple team meetings where we practiced presentations and worked on powerpoints together. This was beneficial for us because we all got good practice presenting the powerpoints we created which allowed us to give a good poster and client mvp presentation. Asking questions via slack to dell and druid community to get help when trying to create a build release of our project.
 
Here's what we'd like to improve:
   * N/A
  
Here are changes we plan to implement in the next sprint:
   * N/A
