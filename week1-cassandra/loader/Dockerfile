# DB loader app - week1
FROM dmitry404/oracle-java8
LABEL maintainer dmity404@gmail.com

COPY build/libs/loader.jar loader.jar
COPY tweets.csv tweets.csv
COPY miniTweets.csv miniTweets.csv
COPY entrypoint.sh entrypoint.sh

RUN chmod +x entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]