#### Build the app with Gradle

```terminal
$ gradlew build
```

#### Run the cluster with docker-compose

```terminal
$ docker-compose up
```

#### Invoke MapReduce Job with one of the following ways

Any of these two will process the content of the `shared/input.dat` and output the results on the screen. 

In fact, the run.sh script uploads the input file to HDFS, invokes `yarn jar` and saves the output to HDFS.
If MapReduce job finished successfully, the script will read the data and prints on the screen.

If you run it like this, you'll get the results as JSON strings

```terminal
$ docker exec hadoop-master-node /root/app/run.sh JsonPaymentsJob
```

Or, if you run it in this wat, you'll get the results as CVS strings

```terminal
$ docker exec hadoop-master-node /root/app/run.sh CsvPaymentsJob
```

----
The output will look similar to this

```
Payments Input:
2016-07-02 20:52:39 1 12.01 www.store1.com
2016-07-02 20:52:39 1123 1.75 www.store1.com
2016-07-02 20:52:39 12 4.05 www.store2.com
2016-07-02 20:52:39 1 7.87 www.store1.com
2016-07-02 20:52:40 12 124.67 www.store2.com
2016-07-02 20:52:40 1 9.14 www.store3.com
2016-07-02 20:52:40 1123 14.75 www.store1.com
2016-07-02 20:52:40 12 54.95 www.store2.com
2016-07-02 20:52:40 1 77.70 www.store3.com
2016-07-02 20:52:40 12 1.99 www.store4.com
Payments Output:
{"id":1,"total":106.72,"stores":["www.store1.com","www.store3.com"]}
{"id":12,"total":185.66,"stores":["www.store2.com","www.store4.com"]}
{"id":1123,"total":16.50,"stores":["www.store1.com"]}
```
