package week1.cassandra.loader;

import org.junit.Ignore;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class TweetsSupplierTest {
  @Test
  public void simpleOneLineTest() throws Exception {
    InputStream fileInputStream = new FileInputStream("oneLineTweet.csv");

    TweetsSupplier supplier = new TweetsSupplier(fileInputStream);
    TweetsSupplier.Tweet tweet = supplier.getTweet();

    assertThat(tweet.id, is("837277950202490885"));
  }

  @Test
  public void emptyTweetsFile() throws Exception {
    InputStream inputStream = new FileInputStream("emptyTweets.csv");

    TweetsSupplier supplier = new TweetsSupplier(inputStream);
    TweetsSupplier.Tweet tweet = supplier.getTweet();

    assertThat(tweet, is(nullValue()));
    assertThat(supplier.hasMoreTweets(), is(false));
  }
}
