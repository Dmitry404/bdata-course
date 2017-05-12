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

> You has to wait till the cluster starts before opening the web-app

---

Here is a screenshot of how the web-app looks like

![web-app](https://cloud.githubusercontent.com/assets/188851/25997783/691b68fe-3726-11e7-9802-15780fe34eaf.png)

