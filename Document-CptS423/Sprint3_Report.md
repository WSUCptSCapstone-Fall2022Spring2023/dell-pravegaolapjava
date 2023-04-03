# Sprint 3 Report (03/02/23 - 04/02/2023)

## What's New (User Facing)
After further testing, users of Pravega's client API will have access to the `compareTo()` method within a stream cut. 
This will allow for a user to compare their current stream cut to another stream cut. A stream cut is a snapshot of a consistent 
point in time of a stream. It can be used as a marker so that a Pravega reader can begin consuming events from this stream cut marker.
Users will have access to this because it is a public method that was needed to be created for the druid pravega extension since druid 
makes calls to an overloaded `compareTo()`.

## Work Summary (Developer Facing)
During our recent consultation with druid experts and work with dell experts, we have identified an issue regarding 
the compatibility between SeekableStream abstraction and Pravega ReaderGroup APIs. The SeekableStream abstraction is 
designed to track ingestion partition, offsets, and checkpoints. However, the existing Pravega ReaderGroup APIs do 
not provide the required low-level access to Pravega stream, causing compatibility concerns.

To address this incompatibility, we have identified two potential solutions:
1. Develop an additional druid indexing service infrastructure that is compatible with the Pravega ReaderGroup level abstraction. 
This would involve creating a new indexing service layer specifically designed to work seamlessly with the Pravega ReaderGroup APIs.
2. Collaborate with the Pravega team to enhance the existing APIs, allowing low-level access to Pravega stream.
By extending the current APIs, we could bridge the gap between the SeekableStream abstraction and Pravega ReaderGroup APIs, ensuring compatibility.

We went and work on Pravega APIs. The primary work in this Sprint is to make the Pravega StreamCut comparable so we can determine the relative position
of two streamcuts within the stream.

## Unfinished Work
- Continue modifying the druid pravega plugin to support the Pravega client API's readergroups. We will begin next sprint by adding calls to Pravega's StreamCut's `compareTo()` within the extension.
- Ensure that stream cuts are passed forward from the druid indexing service to our pravega extension so that we can handle these stream cuts.
- Implement Pravega checkpointing.

## Completed Issues/User Stories
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/135
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/146
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/147
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/160
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/161
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/162
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/163
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/164
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/165
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/166
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/167
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/168
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/169
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/170
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/171
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/172
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/173
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/174
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/175
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/176
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/177
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/178
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/179
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/180
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/181
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/182
https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/183

 ## Incomplete Issues/User Stories
- NA

## Code Files for Review
- [StreamCutImpl.java](https://github.com/derekm/pravega/blob/c76e8773cdf758301dd335e4c652a660acc7a0fd/client/src/main/java/io/pravega/client/stream/impl/StreamCutImpl.java)
- [StreamCut.java](https://github.com/derekm/pravega/blob/c76e8773cdf758301dd335e4c652a660acc7a0fd/client/src/main/java/io/pravega/client/stream/StreamCut.java)
- [StreamCutTest.java](https://github.com/derekm/pravega/blob/a606aa7bc47a3aa9b52f200a78673895e5e9b9d9/client/src/test/java/io/pravega/client/stream/StreamCutTest.java)

## Retrospective Summary
Here's what went well: Making sure we have a list of coherent questions to ask Tom during the client meetings. This was important because as we began making Pravega client API changes to create the `compareTo()` stream cut method, we needed to reach out to Tom, a Pravega client API expert to guide us on the creation of this method. This allowed us to create a method that should behave as expected when comparing a stream cut to another. Another thing that went well was the continued creation of diagrams to use as a visual aid when studying code. This allowed us to better understand the code we were analyzing.

Here's what we'd like to improve: Scheduling meetings at times where everyone is available. Our existing client meetings fell off the calendar which nobody noticed. This led to us scheduling meetings at new times since everyone forgot about the meeting. This was an issue becuase everyone has conflicting schedules, but now recurring meetings where everyone is available have been created. Another thing we'd like to improve are the creation of tests. The team did a good job this sprint creating tests for the StreamCut.compareTo() method, but we would like to improve our creation of Druid tests. Druid tests have been difficult to create because the current tests are modeled after the similar Apache Kafka plugin, which utilizes Kafka client API methods. This makes converting them difficult because the Pravega client API is not the same as Kafka so the methods are not easily translatable. Another thing is that there are a lot of dependencies within these test files. It is often the case where we try to create test cases but the necessary components fo the druid pravega extension have not been implemented yet.

Here are changes we plan to implement in the next sprint: We plan to wrap up our testing and refactoring of the `streamCut.compareTo()` Pravega client API method so that we can switch gears and continue modifying the druid pravega plugin to support the Pravega client API's readergroups. We will begin next sprint by adding calls to Pravega's StreamCut's `compareTo()` within the extension. With this, we will have to ensure that stream cuts are passed forward from the druid indexing service to our pravega extension so that we can handle these stream cuts by using the compareTo() method. Lastly, we will need to implement Pravega checkpointing because Druid can checkpoint, but we need Pravega checkpoints because Pravega streams can be very large and if a failure occurs, we do not want to start over and read from the very begininning. After this, the prototype of the plugin will be in good shape and the team will likely switch gears to start polishing up the extension to get it into a state where it can compile and be tested.
