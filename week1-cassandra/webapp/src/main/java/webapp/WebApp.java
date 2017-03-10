package webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import webapp.services.TweetsService;

@SpringBootApplication
public class WebApp implements CommandLineRunner {
  public static void main(String... args) {
    SpringApplication.run(WebApp.class, args);
  }

  @Autowired
  private TweetsService tweetsService;

  @Override
  public void run(String... args) throws Exception {
    if (args.length > 1 && args[0].equalsIgnoreCase("--host")) {
      String host = args[1];
      tweetsService.initCluster(host);
    } else {
      throw new RuntimeException("Run it with --host parameter, e.g. java -jar webapp.jar --host 172.17.0.2");
    }
  }
}