#!/bin/bash

# fallback options
HOST="127.0.0.1"
LOAD="tweets.csv"
ITER=1
START_DELAY=0

while [ "$1" != "" ]; do
    case $1 in
        --host)
            HOST=$2
            ;;
        --iter)
            ITER=$2
            ;;
        --load)
            LOAD=$2
            ;;
        --start-delay)
            START_DELAY=$2
            ;;
        *)
            echo "ERROR: unknown parameter \"$1\""
            exit 1
            ;;
    esac
    shift
    shift
done

# A workaround. Need to wait for cassandra to start
sleep "$START_DELAY"

echo "Running java -jar loader.jar --host $HOST --iter $ITER --load $LOAD"
java -jar loader.jar --host "$HOST" --iter "$ITER" --load "$LOAD"