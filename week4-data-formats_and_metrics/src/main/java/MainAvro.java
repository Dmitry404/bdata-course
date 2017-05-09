import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import avro.Converter;
import avro.data.Orders;
import generator.FruitStore;

public class MainAvro {
  public static void main(String[] args) throws IOException {
    DatumWriter<Orders> datumWriter = new SpecificDatumWriter<>(Orders.class);
    DataFileWriter<Orders> fileWriter = new DataFileWriter<>(datumWriter);

    Map<String, Double> fruits = getFruitsMap();

    FruitStore fruitStore = new FruitStore(fruits, 1, 5);
    FruitStore fruitStore2 = new FruitStore(fruits, 5, fruits.size() / 2);
    FruitStore fruitStore3 = new FruitStore(fruits, 10, fruits.size());

    Orders orders = new Orders("Kennebec Fruit Company Loc. 1", Converter.toAvroOrders(fruitStore.generateOrders(10)));
    Orders orders2 = new Orders("Kennebec Fruit Company Loc. 2", Converter.toAvroOrders(fruitStore2.generateOrders(10)));
    Orders orders3 = new Orders("Kennebec Fruit Company Loc. 3", Converter.toAvroOrders(fruitStore3.generateOrders(10)));

    fileWriter.create(orders.getSchema(), new File("fruit_stores_data.avro"));

    fileWriter.append(orders);
    fileWriter.append(orders2);
    fileWriter.append(orders3);

    fileWriter.close();

    DatumReader<Orders> datumReader = new SpecificDatumReader<>(Orders.class);
    DataFileReader<Orders> fileReader = new DataFileReader<>(new File("fruit_stores_data.avro"), datumReader);

    while(fileReader.hasNext()) {
      Orders o = fileReader.next();
      System.out.println(o);
    }
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
