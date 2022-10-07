# Project Name
Pravega OLAP Integration 

## Project summary
Dell Technologies takes charge of the open-source project Pravega. This is an infrastructure that serves as a storage system that implements data streams tostore/serve data. These data streams are made up of sections which contain events. These are sets of bytes in a stream that represent some sort of data Pravega is effective at storing/ingesting these due to its data streams being consistent, durable, elastic, and append-only.

Pravega stores data in a row-oriented manner - allows for all data points relating to one object to be stored in the same data block. This is beneficial for queries needing to read/manipulate an entire object, but it is slow to analyze large amounts of data. When we want to process events via big data analytics queries, efficiency is poor due to the row-oriented structure of Pravega. A column-oriented processing engine in which columns store similar data points for distinct objects withina block would allow for a quicker analysis of data points, as well as the compression of columns. Without ingesting Pravega events into a proper big data analytics engine, queries against the events are very slow.

### One-sentence description of the project
Utilization of Java to create a plugin to enable the automatic ingestion of Pravega's data streams to be processed by Apache Druid. 

TODO: 

Pravega's data streams need to be read and transformed from a row oriented manner in order to be written into Apache Druid in a column oriented manner. 

## Technologies
Pravega

OLAP Database (Apache Druid) 

Java

SQL

### Additional information about the project

TODO: Write a compelling/creative/informative project description / summary

## Installation

### Prerequisites

TODO: List what a user needs to have installed before running the installation instructions below (e.g., git, which versions of Ruby/Rails)

### Add-ons

TODO: List which add-ons are included in the project, and the purpose each add-on serves in your app.

### Installation Steps

TODO: Describe the installation process (making sure you mention `bundle install`).
Instructions need to be such that a user can just copy/paste the commands to get things set up and running. 


## Functionality

TODO: Write usage instructions. Structuring it as a walkthrough can help structure this section,
and showcase your features.


## Known Problems

TODO: Describe any known issues, bugs, odd behaviors or code smells. 
Provide steps to reproduce the problem and/or name a file or a function where the problem lives.


## Contributing

TODO: Leave the steps below if you want others to contribute to your project.

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## Additional Documentation

TODO: Provide links to additional documentation that may exist in the repo, e.g.,
  * Sprint reports
  * User links

## License

If you haven't already, add a file called `LICENSE.txt` with the text of the appropriate license.
We recommend using the MIT license: <https://choosealicense.com/licenses/mit/>
