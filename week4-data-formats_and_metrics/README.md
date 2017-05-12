#### Build the app with Gradle

```terminal
$ gradlew build
```

#### Run the cluster with docker-compose

```terminal
$ docker-compose up
```

It will create a HDFS cluster of three containers (master and two slaves). After the cluster starts, an additional container with a Java app 
will generate some amount of orders applicable to a fruit store. Each order will be converted to Avro format and written to HDFS. In addition, there is a Parquet converter which uses Avro schemas in order to write the same Fruit Store data to HDFS.

##### Graphite / codahale.metrics
Some metrics, like order totals and amount of fruits sold, grouped by its name, are reported to Graphite server which you can access at `http://localhost`

##### You can list the written data running 

```terminal
$ docker exec java-app-node run.sh --read_avro
```

or

```terminal
$ docker exec java-app-node run.sh --read_parquet
```

As the result you'll get the generated Fruit Store orders data in JSON format. Note, that it's all done for demonstration purposes only, generaly there is no need in run the two commands above. Make sure you pipe the output to `less` because its size will take more than a few screens.

##### Grafana

Grafana server is also will be run, however it will require additional setup as I used the official Docker container `grafana/grafana`

Its UI is available at `http://localhost:3000`
