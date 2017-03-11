public class EntryPoint {
  public static void main(String... args) {
    NumbersLoader loader = new NumbersLoader("127.0.0.1", 1000, 1);
    loader.load();
  }
}
