FROM dmitry404/openjdk-8
LABEL maintainer dmity404@gmail.com

COPY stocks /root/stocks
COPY build/libs/week2-hdfs.jar /root/week2-hdfs.jar
COPY entrypoint.sh /root/entrypoint.sh
COPY run.sh /usr/local/sbin/run.sh

ENTRYPOINT ["/root/entrypoint.sh"]