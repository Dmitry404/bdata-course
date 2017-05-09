import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import generator.FruitStore;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FruitsStoreOrdersGeneratorTest {
  @Test
  public void testGenerateSomeOrders() throws Exception {
    Map<String, Double> fruits = new HashMap<>();
    fruits.put("apple", 10.10);

    int maxQtyPerProduct = 1;
    int maxProductsInOrder = 1;

    FruitStore store = new FruitStore(fruits, maxQtyPerProduct, maxProductsInOrder);
    List<FruitStore.Order> orders = store.generateOrders(10);

    assertThat(orders.size(), is(10));
    assertThat(orders.get(0).getTotal(), is(10.10));
  }

  @Test
  public void testGenerateSomeOrders_whenMoreFruitsIsPresent() throws Exception {
    Map<String, Double> fruits = new HashMap<>();
    fruits.put("apple", 10.10);
    fruits.put("prune", 8.75);
    fruits.put("pear", 9.99);
    fruits.put("watermelon", 25.00);
    fruits.put("lemon", 4.00);
    fruits.put("banana", 1.99);

    int maxQtyPerProduct = 5;
    int maxProductsInOrder = 10;

    FruitStore store = new FruitStore(fruits, maxQtyPerProduct, maxProductsInOrder);
    List<FruitStore.Order> orders = store.generateOrders(5);

    assertThat(orders.size(), is(5));
  }
}
