# web app - week1
FROM dmitry404/oracle-java8
LABEL maintainer dmity404@gmail.com

COPY build/libs/webapp.jar webapp.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "webapp.jar"]