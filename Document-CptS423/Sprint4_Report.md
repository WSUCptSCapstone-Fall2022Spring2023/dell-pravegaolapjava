# Sprint x Report (8/26/21 - 9/24/2021)

## What's New (User Facing)
 * Feature 1 or Bug Fix 1
 * Feature 2 or Bug Fix 2
 * Feature n or Bug Fix n

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
Here are links to the issues that we completed in this sprint:

 * URL of issue 1
 * URL of issue 2
 * URL of issue n

 Reminders (Remove this section when you save the file):
  * Each issue should be assigned to a milestone
  * Each completed issue should be assigned to a pull request
  * Each completed pull request should include a link to a "Before and After" video
  * All team members who contributed to the issue should be assigned to it on GitHub
  * Each issue should be assigned story points using a label
  * Story points contribution of each team member should be indicated in a comment
 
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
- https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/web-console/src/druid-models/ingestion-spec/ingestion-spec.tsx
- https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/web-console/src/views/load-data-view/load-data-view.tsx
### Pravega
- https://github.com/derekm/pravega/blob/c76e8773cdf758301dd335e4c652a660acc7a0fd/client/src/main/java/io/pravega/client/stream/impl/StreamCutImpl.java
- https://github.com/derekm/pravega/blob/c76e8773cdf758301dd335e4c652a660acc7a0fd/client/src/main/java/io/pravega/client/stream/StreamCut.java
- https://github.com/derekm/pravega/blob/a606aa7bc47a3aa9b52f200a78673895e5e9b9d9/client/src/test/java/io/pravega/client/stream/StreamCutTest.java
### druid-pravega-indexing-service
#### Supervisor
- [PravegaSupervisor.java]https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/supervisor/PravegaSupervisor.java
- https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/supervisor/PravegaSupervisorIOConfig.java
- https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/supervisor/PravegaSupervisorIngestionSpec.java
- https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/supervisor/PravegaSupervisorReportPayload.java
- https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/supervisor/PravegaSupervisorSpec.java
- https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/supervisor/PravegaSupervisorTuningConfig.java
#### Indexing
- https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/IncrementalPublishingPravegaIndexTaskRunner.java
- https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/PravegaConsumerConfigs.java
- https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/PravegaDataSourceMetadata.java
- https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/PravegaEventSupplier.java
- https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/PravegaIndexTask.java
- https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/PravegaIndexTaskClientFactory.java
- https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/PravegaIndexTaskIOConfig.java
- https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/PravegaIndexTaskModule.java
- https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/PravegaIndexTaskTuningConfig.java
- https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/PravegaSamplerSpec.java
- https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/PravegaSequenceNumber.java
 
## Retrospective Summary
Here's what went well:
  * Item 1
  * Item 2
  * Item x
 
Here's what we'd like to improve:
   * Item 1
   * Item 2
   * Item x
  
Here are changes we plan to implement in the next sprint:
   * Item 1
   * Item 2
   * Item x
