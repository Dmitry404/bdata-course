#!/bin/bash

sleep 40s

cd /root
echo "Running java -jar week2-hdfs.jar $@"
java -jar $HOME/week2-hdfs.jar "$@"

while true; do sleep 1000; done