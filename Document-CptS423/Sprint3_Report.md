# Sprint 3 Report (03/02/23 - 04/02/2023)

## What's New (User Facing)

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
N/A

## Completed Issues/User Stories


 ## Incomplete Issues/User Stories

## Code Files for Review

## Retrospective Summary
