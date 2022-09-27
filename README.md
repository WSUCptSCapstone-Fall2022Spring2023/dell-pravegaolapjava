# Integration of Pravega and OLAP Database
## Overview
Dell Technologies takes charge of the open-source project Pravega. This is an infrastructure that serves as a storage system that implements data streams tostore/serve data. These data streams are made up of sections which contain events. These are sets of bytes in a stream that represent some sort of data Pravega is effective at storing/ingesting these due to its data streams being consistent, durable, elastic, and append-only.

Pravega stores data in a row-oriented manner - allows for all data points relating to one object to be stored in the same data block. This is beneficial for queries needing to read/manipulate an entire object, but it is slow to analyze large amounts of data. When we want to process events via big data analytics queries, efficiency is poor due to the row-oriented structure of Pravega. A column-oriented processing engine in which columns store similar data points for distinct objects withina block would allow for a quicker analysis of data points, as well as the compression of columns. Without ingesting Pravega events into a proper big data analytics engine, queries against the events are very slow.

## How to use the project
Installation guide: 

Running the project: 
