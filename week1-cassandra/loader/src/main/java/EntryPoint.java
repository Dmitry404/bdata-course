import com.datastax.driver.core.Cluster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import webapp.WebApp;
import week1.cassandra.loader.DbLoader;
import week1.cassandra.loader.TweetsSupplier;

public class EntryPoint {
  /*
    EntryPoint.main("--load", "tweets.csv"); // loads to DB from tweets.csv
    EntryPoint.main("--webapp"); // starts webapp
    */
  public static void main(String... args) {
    String fileName = "";
    String iterations = "";

    boolean runWebApp = false;

    for (int i = 0; i < args.length; i++) {
      String arg = args[i];

      if (arg.equalsIgnoreCase("--webapp")) {
        runWebApp = true;
      }

      if (arg.equalsIgnoreCase("--load") && args.length > i + 1) {
        fileName = args[i + 1];
      }
      if (arg.equalsIgnoreCase("--iter")) {
        iterations = (args.length > i + 1) ? args[i + 1] : "1";
      }
    }

    if (!fileName.isEmpty()) {
      System.out.println("Loading DB in a thread");

      String filePath = fileName;
      new Thread(() -> {
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
      }).start();
    }

    if (runWebApp) {
      System.out.println("Running webapp in a thread");

      new Thread(WebApp::main).start();
    }
  }
}
