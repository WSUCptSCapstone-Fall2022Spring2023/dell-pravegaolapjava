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

Pravega is a new storage a new abstraction – a stream for continuously generated and unbounded data. In comparison to a distributed messaging system such as Kafka and Pulsar, Pravega provides a multitude of futures that are useful for modern-day data-intensive applications. While Kafka and Pulsar support transactions, long-term retention, and event stream they luck the necessary futures like durable by default, auto-scaling, ingestion of large data, and many other futures. Pravega offers all the essential futures. However, Pravega is not an analytic engine hence it cannot process the data it ingests. Our plugin will integrate Pravega with Druid and enable the automatic ingestion of data streams into an OLAP database, such that a user can perform log-based analytics against the events in their streams. 

## Installation

### Prerequisites
- **Prevega Distributed Mode Prerequisites** 
  - HDFS 
    - Setup an HDFS storage cluster running HDFS version 2.7+. HDFS is used as Tier 2 storage and must have sufficient capacity to store contents of all streams. The storage cluster is recommended to be run alongside Pravega on separate nodes. 
  - Java 
    - Install the latest Java 8 from java.oracle.com. Packages are available for all major operating systems. 
  - Zookeeper 
    - Pravega requires Zookeeper 3.5.1-alpha+. At least 3 Zookeeper nodes are recommended for a quorum. No special configuration is required for Zookeeper but it is recommended to use a dedicated cluster for Pravega. 
    - This specific version of Zookeeper can be downloaded from Apache at zookeeper-3.5.1-alpha.tar.gz. 
    - For installing Zookeeper see the Getting Started Guide. 
  - Bookkeeper 
    - Pravega requires Bookkeeper 4.4.0+. At least 3 Bookkeeper servers are recommended for a quorum. 
    - This specific version of Bookkeeper can be downloaded from Apache at bookkeeper-server-4.4.0-bin.tar.gz.   
    - For installing Bookkeeper see the Getting Started Guide. Some specific Pravega instructions are shown below. All sets assuming being run from the bookkeeper-server-4.4.0 directory. 
  ** Bookkeeper Configuration** 
  The following configuration options should be changed in the `conf/bk_server.conf file`. 
  ``` 
  # Comma separated list of <zp-ip>:<port> for all ZK servers 
  zkServers=localhost:2181 

  # Alternatively specify a different path to the storage for /bk 
  journalDirectory=/bk/journal 
  ledgerDirectories=/bk/ledgers 
  indexDirectories=/bk/index 

  zkLedgersRootPath=/pravega/bookkeeper/ledgers 
  ``` 
  - Initializing Zookeeper paths 
    - The following paths need to be created in Zookeeper. From the `zookeeper-3.5.1-alpha` directory on the Zookeeper servers run: 
    - ` bin/zkCli.sh -server $ZK_URL create /pravega` 
    - ` bin/zkCli.sh -server $ZK_URL create /pravega/bookkeeper` 
  - Running Bookkeeper 
    - `bin/bookkeeper shell metaformat –nonInteractive` 
    - ` bin/bookkeeper bookie` for start the bookie 
 
- **Prevega Standalone Mode Prerequisites (Testing & Demo Purpose)** 
  - Java 8 or later for client-only applications 
  - Java 11 or later for standalone demo and server-side applications 
 
- **Apache Druid Standalone Mode Prerequisites** 
  - Linux, Mac OS X, or other Unix-like OS. (Windows is not supported.) 
  - Java 8u92+ or Java 11. 

### Add-ons

No Add-ons yet that we know of.

### Installation Steps

- **Installation of Pravega** 
  - Download from here: https://github.com/pravega/pravega/releases 
  - `tar <name>.tgz` 
  - `cd bin` 
  - `./pravega-standalone` 
 
- **Installation of Apache Druid** 
  - Download from here: https://www.apache.org/dyn/closer.cgi?path=/druid/24.0.0/apache-druid-24.0.0-bin.tar.gz 
  - ` tar -xzf apache-druid-24.0.0-bin.tar.gz` 
  - ` cd apache-druid-24.0.0` 


## Functionality

After successfully installing Pravega and Druid.  Users should start up Druid services using the micro-quickstart single-machine configuration.    

Once Druid services finish startup user should lunch the Druid web console at http://localhost:8888.  

Ater Druid’s web console successfully lunched user should navigate to the load page and select Pravega.  In the Pravega plugin user users can pass the desired specification and submit.   

## Known Problems
Currently there isn’t any known problem.  

## Contributing

- **Non-Collaborator Contribution** 
    1. Fork it!
    2. Create your feature branch: `git checkout -b my-new-feature`
    3. Commit your changes: `git commit -am 'Add some feature'`
    4. Push to the branch: `git push origin my-new-feature`
    5. Submit a pull request :D

- **Collaborator Contribution** 
    1. `git pull` at master branch 
    2. `git checkout –b my-new-feature` 
    3. `git add some new features` 
    4. `git push origin my-new-feature` 
    5. Submit a pull request 

## Additional Documentation

Pravega readings:  

https://cncf.pravega.io/docs/nightly/pravega-concepts/#introduction 

https://cncf.pravega.io/docs/v0.11.0/ 

https://cncf.pravega.io/ 

Apache Druid readings:  

https://druid.apache.org/druid 

https://druid.apache.org/docs/latest/design/index.html 

https://druid.apache.org/use-cases 

## License

License URL: https://github.com/WSUCptSCapstone-Fall2022Spring2023/dell-pravegaolapjava/blob/master/Document/License.txt
