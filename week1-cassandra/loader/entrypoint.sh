#!/bin/bash

# fallback options
HOST="127.0.0.1"
LOAD="tweets.csv"
ITER=1

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
        *)
            echo "ERROR: unknown parameter \"$1\""
            exit 1
            ;;
    esac
    shift
    shift
done

echo "Running java -jar loader.jar --host $HOST --iter $ITER --load $LOAD"
java -jar loader.jar --host "$HOST" --iter "$ITER" --load "$LOAD"