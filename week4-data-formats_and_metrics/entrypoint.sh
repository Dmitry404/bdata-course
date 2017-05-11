#!/bin/bash

sleep 40s

cd /root
echo "Running java -jar week4-data-formats_and_metrics.jar $@"
java -jar $HOME/week4-data-formats_and_metrics.jar "$@"

while true; do sleep 1000; done