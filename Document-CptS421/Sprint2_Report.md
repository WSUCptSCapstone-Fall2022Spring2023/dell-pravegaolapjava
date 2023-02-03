# Sprint 2 Report (10/09/22 - 11/09/2022) 

 

## What's New (User Facing) 

 * Maven project module set up. 

 * Resolved jar dependencies from Pravega Client APIs and Apache Druid APIs. 

* Implemented a dummy Pravega writer and dummy Pravega reader. 

* Added testing documentation. 

* Investigating Pravega schema registry for implementing a generic Pravega reader. 

* Investigating Apache Druid APIs for transferring a dummy event to the druid. 

* A demo of Pravega read and write. 

* Sprint2 report added. 

 

 

 

 

## Work Summary (Developer Facing) 

Provide a one paragraph synopsis of what your team accomplished this sprint. Don't repeat the "What's New" list of features. Instead, help the instructor understand how you went about the work described there, any barriers you overcame, and any significant learnings for your team. 

During sprint two, the team completed lots of research. In sprint one it was stated that the team needed knowledge about the stream processing (read/write) that occurs with Pravega, as well as the API used in order to interact with Pravega from Druid source code. With this research completed, the team was able to produce code to be demonstrated for this sprint cycle. A demo of a Pravega read and write was delivered in the repository that allows the Druid source to connect to Pravega.  

The development environment was also figured out as well. With the advice of the Dell client, the team decided to use the IntelliJ IDE for our Java development, alongside the Maven technology. With this, we are able to develop the project with all the necessary dependencies/libraries.  

 

 

## Unfinished Work 

* Reading a stream of segments from Pravega. The plugin will have two main features: one is to read streams from Pravega, and the other to indexing them to Druid.   

So far, we have been able to read an instance of a single segment using the Pravega sample application. The remaining part is to read the stream of segments, which we are working toward by exploring the Pravega documentations and samples.   

More time is needed for us to explore and analyze Pravega to complete this issue. Therefore, this issue will be added to the subsequent sprint and be addressed as the sprint three progresses.  

* Indexing segments read from Pravega to Druid.   

For indexing Segments to Druid, we have not progressed as much as we anticipated. We are waiting on our clients to arrange a meeting between the team and some who have specialization in Druid, so that we will have more understanding of the Druid inner working. This issue will also be added to the next sprint and be completed as the sprint progresses.  

We want to write some event information gathered from the Pravega API into druid.  

 
 

 
## Completed Issues/User Stories 

Here are links to the issues that we completed in this sprint: 

#41 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/41

Write Introduction – Test Plans 

#42 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/42

Write Testing Strategy - Test Plans 

#43 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/43

Test Plans - Unit Testing - Test Plans 

#44 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/44

Test Plans - Integration Testing – Test Plans 

#45 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/45

Test Plans - System Testing – Test Plans 

#46 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/46

Environment Requirements – Test Plans 

#47 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/47

Explore the Kafka plugin repo. 

#48 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/48

Research on Maven and add pravega client API dependencies – Reasearch 

#49 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/49

Research test driven development and JUnit – Research  

#50 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/50

Research Kafka plugin repo - Research 

#51 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/51

Setup Development environment – intelliIJDEA – Research  

#52 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/52

Research Pravega Client API Blog - Research 

#53 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/53

Setup Development Environment - Research 

#54 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/54

Demo Pravega Standalone - Research 

#55 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/55

Include Forked Druid repo within the dell-pravegaolapjava repo - Research 

#56 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/56

Create a branch for local development - Research 

#57 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/57

Create a branch for local development - Research 

#58  https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/58

Research helloworld reader and writer java files - Research 

#59 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/59

Setup Workspace for Pravega Indexing Service – Code Implementation 

#62 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/62

Look over ReadME (update if needed) - Sprint Report 

#63 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/63

Write "What's New" - Sprint Report 

#65 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/65

Write "Unfinished Work" - Sprint Report 

#67 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/67

Write "Incomplete Issues/User Stories" - Sprint Report 

#68 
Write "Code Files for review" - Sprint Report 

#68 https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/issues/68

 Reminders (Remove this section when you save the file): 

  * Each issue should be assigned to a milestone 

  * Each completed issue should be assigned to a pull request 

  * Each completed pull request should include a link to a "Before and After" video 

  * All team members who contributed to the issue should be assigned to it on GitHub 

  * Each issue should be assigned story points using a label 

  * Story points contribution of each team member should be indicated in a comment 

  

 ## Incomplete Issues/User Stories 

 Here are links to issues we worked on but did not complete in this sprint: 

    N/A 

 

## Code Files for Review 

Please review the following code files, which were actively developed during this sprint, for quality: 

 * [Constant.java](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-index-service/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/Constant.java) 

 * [PravegaWriter.java](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-index-service/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/PravegaWriter.java) 

 * [PravegaReader](https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-index-service/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/data/input/pravegainput/PravegaReader.java) 

* [PravegaRecordEntity(https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/pravega-index-service/extensions-core/pravega-indexing-service/src/main/java/org/apache/druid/data/input/pravega/PravegaRecordEntity.java) 

 

  

## Retrospective Summary 

Here's what went well: 

Asking the Dell clients more directed questions about the next steps. After the completion of the Solution approach section, the team was confused as to what the next steps were to actually begin developing our project. We highlighted what our main blockers were to come up with a list of questions to ask the Dell clients in our weekly meetings. We first asked about the development environment. This led to the client listing a few IDEs that would be good for Java development and also do a lot of heavy lifting when it came to installing dependencies. This allowed us to get our development environment set up and begin development. We also asked about documentation about the Pravega API. This was a crucial question to ask because once provided with the documentation, the team was able to see how to write and read to/from Pravega, which was a great starting point for the development of our project. Lastly, were questions relating to the Kafka repo. This was an important ask because Apache Kafka has a similar plugin that we want to develop (Pravega to Kafka). Essentially, asking the client direct questions relating to the things blocking us went well this sprint.  

Another thing that went well with this sprint was the team’s communication via Discord. We received feedback from Professor AJ that although our team was producing quality work and communicating via Discord, we were not communicating enough. Looking back this was definitely the case. We would have weekly scrum meetings and then send messages regarding upcoming assignments. We would never send updates or ask for help in our server. With this feedback, we feel there has been a positive shift and communication is higher now that it was in sprint 1. We frequently ask each other questions and give updates for the things that we have done or are planning to do. With this, we feel more cohesive as a team and are on track about what is going on and what the next steps are.  

Here's what we'd like to improve: 

The team would like to improve the speed at which we are producing code. We are all busy with other responsibilities such as other classes or jobs, but we would still like to improve the rate that we produce code. A blocker to producing code is due to things that are currently unknown such as the Druid API. This means that we as a team must speed up our research as well if we are aiming to produce more code. 

We would also like to improve the schedule in which we prepare a list of questions to ask the Dell client. Sometimes we find ourselves the day of our client meeting coming up with a list of questions to ask the client. This is not ideal, and it would benefit everyone in the team if we prepared questions earlier to save everyone some stress.  

Here are changes we plan to implement in the next sprint: 

For sprint three the team is looking to provide code for a full end to end process flow from Pravega to Druid. We will likely write some temporary data to Pravega using the API, then read that data into our stream reader. The plan is for the information within this stream reader to undergo transformations in order for it to be written in a compatible form to the Druid indexing tables. This end to end process will be included in the prototype demo shown to Dell at the end of the semester.   
