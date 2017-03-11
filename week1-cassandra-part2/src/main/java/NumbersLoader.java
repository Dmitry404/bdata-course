import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class NumbersLoader {
  private final int maxId;
  private final int parallelism;

  private final Cluster cluster;

  public NumbersLoader(String clusterHost, int maxId, int parallelism) {
    this.maxId = maxId;
    this.parallelism = parallelism;

    cluster = Cluster.builder()
        .addContactPoint(clusterHost)
        .build();

    initDb();
  }

  private void initDb() {
    try (Session session = cluster.connect()) {
      session.execute("CREATE KEYSPACE IF NOT EXISTS bdcourse\n" +
          "WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 1}");
      session.execute("USE bdcourse");
      session.execute("CREATE TABLE IF NOT EXISTS numbers("
          + "id int PRIMARY KEY, "
          + "value double, "
          + "message varchar)");
    }
  }

  public void load() {
    new Loader(parallelism).loadTo(data -> {
      try (Session session = cluster.newSession()) {
        String query = "INSERT INTO bdcourse.numbers(id, value, message) VALUES (?, ?, ?)";
        session.execute(query, data.id, new Random().nextGaussian(), data.message);
      }
    }, maxId);
  }

  private static class Loader {
    private final int parallelism;
    CountDownLatch doneSignal = new CountDownLatch(1);

    public Loader(int parallelism) {
      this.parallelism = parallelism;
    }

    public void loadTo(Consumer<Data> consumer, int maxId) {
      ForkJoinPool forkJoinPool = new ForkJoinPool(parallelism);
      forkJoinPool.submit(() -> {
        ThreadLocalRandom.current().ints(Long.MAX_VALUE, 1, maxId)
            .parallel()
            .filter(i -> i > 0)
            .forEach(id -> consumer.accept(new Data(id, Thread.currentThread().getName())));
        doneSignal.countDown();
      });

      try {
        doneSignal.await();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private static class Data {
    final int id;
    final String message;

    private Data(int id, String message) {
      this.id = id;
      this.message = message;
    }
  }
}
