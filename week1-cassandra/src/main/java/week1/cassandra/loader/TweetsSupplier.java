package week1.cassandra.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class TweetsSupplier {
  private BufferedReader bufferedReader = null;

  public TweetsSupplier(InputStream inputStream) {
    try {
      Reader reader = new InputStreamReader(inputStream, "UTF-8");
      bufferedReader = new BufferedReader(reader);

      skipHeader();
    } catch (IOException e) {
      drawnSupplier();
    }
  }

  private void drawnSupplier() {
    if (bufferedReader != null) {
      try {
        bufferedReader.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    bufferedReader = null;
  }

  private boolean isDrown() {
    return bufferedReader == null;
  }

  public boolean hasMoreTweets() {
    return !isDrown();
  }

  private String skipHeader() throws IOException {
    return bufferedReader.readLine();
  }

  public Tweet getTweet() {
    if (isDrown()) {
      return null;
    }
    try {
      while(true) {
        String line = bufferedReader.readLine();
        if (line == null) {
          drawnSupplier();
          return null;
        }
        Tweet tweet = new Tweet(line);
        if (!tweet.isValid) {
          continue;
        }
        return tweet;
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static class Tweet {
    public final String id;
    public final String hashTag;
    public final String user;
    public final String message;
    public final String createdAt;
    public final String latitude;
    public final String longitude;

    private final boolean isValid;

    public Tweet(String line) {
      String[] strings = line.split(",");

      isValid = !(strings.length <= 2);

      id = strings.length > 0 ? strings[0] : "";
      hashTag = strings.length > 1 ? strings[1] : "";
      user = strings.length > 2 ? strings[2] : "";
      message = strings.length > 3 ? strings[3] : "";
      createdAt = strings.length > 4 ? strings[4] : "";

      latitude = strings.length > 5 ? strings[5] : "";
      longitude = strings.length > 6 ? strings[6] : "";
    }

    @Override
    public String toString() {
      return "Tweet{" +
          "isValid=" + isValid +
          ", id='" + id + '\'' +
          ", hashTag='" + hashTag + '\'' +
          ", user='" + user + '\'' +
          ", message='" + message + '\'' +
          ", createdAt='" + createdAt + '\'' +
          ", latitude='" + latitude + '\'' +
          ", longitude='" + longitude + '\'' +
          '}';
    }
  }
}
