FROM dmitry404/openjdk-8
LABEL maintainer dmity404@gmail.com

COPY build/libs/week4-data-formats_and_metrics.jar /root/week4-data-formats_and_metrics.jar
COPY entrypoint.sh /root/entrypoint.sh
COPY run.sh /usr/local/sbin/run.sh

ENTRYPOINT ["/root/entrypoint.sh"]