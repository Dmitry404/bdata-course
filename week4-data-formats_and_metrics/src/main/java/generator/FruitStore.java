package generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FruitStore {
  private final Map<String, Double> fruits;
  private final int maxQtyPerProduct;
  private final int maxProductsInOrder;

  public FruitStore(Map<String, Double> fruits, int maxQtyPerProduct, int maxProductsInOrder) {
    this.fruits = fruits;
    this.maxQtyPerProduct = maxQtyPerProduct;
    this.maxProductsInOrder = maxProductsInOrder;
  }

  public List<Order> generateOrders(int numberOfOrders) {
    return IntStream.rangeClosed(1, numberOfOrders)
        .mapToObj(id -> new Order(id, pickFruits()))
        .collect(Collectors.toList());
  }

  private List<Fruit> pickFruits() {
    List<String> fruitNames = new ArrayList<>(fruits.keySet());
    Collections.shuffle(fruitNames);

    return fruitNames.subList(0, getProductsNumberInOrder(fruitNames)).stream()
        .map(this::createFruitSet)
        .collect(Collectors.toList());
  }

  private int getProductsNumberInOrder(List<String> fruitNames) {
    int maxProducts = fruitNames.size() < maxProductsInOrder ? fruitNames.size() : maxProductsInOrder;
    return new Random().nextInt(maxProducts) + 1;
  }

  private Fruit createFruitSet(String fruitName) {
    int qty = new Random().nextInt(maxQtyPerProduct) + 1;
    return new Fruit(fruitName, fruits.get(fruitName), qty);
  }

  public static class Order {
    private final int id;
    private final List<Fruit> fruits;
    private final double total;

    private Order(int id, List<Fruit> fruits) {
      this.id = id;
      this.fruits = fruits;

      this.total = fruits.stream()
          .mapToDouble(fruit -> fruit.price * fruit.qty)
          .sum();
    }

    public int getId() {
      return id;
    }

    public double getTotal() {
      return total;
    }

    public List<Fruit> getFruits() {
      return fruits;
    }
  }

  public static class Fruit {
    private final String name;
    private final double price;
    private final int qty;

    private Fruit(String name, double price, int qty) {
      this.name = name;
      this.price = price;
      this.qty = qty;
    }

    public String getName() {
      return name;
    }

    public double getPrice() {
      return price;
    }

    public int getQty() {
      return qty;
    }
  }
}
