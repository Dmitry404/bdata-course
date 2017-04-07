#!/bin/bash

cd /root/app

echo "Trying to cleanup hdfs:/output directory"
hdfs dfs -rm -r /output
echo "Copying input to hdfs:/input.dat"
hdfs dfs -put input.dat /input.dat

echo "Running yarn jar week3-hdfs.jar $@"
yarn jar week3-yarn.jar "$@"

if [ $? -eq 0 ]
then
  echo "Payments Input:"
  hdfs dfs -text /input.dat

  echo ""
  echo "Payments Output:"
  hdfs dfs -text /output/part-r-00000
fi