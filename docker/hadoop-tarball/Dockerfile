# Hadoop image
FROM dmitry404/openjdk-8
LABEL maintainer dmity404@gmail.com

RUN wget http://apache.lauf-forum.at/hadoop/common/hadoop-2.7.3/hadoop-2.7.3.tar.gz \
  && tar -xzf hadoop-2.7.3.tar.gz \
  && rm -f hadoop-2.7.3.tar.gz \
  && mv hadoop-2.7.3 /usr/lib/hadoop

ENV HADOOP_DIR=/usr/lib/hadoop
ENV PATH "$PATH:$HADOOP_DIR/bin"

RUN echo "export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64" >> $HADOOP_DIR/etc/hadoop/hadoop-env.sh
RUN hdfs namenode -format

RUN apt-get update && apt-get install -y --no-install-recommends openssh-server && rm -rf /var/lib/apt/lists/*
RUN sed -ri 's/^#.* (StrictHostKeyChecking).*/\1 no/' /etc/ssh/ssh_config \
  && ssh-keygen -t rsa -P "" -f $HOME/.ssh/id_rsa \
  && cat $HOME/.ssh/id_rsa.pub >> $HOME/.ssh/authorized_keys

COPY conf $HADOOP_DIR/etc/hadoop

COPY entrypoint.sh $HADOOP_DIR/bin/entrypoint.sh

ENTRYPOINT ["entrypoint.sh"]

EXPOSE 50070 8042 8088