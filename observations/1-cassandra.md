Is Cassandra a column-oriented storage, or not? Assuming the data model below (rows > column1, column2), I'm not sure. However, I can say for sure that Cassandra is a key-value storage.

----
*Cassandra data model*

Rows of Columns/Value. The most important thing is the primary key. It defines location of the data using its hash (there are different partitioning strategies exist)

_Structure_
Row Key -> Column1, Column1, .. ,ColumnN 

Each column contains at least:
  - Value
  - Timestamp (value insert timestamp, writteime)
  - TTL (timeout of data removal)
  - Timestomb (for data removal)

----
*What was tested*

_Replication_
  - with a simulation of node's crash and restore. 
  - node assasination via nodetool.
_Compaction_
  - different strategies (not much differences observed on the configuration I ran)
  - disabled compaction (data size grows on all nodes)
  - manual compaction via nodetool (compacts only one node, the one you run compaction on)
_Nodetool commands tested_
  - status, tablestats, compactionhistory, compactionstats, getcompactionthroughput, getcompactionthreshold, getendpoints, getsstables
_Load (endless stream of events of ±1k size with random PK withing 1..10_000_000)_
  - with lots of reads/writes
  - with only writes
    - not much difference because I didn't find a way to measure it

----
*Configuration*
  - Helpful link https://www.ecyrd.com/cassandracalculator
_replication factor_
  - says how many copies will be stored in cluster
  - set when creating a keyspace
_consistency level (read/write)_ 
  - https://docs.datastax.com/en/cassandra/2.1/cassandra/dml/dml_config_consistency_c.html
_eventual consistency_
  - means that the data will be eventually consistent, but not guaranteed that in this particular read

_coordinator_
  - a node which receives read/write request from an app
_cassandra ring_
  - cluster consists from nodes, each of which contains part of the hash values
  - nodes 
_gossip_
  - protocol which is used by nodes to join a new node to cluster
  - "seed" parameter is passed onto a node, which will send "gossip" to the given host/ip

----
*Java*
- The driver I used https://github.com/datastax/java-driver
- Lucene index https://github.com/Stratio/cassandra-lucene-index

- You can connect to any node (this node will be a coordinator) in cluster on port 9042
    - make sure that rpc_address, in cassandra.yml, is set to 0.0.0.0 when you run it in Docker and want to connect from the host
- All data you write or read will be partitioned accordingly

- prepared statements https://docs.datastax.com/en/developer/java-driver/3.1/manual/statements/prepared/
- object mappings https://docs.datastax.com/en/developer/java-driver/3.1/manual/object_mapper/

----

*Cassandra terms*

_node_ 
  - each node is responsible for a part of data in DB
_partition_
  - a record
_partition key_
  - primary key in CQL table or a key in "hash table"
_replica_
  - copy of a partition on another node

_keyspace_
  - kind of a "schema" in RDBMS
_column family_
  - kind of a "table" in RDBMS

_write/read data_
  - commit log
  - memtable
  - sstable

_bloomfilter_
  - a simple example https://llimllib.github.io/bloomfilter-tutorial/
_tombstone_
  - a mark in sstable which means the partition can be removed during compaction
_ttl_
  - expiration time for a column which indicates data expiration in seconds (will set a tombstone?)
  - this is optional property

_compaction_
  - there are three strategies
_compression_
  - didn't test it much. However when I added more than 8M records the total size was ±1.5Gb, meaning there is a compression for sure

_to make sure data is consistent_
  - consistency levels http://docs.datastax.com/en/archived/cassandra/1.2/cassandra/dml/dml_config_consistency_c.html
  - hinted handoff
  - anti-entropy node repair
  - read-repair 

----
*CQL*
  - table
  - row key
  - composite keys
  - index
    - I needed to filter some data (by the number of record updates)
    - For this purpose I created an index on this column
    - This led to another "sstable(s)" creation (also saw this data participating in compaction cycles)

- batching
  - data locality matters https://docs.datastax.com/en/cql/3.3/cql/cql_using/useBatchBadExample.html
  - if we know the algorithm (hashing?) of assigning to a particular node(vnode?), we can assemble a batch of records which will go to one node
  - example of using batch https://docs.datastax.com/en/cql/3.3/cql/cql_using/useBatchGoodExample.html