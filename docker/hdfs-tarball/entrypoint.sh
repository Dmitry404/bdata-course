#!/bin/bash

function set_master_host {
  echo "Writing master host $1 to $HADOOP_DIR/etc/hadoop/core-site.xml"
  sed -i "s/localhost/${1}/" $HADOOP_DIR/etc/hadoop/core-site.xml
}

service ssh start

if [ "$1" = "--run-as-master-with-slaves" ]; then
   for slave in "${@:2}"; do
    echo $slave >> $HADOOP_DIR/etc/hadoop/slaves
   done
   #set_master_host "$(hostname --ip-address)" # ip-addr
   set_master_host "$(hostname)"
   $HADOOP_DIR/sbin/start-dfs.sh
else
  # ignore --run-as-slave-with-master param as it's there for informational purpose only
  #set_master_host "$(getent hosts $2 | awk '{ print $1 }')" # ip-addr
  set_master_host $2 
fi

while true; do sleep 1000; done