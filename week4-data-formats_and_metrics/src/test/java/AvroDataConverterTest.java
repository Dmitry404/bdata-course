import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import avro.Converter;
import avro.data.Orders;
import generator.FruitStore;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AvroDataConverterTest {
  @Test
  public void testConverterWith_simplestCase_oneItemInStore() throws Exception {
    Map<String, Double> fruits = new HashMap<>();
    fruits.put("apple", 0.00);

    int maxQtyPerProduct = 1;
    int maxProductsInOrder = 1;

    FruitStore fruitStore = new FruitStore(fruits, maxQtyPerProduct, maxProductsInOrder);

    Orders orders = new Orders("Test Fruit Store", Converter.toAvroOrders(fruitStore.generateOrders(1)));

    assertThat(orders.getData().size(), is(1));
    assertThat(orders.getData().get(0).getTotal(), is(0.0));
  }
}
