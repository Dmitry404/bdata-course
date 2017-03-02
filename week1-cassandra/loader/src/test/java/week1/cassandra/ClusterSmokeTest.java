package week1.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

public class ClusterSmokeTest {
  @Ignore
  @Test
  public void checkIfRunning() {
    try (Cluster cluster = Cluster.builder()
        .addContactPoint("127.0.0.1")
        .build()) {
      Session session = cluster.connect();

      ResultSet rs = session.execute("select release_version from system.local");
      Row row = rs.one();

      assertThat(row.getString("release_version"), startsWith("3."));
    }
  }
}
