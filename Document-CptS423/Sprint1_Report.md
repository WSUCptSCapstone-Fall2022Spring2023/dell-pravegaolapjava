# Sprint x Report (8/26/21 - 9/24/2021)

## What's New (User Facing)
Druid Web console added that is hosted via AWS EC2 allows for anyone to access it. No added functionality though, user's cannot access the Pravega plugin becuase it is not completed and because the web console does not have access to our project yet. Link is below: 
http://35.89.55.203:8888/unified-console.html

## Work Summary (Developer Facing)
The team met with the Dell client to set up a running Druid web console instance on a Dell owned AWS EC2 server. This was done by creating four servers: master, data1, data2, and query, which are all required for a Druid web console to be hosted and accessible to all. Zookeeper was used so that the four separate running instances could connect to a shared Zookeeper instance and recognize each other. 

The Apache Kafka - Druid connector was copied and pasted into the `apache/druid/extensions-core/pravega-indexing-service` directory where the team has been working to study the Kafka connector and convert it to be compatible with Pravega. Our work began within the `KafkaRecordEntity.java` file because it contained a method named poll() which was of interest since it took charge of fetching records from Kafka. We began here and modified it to `PravegaEventEntity.java` and the poll method is now compatible to read events contained within the streams of Pravega.

## Unfinished Work
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/105 - Modify Pravega Writer/Reader to support JSON instead of string was not completed. This was planned because JSON is compatible with Druid, but this issue was never completed because we began working with Dell client in our Druid work sessions to scrap our original code and instead work by copying and pasting the kafka connector and renaming it as the pravega connector since they are very similar.

The rest of the unfinished work is to convert the existing Kafka connector to be a compatible with Pravega in order to produce a Pravega connector. Further investigation of the existing Kafka connector as well as the Amazon Kinesis connector, maybe the pending Pulsar connector too will be required to produce the Pravega connector. 

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
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/123 <<Issue was not completed initially becaause we were unsure if poll() would be called multiple times within a loop, or if we would be looping within poll() to grab all events. Will complete in sprint 2>>

https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/124 <<Issue was not completed initially because we were under the assumption that we could use partitionIDs to identify our streams inside of poll(). When meeting with our client we found out that we could use readergroup IDs instead. Will complete in sprint 2>>

## Code Files for Review
 * [PravegaEventSupplier.java](https://github.com/jose-robles2/druid/blob/pravega-index-service/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/indexing/kafka/PravegaEventSupplier.java)
 
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
