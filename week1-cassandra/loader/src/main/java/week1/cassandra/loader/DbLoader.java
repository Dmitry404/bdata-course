package week1.cassandra.loader;

import com.datastax.driver.core.Session;

public class DbLoader {
  private final Session session;
  private final TweetsSupplier tweetsSupplier;

  public DbLoader(Session session, TweetsSupplier tweetsSupplier) {
    this.session = session;
    this.tweetsSupplier = tweetsSupplier;
  }

  public void populateDb() {
    while (tweetsSupplier.hasMoreTweets()) {

      TweetsSupplier.Tweet tweet = tweetsSupplier.getTweet();
      if (tweet == null) {
        return;
      }
      session.execute("INSERT INTO bdcourse.tweets(id, hashtag, user, message) VALUES (?, ?, ?, ?)",
          tweet.id, tweet.hashTag, tweet.user, tweet.message);
    }
  }

  public void truncateDb() {
    session.execute("TRUNCATE bdcourse.tweets");
  }

  public void createDb() {
    session.execute("CREATE KEYSPACE IF NOT EXISTS bdcourse\n" +
        "WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 1}");

    session.execute("USE bdcourse");

    String query = "CREATE TABLE tweets(id text PRIMARY KEY, "
        + "hashtag text, "
        + "user text, "
        + "message varchar );";
    session.execute(query);
  }
}
