#!/bin/bash

echo "Running java -jar week4-data-formats_and_metrics.jar --host hdfs-master-node $@"
java -jar $HOME/week4-data-formats_and_metrics.jar --host hdfs-master-node "$@"
