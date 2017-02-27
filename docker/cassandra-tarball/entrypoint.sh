#!/bin/bash

echo "\nConfiguring Cassandra...\n"

HOSTNAME_ADDR="$(hostname --ip-address)"
CASSANDRA_CONFIG=$1/conf

sed -ri 's/('listen_address':).*/\1 '"$HOSTNAME_ADDR"'/' "$CASSANDRA_CONFIG/cassandra.yaml"
sed -ri 's/# ('broadcast_address':)(.*)/\1 '"$HOSTNAME_ADDR"'/' "$CASSANDRA_CONFIG/cassandra.yaml"
sed -ri 's/# ('broadcast_rpc_address':)(.*)/\1 '"$HOSTNAME_ADDR"'/' "$CASSANDRA_CONFIG/cassandra.yaml"
sed -ri 's/(- seeds:).*/\1 "'"$HOSTNAME_ADDR"'"/' "$CASSANDRA_CONFIG/cassandra.yaml"

sed -ri 's/^(# )?('rpc_address':).*/\2 '0.0.0.0'/' "$CASSANDRA_CONFIG/cassandra.yaml"

echo "\nStarting Cassandra...\n"
# I promise not to run it as root in production 
cassandra -Rf