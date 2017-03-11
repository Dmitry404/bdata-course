import com.datastax.driver.core.Cluster;

import java.io.FileInputStream;
import java.io.InputStream;

import arguments.ArgumentsParser;
import loader.DbLoader;
import loader.TweetsSupplier;

public class EntryPoint {
  public static void main(String... args) throws Exception {
    ArgumentsParser arguments = new ArgumentsParser(args);
    arguments.addDefault("host", "127.0.0.1");
    arguments.addDefault("iter", "1");
    arguments.addDefault("replication-factor", "1");

    System.out.println(arguments);
    
    Cluster cluster = Cluster.builder()
        .addContactPoint(arguments.get("host"))
        .build();

    try (InputStream fileInputStream = new FileInputStream(arguments.get("load"))) {
      DbLoader loader = new DbLoader(cluster.newSession(), new TweetsSupplier(fileInputStream));

      int replicationFactorNum = Integer.parseInt(arguments.get("replication-factor"));
      loader.createDb(replicationFactorNum);

      int iterationsNum = Integer.parseInt(arguments.get("iter"));
      loader.populateDb(iterationsNum);
    }
  }
}
