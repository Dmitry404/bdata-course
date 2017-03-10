package webapp.entities;

public class Tweet {
  private String id;
//  private double gaussian;
  private String hashtag;
  private String user;
  private String message;

  public Tweet(String id, String hashtag, String user, String message) {
    this.id = id;
    this.user = user;
    this.message = message;
    this.hashtag = hashtag;
  }

  public String getId() {
    return id;
  }

  public String getHashtag() {
    return hashtag;
  }

  public String getUser() {
    return user;
  }

  public String getMessage() {
    return message;
  }
}
