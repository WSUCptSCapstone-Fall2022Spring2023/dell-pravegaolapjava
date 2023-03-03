# Sprint x Report (8/26/21 - 9/24/2021)

## What's New (User Facing)
Changes to the Druid web console have been made for users to be able to access our plugin through the web console. When a user launches the web console through a localhost, they can select: Load Data -> Streaming. Different cards will be shown for the user to select which data source they want to read from. Things like Apache Kafka, Amazon Kinesis, and of course, Pravega. 

## Work Summary (Developer Facing)
Continued working with Dell engineer to modify the existing Kafka Connector to be compatible with the Pravega API. The most work was put into the `PravegaEventSupplier.java` file because this file deals with the data source we are reading from specifically. This meant that the utilization of the Pravega API would occur here for things such as reading events from Pravega, returning the head/tail of a stream and seeking to a particular position in a stream.

Changed to other files such as `PravegaSamplerSpec.java` occurred. In this file, we made additions to the consumerProperties hashmap. This is an object that maps strings to objects. It is created from user input on the web console. We needed to add certain fields to this map that the Pravega API needs to access, things like scopedStreamName and readerGroupName. This means that when a user is using the web console to ingest information, they will input this information.

## Unfinished Work


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
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/135
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
 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/147

 ## Incomplete Issues/User Stories

## Code Files for Review
 * [PravegaEventSupplier.java](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/PravegaEventSupplier.java)
 * [PravegaSamplerSpec.java](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-connector/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/pravega/PravegaSamplerSpec.java)
 
## Retrospective Summary
