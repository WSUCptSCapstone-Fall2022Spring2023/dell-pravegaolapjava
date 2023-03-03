# Sprint 2 Report (02/02/23 - 03/02/2023)

## What's New (User Facing)
Changes to the Druid web console have been made for users to be able to access our plugin through the web console. When a user launches the web console through a localhost, they can select: Load Data -> Streaming. Different cards will be shown for the user to select which data source they want to read from. Things like Apache Kafka, Amazon Kinesis, and of course, Pravega. 

## Work Summary (Developer Facing)
Continued working with Dell engineer to modify the existing Kafka Connector to be compatible with the Pravega API. The most work was put into the `PravegaEventSupplier.java` file because this file deals with the data source we are reading from specifically. This meant that the utilization of the Pravega API would occur here for things such as reading events from Pravega, returning the head/tail of a stream and seeking to a particular position in a stream.

Usage of Pravega ReaderGroup abstraction level occurred to manage the reading of streams and handling of offsets easier. There are potential clashes from the high level readergroup abstraction level and the lower level `SeekableStreamSupervisor.java` that still need to be investigated further.

Changed to other files such as `PravegaSamplerSpec.java` occurred. In this file, we made additions to the consumerProperties hashmap. This is an object that maps strings to objects. It is created from user input on the web console. We needed to add certain fields to this map that the Pravega API needs to access, things like scopedStreamName and readerGroupName. This means that when a user is using the web console to ingest information, they will input this information.

## Unfinished Work
1. IncrementalPublishingPravegaIndexTaskRunner.java
2. PravegaConsumerConfigs.java
3. PravegaDataSourceMetadata.java
4. PravegaIndexTask.java
5. PravegaIndexTaskClientFactory.java
6. PravegaIndexTaskModule.java
7. PravegaIndexTaskTuningConfig.java
8. PravegaPositionBuffer.java
9. PravegaSequenceNumber.java
10. PravegaSupervisor.java
11. PravegaSupervisorIngestionSpec.java
12. PravegaSupervisorReportPayload.java
13. PravegaSupervisorSpec.java
14. PravegaSupervisorTuningConfig.java

## Completed Issues/User Stories
Here are links to the issues that we completed in this sprint:

 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/123
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/124
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/125
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/126
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/127
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/128
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/129
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/130
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/131
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/132
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/133
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/134
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/136
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/137
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/138
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/139
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/140
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/141
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/142
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/143
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/144
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/145
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/145
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/147
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/148
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/149
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/150
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/151
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/152
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/153
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/154
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/155
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/156
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/157
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/158
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/159

 ## Incomplete Issues/User Stories
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/135 - Not completed, still more tests that need to be researched. Currently the `PravegaEventSupplier.java` tests have been reviewed and converted so that they can be added to our MVP report draft two.

https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/147 - Not completed, still need to research `SeekableStreamIndexTaskRunner.java` more. Now that the team has an understanding of `SeekableStreamSupervisor.java` we can begin looking at the more lower level behind the scenes work that is occuring that the supervisor does not involve itself with directly.


## Code Files for Review
 * [PravegaEventSupplier.java](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/PravegaEventSupplier.java)
 * [PravegaSamplerSpec.java](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/PravegaSamplerSpec.java)
 * [PravegaSupervisorIOConfig.java](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/supervisor/PravegaSupervisorIOConfig.java)
 * [ingestion-spec.tsx](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/web-console/src/druid-models/ingestion-spec/ingestion-spec.tsx)
 * [load-data-view.tsx](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/web-console/src/views/load-data-view/load-data-view.tsx)
 
## Retrospective Summary
Here's what went well: Scheduling weekly Druid work sessions with the team and any of the client members that are able to make it. These work sessions allowed us all to really dive into and fully ingest the code of the Kafka connector. Studying the code and working on issues before these meetings allowed us to show up prepared and knoweledgeable about the codebase so that we are able to follow along, ask meaningful questions and contribute problem solving ideas. Taking notes and creating diagrams for the code we studied also helped us understand the code more. Proactive creation of issues allowed us to be more organized and keep track of all the work that was being done.

Here's what we'd like to improve: We would like to get better at reading and studying code. Currently it takes us a long time to read and understand unfamiliar druid code since it very complex. By taking notes on the code and creating high level diagrams, we can start to ingest the code more efficiently.

Here are changes we plan to implement in the next sprint: 
 * Add logic for Pravega API checkpointing whenever Druid signals that a checkpoint must occur.
 * Implement a sort of object wrapper for a Pravega ByteBuffer. ByteBuffer contains positional offset information for a Pravega stream. Druid source code wants direct access to these offsets in order to do offset comparison logic during a tasks execution. With this wrapper, we'll be able to deserialize our byte buffer and do our own offset comparison logic.
 *  Continue making changes to the rest of the Pravega files listed under `Unfinished Work` above
