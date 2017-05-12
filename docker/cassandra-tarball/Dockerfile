# Cassandra 3.10 image for BigData course
# tarball installation
FROM dmitry404/openjdk-8
LABEL maintainer dmitry404@gmail.com

RUN wget http://mirror.yannic-bonenberger.com/apache/cassandra/3.9/apache-cassandra-3.9-bin.tar.gz \
	&& tar xzf apache-cassandra-3.9-bin.tar.gz \
	&& rm -f apache-cassandra-3.9-bin.tar.gz \
	&& cp -r apache-cassandra-3.9 /usr/lib/cassandra \
	&& rm -rf apache-cassandra-3.9

RUN wget http://central.maven.org/maven2/com/stratio/cassandra/cassandra-lucene-index-plugin/3.9.0-RELEASE/cassandra-lucene-index-plugin-3.9.0-RELEASE.jar \
	&& mv cassandra-lucene-index-plugin-3.9.0-RELEASE.jar /usr/lib/cassandra/lib

# cqlsh requires python 2.7
RUN apt-get update && apt-get install -y --no-install-recommends python2.7 && rm -rf /var/lib/apt/lists/*

ENV CASSANDRA_DIR=/usr/lib/cassandra
ENV PATH "$PATH:$CASSANDRA_DIR/bin"

EXPOSE 9042

ADD entrypoint.sh /usr/lib/cassandra/bin/entrypoint.sh

ENTRYPOINT ["entrypoint.sh"]
