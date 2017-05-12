import org.apache.hadoop.conf.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import avro.Converter;
import avro.data.Orders;
import generator.FruitStore;
import hdfs.AvroDataReader;
import hdfs.AvroDataWriter;
import hdfs.ParquetDataReader;
import hdfs.ParquetDataWriter;

public class EntryPoint {
  private static final String FRUIT_STORES_DATA_FILE_AVRO = "fruit_stores.avro";
  private static final String FRUIT_STORES_DATA_FILE_PARQUET = "fruit_stores.parquet";

  public static void main(String[] args) {
    if (isDataGenerationRequested(args)) {
      List<Orders> ordersList = generateData();

      new AvroDataWriter(getHdfsConfig(args), FRUIT_STORES_DATA_FILE_AVRO).write(ordersList);
      new ParquetDataWriter(getHdfsConfig(args), FRUIT_STORES_DATA_FILE_PARQUET).write(ordersList);
    } else if (readAsAvro(args)) {
      new AvroDataReader(getHdfsConfig(args), FRUIT_STORES_DATA_FILE_AVRO).read().forEach(System.out::println);
    } else if (readAsParquet(args)) {
      new ParquetDataReader(getHdfsConfig(args), FRUIT_STORES_DATA_FILE_PARQUET).read().forEach(System.out::println);
    } else {
      System.out.println("No valid params passed:");
      Arrays.stream(args).forEach(System.out::println);
    }
  }

  private static Configuration getHdfsConfig(String[] args) {
    Configuration configuration = new Configuration();
    configuration.set("fs.defaultFS","hdfs://" + getHostFrom(args) + ":9009");

    return configuration;
  }

  // add metrics to read/write methods - e.g. size of an order
  // metrics for fruits when generation?

  private static boolean isDataGenerationRequested(String[] args) {
    return args.length >= 2 && args[0].equals("--load_data_to");
  }

  private static String getHostFrom(String... args) {
    return args.length < 1 ? "localhost" : args[1];
  }

  private static boolean readAsAvro(String[] args) {
    return args.length >= 3 && args[2].equals("--read_avro");
  }

  private static boolean readAsParquet(String[] args) {
    return args.length >= 3 && args[2].equals("--read_parquet");
  }

  private static List<Orders> generateData() {
    Map<String, Double> fruits = getFruitsMap();

    FruitStore fruitStore = new FruitStore(fruits, 1, 5);
    FruitStore fruitStore2 = new FruitStore(fruits, 5, fruits.size() / 2);
    FruitStore fruitStore3 = new FruitStore(fruits, 10, fruits.size());
    FruitStore fruitStore4 = new FruitStore(fruits, 1000, fruits.size());
    FruitStore fruitStore5 = new FruitStore(fruits, 10000, fruits.size());

    Orders orders = new Orders("Kennebec Fruit Company S", Converter.toAvroOrders(fruitStore.generateOrders(5)));
    Orders orders2 = new Orders("Kennebec Fruit Company M", Converter.toAvroOrders(fruitStore2.generateOrders(10)));
    Orders orders3 = new Orders("Kennebec Fruit Company L", Converter.toAvroOrders(fruitStore3.generateOrders(100)));
    Orders orders4 = new Orders("Kennebec Fruit Company XL", Converter.toAvroOrders(fruitStore4.generateOrders(1000)));
    Orders orders5 = new Orders("Kennebec Fruit Company XXL", Converter.toAvroOrders(fruitStore5.generateOrders(10000)));

    return Arrays.asList(orders, orders2, orders3, orders4, orders5);
  }

  private static Map<String, Double> getFruitsMap() {
    Map<String, Double> fruits = new HashMap<>();

    fruits.put("apple", 10.10);
    fruits.put("prune", 8.75);
    fruits.put("pear", 9.99);
    fruits.put("watermelon", 25.00);
    fruits.put("lemon", 4.00);
    fruits.put("lime", 4.99);
    fruits.put("banana", 1.99);
    fruits.put("pineapple", 10.00);
    fruits.put("strawberry", 0.99);
    fruits.put("strawberry", 0.99);
    fruits.put("raspberry", 0.99);
    fruits.put("orange", 1.99);
    fruits.put("grape", 0.55);
    fruits.put("cherry", 0.05);
    fruits.put("peach", 1.05);
    fruits.put("grapefruit", 4.15);
    fruits.put("mango", 3.78);
    fruits.put("kiwifruit", 1.28);
    fruits.put("avocado", 10.00);
    fruits.put("papaya", 4.00);
    fruits.put("apricot", 1.00);
    fruits.put("pomegranate", 10.20);
    fruits.put("coconut", 9.99);
    fruits.put("durian", 99.99);

    return fruits;
  }
}
