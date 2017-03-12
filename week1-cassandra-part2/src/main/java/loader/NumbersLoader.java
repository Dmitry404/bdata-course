package loader;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.util.Date;
import java.util.Random;

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
          + "diag_message varchar,"
          + "value_increment int,"
          + "updated_at timestamp"
          + ")");
    }
  }

  public void load(boolean updateRecord) {
    new NumbersSupplier(parallelism).loadTo((id, diagMessage) -> {
      double currentValue = 0.0;
      int currentValueIncr = 0;

      try (Session session = cluster.newSession()) {
        if (updateRecord) {
          String query = "SELECT value, value_increment FROM bdcourse.numbers WHERE id=?";
          Row row = session.execute(query, id).one();
          if (row != null) {
            currentValue = row.getDouble("value");
            currentValueIncr = row.getInt("value_increment");
          }
        }

        String query = "INSERT INTO bdcourse.numbers(" +
            "id, value, diag_message," +
            "value_increment, updated_at) VALUES (?, ?, ?, ?, ?)";
        session.execute(query, id, currentValue + new Random().nextGaussian(), diagMessage,
            ++currentValueIncr, new Date());
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
    }, maxId);
  }
}
