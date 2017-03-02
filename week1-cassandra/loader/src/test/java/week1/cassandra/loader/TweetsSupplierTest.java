package week1.cassandra.loader;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class TweetsSupplierTest {
  @Test
  public void simpleOneLineTest() throws Exception {
    TweetsSupplier supplier = new TweetsSupplier("oneLineTweet.csv");
    TweetsSupplier.Tweet tweet = supplier.getTweet()
        .orElse(TweetsSupplier.Tweet.EMPTY);

    assertThat(tweet.id, is("837277950202490885"));
  }

  @Test
  public void wrongFileTest() throws Exception {
    TweetsSupplier supplier = new TweetsSupplier("not-a-file");

    TweetsSupplier.Tweet tweet = supplier.getTweet()
        .orElse(TweetsSupplier.Tweet.EMPTY);

    assertThat(tweet.id, is(" "));
  }

  @Test
  public void emptyTweetsFile() throws Exception {
    TweetsSupplier supplier = new TweetsSupplier("emptyTweets.csv");

    TweetsSupplier.Tweet tweet = supplier.getTweet()
        .orElse(TweetsSupplier.Tweet.EMPTY);

    assertThat(tweet.id, is(" "));
    assertThat(supplier.hasMoreTweets(), is(false));
  }
}
