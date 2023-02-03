Sprint 3 Report (8/26/21 - 9/24/2021)

## What's New (User Facing)
 * Study StreamSeekable APIs. 
 * Study ingestion dependency tool Guice.
 * Read and understand similar ingestion plugin source code, Kafka and Kinesis. 
 * Separate connection to be handled by connector.


## Work Summary (Developer Facing)
During sprint three, the team was able to build on the foundation created by adding the files responsible for writing and reading to and from Pravega in sprint2. Some new code was added to parse the fetched events from Pravega (via the read file) into sequences of bytes. Also, a new file known as Connector.java was added to establish a connection with Pravega and read all events by using the Pravega client API. 

Base knowledge of the kafka extension within Apache Druid has been established. This is a plugin between the stream storage technology known as Kafka and the OLAP database, Druid. This plugin implements an architecture that is similar to our proposed architecture for the Pravega extension within Apache Druid. Throughout the sprint the team has been investigating the source code for this kafka extension. Although very complex, the team has made several discoveries that will help us begin to ingest our read events from Pravega into Druid. See sprint3 video on the repo for further details. 

## Unfinished Work
Indexing segments read from Pravega to Druid.	
For indexing Segments to Druid, we have not progressed as much as we anticipated. We intended on writing the fetched events from the Pravega StreamReader into the Deep Storage of Apache Druid before the end of the semester, but due to the complexity of the Kafka extension for Apache Druid, we were not able to do so. 
Although the team has still made progress in researching and becoming familiar with the Kafa extension so there is still progress being made. We plan on working more closely with Dell and the Apache Druid team to help us make more progress on this avenue next semester
## Completed Issues/User Stories
Here are links to the issues that we completed in this sprint:

- #60 
Create a reader and writer within pravega-indexing-jose - Research
- #61
Modify reader and writer templates to support multiple segments within a stream - Sprint Report
- #64
Write “work Summary” - Sprint Report 
- #66
Write "Completed Issues/User Stories" - Sprint report 
- #67
Write "Incomplete Issues/User Stories" - Sprint Report 
- #68
Write "Code Files for review" - Sprint Report
- #69
Write "Retrospective Summary" - Sprint report 
- #70
Set up a virtual machine - environment Setup
- #71
Install and Research Maven - Research
- #72
Research Druid indexing documentation - Research
- #73
Start researching Junit - Research
- #74
added demo video - Sprint Report 
- #75
Write Introduction - Prototype Report
- #76
Add Bios and Project Roles Section - Prototype Report
- #77
Write Project Requirements - Prototype Report
- #78
Write Solution Approach -  Prototype Report
- #79
Write Alpha Prototype Description - -Prototype Report
- #80
Write Alpha Prototype Demonstration - Prototype Report
- #81
Write Future Work - Prototype Report
- #82
Add more keywords to glossary - -Prototype Report
- #83
Project Overview and Requirements Slide - Presentation
- #84
Project Solution Approach Slide - Presentation
- #85
Project's Current Status Slide - Presentation
- #86
Project's Future Work Slide - Presentation
- #87
Update Scrum Note Entries - Documentation 
- #88
Update Client Note entries - Documentation 
- #89
Submit Client Presentation - -Presentation
- #90
Peer Review Prototype Project Report
- #91
Sprint 3 Demo video - Sprint Report
- #92
Update README - Sprint Report
- #93
add all scrum notes - Sprint Report
- #94
What's New - Sprint Report
- #95
Work Summary -  Sprint Report
- #96
Completed Issues/User Stories - Sprint Report
- #97
Incomplete Issues/User Stories - Sprint Report
- #98
Code Files for Review - Sprint Report
- #99
Retrospective Summary -  Sprint Report


## Incomplete Issues/User Stories
N/A

## Code Files for Review
Please review the following code files, which were actively developed during this sprint, for quality:
 * [Connector](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-index-service/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/Connector.java)
 
## Retrospective Summary
Here's what went well:
As usual consulting with the client has been very helpful. 
The client feedback is a crucial part of our development process. Communicating with the client allows us to confirm we are approaching the problem with  the right approach. 
Communicating with team members.
Our weekly meeting combined with our discord chat have allowed the team to catch up on each other's progress and learn what other teammates have discovered. This has overall improved our efficiency as a team.
Reading the Pravega documentation and samples.
Reading through  Pravega documentation and looking at the samples  have allowed the team to get an even better understanding of the project. 

Here's what we'd like to improve:
The team would like to progress faster in terms of indexing segments to Druid table.
Currently we have been able to connect, write and read with Pravega However on the Druid end we are still researching resource and API documentation to get a more comprehensive understanding of the inner workings of Druid API. We plan to speed up or research in order to be able to move forward with implementation.

