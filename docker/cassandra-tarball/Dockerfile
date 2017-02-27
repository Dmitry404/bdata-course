# Cassandra 3.10 image for BigData course
# tarball installation
FROM dmitry404/oracle-java8
LABEL maintainer dmity404@gmail.com

RUN wget http://apache.cp.if.ua/cassandra/3.10/apache-cassandra-3.10-bin.tar.gz \
	&& tar xzf apache-cassandra-3.10-bin.tar.gz \
	&& rm -f apache-cassandra-3.10-bin.tar.gz \
	&& cp -r apache-cassandra-3.10 /usr/lib/cassandra-3.10 \
	&& rm -rf apache-cassandra-3.10

ENV CASSANDRA_DIR=/usr/lib/cassandra-3.10
ENV PATH "$PATH:$CASSANDRA_DIR/bin"

EXPOSE 9042

ADD entrypoint.sh /usr/lib/cassandra-3.10/bin/entrypoint.sh

ENTRYPOINT ["entrypoint.sh"]
