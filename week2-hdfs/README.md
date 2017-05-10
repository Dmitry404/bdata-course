#### Build the app with Gradle

```terminal
$ gradlew build
```

#### Run the cluster with docker-compose

```terminal
$ docker-compose up
```

It will create a HDFS cluster of three containers. After the cluster starts, an additional container with a small Java app 
will upload there the historical data, taken from finance.yahoo.com, of the stocks of some famous Internet companies, e.g. Apple, Google.

##### You can list available companies running the app in this way:

```terminal
$ docker exec hdfs-java-app-node run.sh --list
```

You will get the similar output to the below

```
2017:
 - AAPL
 - FB
 - GOOG
 - MSFT
 - RHT
2016:
 - AAPL
 - FB
 - GOOG
 - MSFT
 - RHT
 ```
 
 ##### Now, you can see how much Apple's stocks cost in 1980 running this command
 
 ```terminal
 $ docker exec hdfs-java-app-node run.sh --read AAPL 1980
 ```
 
 The output will be similar to this
 
 ```
   #Date   |  #Open   |  #High   |   #Low   |  #Close
1980-12-31| 34.250000| 34.250000| 34.125000| 34.125000
1980-12-30| 35.250000| 35.250000| 35.125000| 35.125000
1980-12-29| 36.000000| 36.125000| 36.000000| 36.000000
1980-12-26| 35.500000| 35.625000| 35.500000| 35.500000
1980-12-24| 32.500000| 32.625000| 32.500000| 32.500000
1980-12-23| 30.874998| 30.999998| 30.874998| 30.874998
1980-12-22| 29.625000| 29.750000| 29.625000| 29.625000
1980-12-19| 28.249998| 28.374998| 28.249998| 28.249998
1980-12-18| 26.625000| 26.750000| 26.625000| 26.625000
1980-12-17| 25.875000| 25.999998| 25.875000| 25.875000
1980-12-16| 25.375000| 25.375000| 25.250000| 25.250000
1980-12-15| 27.375002| 27.375002| 27.250000| 27.250000
1980-12-12| 28.750000| 28.875000| 28.750000| 28.750000
```
 
 
 
