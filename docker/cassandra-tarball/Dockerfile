# Cassandra 3.10 image for BigData course
# tarball installation
FROM dmitry404/oracle-java8
MAINTAINER dmity404@gmail.com

RUN wget http://apache.cp.if.ua/cassandra/3.10/apache-cassandra-3.10-bin.tar.gz \
	&& tar xzf apache-cassandra-3.10-bin.tar.gz \
	&& rm -f apache-cassandra-3.10-bin.tar.gz \
	&& cp -r apache-cassandra-3.10 /usr/lib/cassandra-3.10 \
	&& rm -rf apache-cassandra-3.10

ENV CASSANDRA_DIR=/usr/lib/cassandra-3.10
ENV CASSANDRA_CONFIG=/usr/lib/cassandra-3.10/conf 
ENV CASSANDRA_BIN=/usr/lib/cassandra-3.10/bin 

ENV PATH "$PATH:$CASSANDRA_BIN"

ENV HOSTNAME_ADDR="$(hostname --ip-address)"

RUN sed -ri 's/^(# )?('listen_address':).*/\2 '172.17.0.2'/' "$CASSANDRA_CONFIG/cassandra.yaml"
RUN sed -ri 's/# ('broadcast_address':)(.*)/\1 '172.17.0.2'/' "$CASSANDRA_CONFIG/cassandra.yaml"
RUN sed -ri 's/# ('broadcast_rpc_address':)(.*)/\1 '172.17.0.2'/' "$CASSANDRA_CONFIG/cassandra.yaml"
RUN sed -ri 's/(- seeds:).*/\1 "'172.17.0.2'"/' "$CASSANDRA_CONFIG/cassandra.yaml"

RUN sed -ri 's/^(# )?('rpc_address':).*/\2 '0.0.0.0'/' "$CASSANDRA_CONFIG/cassandra.yaml"

EXPOSE 9042

ADD run.sh /usr/lib/cassandra-3.10/bin/start_cassandra

CMD start_cassandra
