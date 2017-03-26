#!/bin/bash

echo "Running java -jar week2-hdfs.jar --host hdfs-master-node $@"
java -jar $HOME/week2-hdfs.jar --host hdfs-master-node "$@"
