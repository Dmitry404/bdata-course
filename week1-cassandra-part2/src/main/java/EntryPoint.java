import arguments.ArgumentsParser;
import loader.NumbersLoader;

public class EntryPoint {
  public static void main(String... args) {
    ArgumentsParser arguments = new ArgumentsParser(args);
    arguments.addDefault("host", "127.0.0.1");
    arguments.addDefault("max_id", "1000");
    arguments.addDefault("parallelism", "1");
    arguments.addDefault("sum_values", "false");
    arguments.addDefault("replication_factor", "1");

    System.out.println(arguments);
    
    NumbersLoader loader = new NumbersLoader(
        arguments.get("host"),
        Integer.parseInt(arguments.get("max_id")),
        Integer.parseInt(arguments.get("parallelism")),
        Integer.parseInt(arguments.get("replication_factor"))
    );
    loader.load(Boolean.parseBoolean(arguments.get("sum_values")));
  }
}
