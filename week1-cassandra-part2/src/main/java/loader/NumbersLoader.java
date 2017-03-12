package loader;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;

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
          + "diagMessage varchar)");
    }
  }

  public void load() {
    new NumbersSupplier(parallelism).loadTo((id, diagMessage) -> {
      try (Session session = cluster.newSession()) {
        String query = "INSERT INTO bdcourse.numbers(id, value, diagMessage) VALUES (?, ?, ?)";
        session.execute(query, id, new Random().nextGaussian(), diagMessage);

        //throw new RuntimeException("TODO read, sum and update");
        // updatedAt, updatesCount
      }
    }, maxId);
  }

  private static class NumbersSupplier {
    private final int parallelism;
    CountDownLatch doneSignal = new CountDownLatch(1);

    NumbersSupplier(int parallelism) {
      this.parallelism = parallelism;
    }

    void loadTo(BiConsumer<Integer, String> consumer, int maxId) {
      ForkJoinPool forkJoinPool = new ForkJoinPool(parallelism);
      forkJoinPool.submit(() -> {
        ThreadLocalRandom.current().ints(Long.MAX_VALUE, 1, maxId)
            .parallel()
            .filter(i -> i > 0)
            .forEach(id -> consumer.accept(id, Thread.currentThread().getName()));
        doneSignal.countDown();
      });

      try {
        doneSignal.await();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
