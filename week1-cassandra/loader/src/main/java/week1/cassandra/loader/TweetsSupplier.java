package week1.cassandra.loader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Optional;
import java.util.zip.GZIPInputStream;

public class TweetsSupplier {
  private BufferedReader bufferedReader = null;

  public TweetsSupplier(String csvFile) {
    try {
      InputStream fileInputStream = new FileInputStream(csvFile);
      //InputStream gzInputStream = new GZIPInputStream(fileInputStream);
      Reader reader = new InputStreamReader(fileInputStream, "UTF-8");

      bufferedReader = new BufferedReader(reader);

      skipHeader();
    } catch (IOException e) {
      e.printStackTrace();
      // make a safe point here, e.g. setting things to safe values
    }
  }

  private String skipHeader() throws IOException {
    return bufferedReader.readLine();
  }

  public Optional<Tweet> getTweet() {
    if (bufferedReader == null) {
      return Optional.empty();
    }
    try {
      String line = bufferedReader.readLine();
      if (line == null) {
        bufferedReader.close();
        bufferedReader = null;
        return Optional.empty();
      }
      return Optional.of(new Tweet(line));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return Optional.empty();
  }

  public boolean hasMoreTweets() {
    return bufferedReader != null;
  }

  public static class Tweet {
    public static final Tweet EMPTY = new Tweet(" , , , , ,");

    public final String id;
    public final String hashTag;
    public final String user;
    public final String message;
    public final String createdAt;
    public final String latitude;
    public final String longitude;

    public Tweet(String line) {
      String[] strings = line.split(",");

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
          "id='" + id + '\'' +
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
