#### Build the app with Gradle

```terminal
$ gradlew build
```

#### Run the cluster with docker-compose

```terminal
$ docker-compose up
```

It will create a Cassandra cluster of two containers. After the cluster starts, an additional container with a small Java app 
will upload some data collected from Twitter to Cassandra. Additionally a web-app written on Spring-boot will be started. The web-app is available at `http://localhost:8080`

