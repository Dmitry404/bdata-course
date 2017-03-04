import com.datastax.driver.core.Cluster;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import week1.cassandra.loader.DbLoader;
import week1.cassandra.loader.TweetsSupplier;

public class Main {
  public static void main(String[] args) throws FileNotFoundException {
    //todo add acceptance tests
    // - a smoke test for cassandra cluster
    // - a smoke test for the main

    InputStream fileInputStream = new FileInputStream("tweets.csv");

    Cluster cluster = Cluster.builder()
        .addContactPoint("127.0.0.1")
        .build();

    DbLoader loader = new DbLoader(cluster.newSession(), new TweetsSupplier(fileInputStream));
    loader.populateDb();
  }
}
