#!/bin/bash

echo "Configuring Cassandra..."

CASSANDRA_CONFIG=$CASSANDRA_DIR/conf
HOSTNAME_ADDR="$(hostname --ip-address)"

if [ "$1" = "--seed" ] && [ -n "$2" ]; then
    SEED_ADDR=$2
else
    SEED_ADDR=$HOSTNAME_ADDR
fi

sed -ri 's/('listen_address':).*/\1 '"$HOSTNAME_ADDR"'/' "$CASSANDRA_CONFIG/cassandra.yaml"
sed -ri 's/# ('broadcast_address':)(.*)/\1 '"$HOSTNAME_ADDR"'/' "$CASSANDRA_CONFIG/cassandra.yaml"
sed -ri 's/# ('broadcast_rpc_address':)(.*)/\1 '"$HOSTNAME_ADDR"'/' "$CASSANDRA_CONFIG/cassandra.yaml"

sed -ri 's/(- seeds:).*/\1 "'"$SEED_ADDR"'"/' "$CASSANDRA_CONFIG/cassandra.yaml"

sed -ri 's/^(# )?('rpc_address':).*/\2 '0.0.0.0'/' "$CASSANDRA_CONFIG/cassandra.yaml"

echo "Starting Cassandra..."
# I promise not to run it as root in production 
cassandra -Rf