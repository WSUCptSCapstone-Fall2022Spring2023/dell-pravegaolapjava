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

## Code Files for Review
- [StreamCutImpl.java](https://github.com/derekm/pravega/blob/c76e8773cdf758301dd335e4c652a660acc7a0fd/client/src/main/java/io/pravega/client/stream/impl/StreamCutImpl.java)
- [StreamCut.java](https://github.com/derekm/pravega/blob/c76e8773cdf758301dd335e4c652a660acc7a0fd/client/src/main/java/io/pravega/client/stream/StreamCut.java)
- [StreamCutTest.java](https://github.com/derekm/pravega/blob/a606aa7bc47a3aa9b52f200a78673895e5e9b9d9/client/src/test/java/io/pravega/client/stream/StreamCutTest.java)

## Retrospective Summary
Here's what went well:

Here's what we'd like to improve:

Here are changes we plan to implement in the next sprint:
