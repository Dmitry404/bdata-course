#!/bin/bash

function set_master_host {
  echo "Writing master host $1 to $HADOOP_DIR/etc/hadoop/core-site.xml"
  sed -i "s/localhost/${1}/" $HADOOP_DIR/etc/hadoop/core-site.xml
  sed -i "s/localhost/${1}/" $HADOOP_DIR/etc/hadoop/yarn-site.xml
}

service ssh start

if [ "$1" = "--run-as-master-with-slaves" ]; then
   for slave in "${@:2}"; do
    echo $slave >> $HADOOP_DIR/etc/hadoop/slaves
   done
   set_master_host "$(hostname)"

   $HADOOP_DIR/sbin/start-dfs.sh
   $HADOOP_DIR/sbin/start-yarn.sh
else
  # ignore --run-as-slave-with-master param as it's there for informational purpose only
  set_master_host $2 $HADOOP_DIR/etc/hadoop/core-site.xml
fi

while true; do sleep 1000; done