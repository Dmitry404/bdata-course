package week1.cassandra.loader;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DbLoaderTest {
  private Cluster cluster;

  @Before
  public void setUp() throws Exception {
    cluster = Cluster.builder()
        .addContactPoint("127.0.0.1")
        .build();
  }

  @After
  public void tearDown() throws Exception {
    if (!cluster.isClosed()) {
      cluster.close();
    }
  }

  @Test
  public void mainFlow() throws Exception {
    DbLoader loader = new DbLoader(cluster.newSession(), new TweetsSupplier("oneLineTweet.csv"));
    loader.truncateDb();
    loader.populateDb();

    Session session = cluster.connect();
    ResultSet rs = session.execute("select * from bdcourse.tweets");
    Row row = rs.one();

    assertThat(row.getString("id"), is("837277950202490885"));

    session.close();
    loader.truncateDb();
  }
}
