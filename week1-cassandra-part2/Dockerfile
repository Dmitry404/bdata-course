FROM dmitry404/openjdk-8
LABEL maintainer dmity404@gmail.com

COPY build/libs/numbersLoader.jar numbersLoader.jar
COPY entrypoint.sh entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]