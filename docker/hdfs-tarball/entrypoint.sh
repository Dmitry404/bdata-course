#!/bin/bash

service ssh start

if [ "$1" = "--run-as-master-with-slaves" ]; then
   for slave in "${@:2}"; do
    echo $slave >> $HADOOP_DIR/etc/hadoop/slaves
   done
   $HADOOP_DIR/sbin/start-dfs.sh
else
  MASTER_HOSTNAME=$1
  sed -i "s/localhost/${MASTER_HOSTNAME}/" $HADOOP_DIR/etc/hadoop/core-site.xml
fi

while true; do sleep 1000; done