Cassandra is a column-oriented storage, or not? Assuming the data model below (rows > column1, column2), I'm not sure. However, I can say for sure that Cassandra is a key-value storage.

----
Cassandra data model

Rows of Columns/Value. The most important thing is the primary key. It defines location of the data using its hash (there are different partitioning strategies exist)

Structure:
Row Key -> Column1, Column1, .. ,ColumnN 

Each column contains at least:
  - Value
  - Timestamp (value insert timestamp, writteime?)
  - TTL (for a sake of some compaction strategies? or for all?)
  - Timestomb (for data removal)

----
What was tested

Replication
  - with a simulation of node's crash and restore. 
  - node assasination via nodetool.
Compaction 
  - different strategies (not much differences observed on the configuration I ran)
  - disabled compaction (data size grows on all nodes)
  - manual compaction via nodetool (compacts only one node, the one you run compaction on)
Nodetool commands tested
  - status, tablestats, compactionhistory, compactionstats, getcompactionthroughput, getcompactionthreshold, getendpoints, getsstables
Load (endless stream of events of ±1k size with random PK withing 1..10_000_000)
  - with lots of reads/writes
    - writes are relatively "cheap" 
    - reads 
  - with only writes
    - not much difference because I didn't find a way to measure it

----
Configuration
  - Helpful link https://www.ecyrd.com/cassandracalculator
replication factor
  - says how many copies will be stored in cluster
  - set when creating a keyspace
consistency level (read/write) - https://docs.datastax.com/en/cassandra/2.1/cassandra/dml/dml_config_consistency_c.html
eventual consistency

coordinator
partitioner
cassandra ring
  - cluster consists from nodes, each of which contains part of the hash values
  - nodes 
gossip
  - protocol which is used by nodes to join a new node to cluster
  - "seed" parameter is passed onto a node, which will send "gossip" to the given host/ip

----
Java
- The driver I used https://github.com/datastax/java-driver
- Lucene index https://github.com/Stratio/cassandra-lucene-index

- You can connect to any node (this node will be a coordinator) in cluster on port 9042
    - make sure that rpc_address, in cassandra.yml, is set to 0.0.0.0 when you run it in Docker and want to connect from the host
- All data you write or read will be partitioned accordingly

- prepared statements https://docs.datastax.com/en/developer/java-driver/3.1/manual/statements/prepared/
- object mappings https://docs.datastax.com/en/developer/java-driver/3.1/manual/object_mapper/

----

Cassandra terms

node 
  - each node is responsible for a part of data in DB
partition
  - a record
partition key
  - primary key in a "hash table"
replica
  - copy of a partition

keyspace
  - kind of a "schema" in RDBMS
column family
  - kind of a "table" in RDBMS

_write/read data_
> - commit log
> - memtable
> - sstable

bloomfilter
  - a simple example https://llimllib.github.io/bloomfilter-tutorial/
tombstone
  - a mark in sstable which means the partition can be removed during compaction
ttl
  - expiration time for a column which indicates data expiration in seconds (will set a tombstone?)
  - this is optional property

compaction
  - there are three strategies
compression
  - ...

_to make sure data is consistent_
> - consistency levels http://docs.datastax.com/en/archived/cassandra/1.2/cassandra/dml/dml_config_consistency_c.html
> - hinted handoff
> - anti-entropy node repair
> - read-repair 

----
CQL
table
row key
composite keys
index
  - I needed to filter some data (by the number of record updates)
  - For this purpose I created an index on this column
    - This led to another "sstable(s)" creation (also saw this data participating in compaction cycles)

batching
  - data locality matters https://docs.datastax.com/en/cql/3.3/cql/cql_using/useBatchBadExample.html
  - if we know the algorithm (hashing?) of assigning to a particular node(vnode?), we can assemble a batch of records which will go to one node
  - example of using batch https://docs.datastax.com/en/cql/3.3/cql/cql_using/useBatchGoodExample.html