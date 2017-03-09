package services;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import entities.Tweet;

@Component
public class TweetsService {
  private Cluster cluster;

  public void initCluster(String host) {
    cluster = Cluster.builder()
        .addContactPoint(host)
        .build();
  }

  public List<Tweet> getTweetsLimitedBy(int resultsOnPage) {
    Session session = cluster.connect("bdcourse");
    ResultSet resultSet = session.execute("SELECT id, hashtag, user, message FROM tweets LIMIT " + resultsOnPage);

    return resultSet.all().stream()
        .map(row -> new Tweet(
            row.getString("id"),
            row.getString("hashtag"),
            row.getString("user"),
            row.getString("message")))
        .collect(Collectors.toList());
  }

  public List<Tweet> findTweetsById(String id) {
    Session session = cluster.connect("bdcourse");
    ResultSet resultSet = session.execute(
        "SELECT id, hashtag, user, message FROM tweets WHERE id='" + id + "'");

    return resultSet.all().stream()
        .map(row -> new Tweet(
            row.getString("id"),
            row.getString("hashtag"),
            row.getString("user"),
            row.getString("message")))
        .collect(Collectors.toList());
  }

  public Object findTweetsByMsg(String msg, int resultsOnPage) {
    Session session = cluster.connect("bdcourse");
    ResultSet resultSet = session.execute(
        "SELECT id, hashtag, user, message FROM tweets WHERE expr(tweets_msg_index, '{ " +
            "   query: {type: \"phrase\", field: \"message\", value: \""+ msg + "\", slop: 1} " +
            "}') LIMIT " + resultsOnPage);

    return resultSet.all().stream()
        .map(row -> new Tweet(
            row.getString("id"),
            row.getString("hashtag"),
            row.getString("user"),
            row.getString("message")))
        .collect(Collectors.toList());
  }
}
