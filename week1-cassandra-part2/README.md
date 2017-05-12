#### Build the app with Gradle

```terminal
$ gradlew build
```

#### Run the cluster with docker-compose

```terminal
$ docker-compose up
```

It will create a Cassandra cluster of three nodes. After the cluster starts, an additional container with a Java app 
will start generating data pushing it to the cluster. 

< Note that the app will be generating data continuously, so be sure you have enough free disk space available
