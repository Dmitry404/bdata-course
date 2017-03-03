package week1.cassandra.loader;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DbLoaderTest {

  @Test
  public void main() throws Exception {
    try (Cluster cluster = Cluster.builder()
        .addContactPoint("127.0.0.1")
        .build()) {
      Session session = cluster.connect();

      DbLoader loader = new DbLoader(cluster.newSession(), new TweetsSupplier("oneLineTweet.csv"));
      loader.truncateDb();
      loader.populateDb();

      ResultSet rs = session.execute("select * from bdcourse.tweets");
      Row row = rs.one();

      assertThat(row.getString("id"), is("837277950202490885"));
    }
  }
}
