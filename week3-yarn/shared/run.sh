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
  echo "Payments Input (truncated to the first 20 lines if the total size is more than that):"
  hdfs dfs -text /input.dat | head -n 20

  echo ""
  echo "Payments Output:"
  hdfs dfs -text /output/part-r-00000
fi
