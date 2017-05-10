#!/bin/bash

cd /root/app

echo "Trying to cleanup hdfs:/output directory"
hdfs dfs -rm -r /output
echo "Copying input to hdfs:/input.dat"
hdfs dfs -put -f input.dat /input.dat

echo "Running yarn jar week3-hdfs.jar $@"
yarn jar week3-yarn.jar "$@"

if [ $? -eq 0 ]
then
  echo "Payments Input (truncated if the total number of lines is big):"
  hdfs dfs -text /input.dat | head -n 30

  echo ""
  echo "Payments Output:"
  hdfs dfs -text /output/part-r-00000
fi
