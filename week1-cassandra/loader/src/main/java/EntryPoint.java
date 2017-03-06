import com.datastax.driver.core.Cluster;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import loader.DbLoader;
import loader.TweetsSupplier;

public class EntryPoint {
  /*
    EntryPoint.main("--load", "tweets.csv"); // loads to DB from tweets.csv
  */
  public static void main(String... args) {
    String filePath = "";
    String iterations = "";

    for (int i = 0; i < args.length; i++) {
      String arg = args[i];

      if (arg.equalsIgnoreCase("--load") && args.length > i + 1) {
        filePath = args[i + 1];
      }
      if (arg.equalsIgnoreCase("--iter")) {
        iterations = (args.length > i + 1) ? args[i + 1] : "1";
      }
    }

    if (!filePath.isEmpty()) {
      Cluster cluster = Cluster.builder()
          .addContactPoint("127.0.0.1")
          .build();

      InputStream fileInputStream = null;
      try {
        fileInputStream = new FileInputStream(filePath);
      } catch (FileNotFoundException e) {
        System.err.println(e.getMessage());
        System.exit(-1);
      }

      DbLoader loader = new DbLoader(cluster.newSession(), new TweetsSupplier(fileInputStream));
      loader.createDb();
      loader.populateDb();
    }
  }
}
