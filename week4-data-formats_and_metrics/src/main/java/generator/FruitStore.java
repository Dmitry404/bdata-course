package generator;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.codahale.metrics.MetricRegistry.name;

public class FruitStore {
  private final Map<String, Double> fruits;
  private final int maxQtyPerProduct;
  private final int maxProductsInOrder;

  private Histogram orderTotalsHistogram;
  private Map<String, Histogram> fruitHistograms;

  public FruitStore(Map<String, Double> fruits, int maxQtyPerProduct, int maxProductsInOrder) {
    this.fruits = fruits;
    this.maxQtyPerProduct = maxQtyPerProduct;
    this.maxProductsInOrder = maxProductsInOrder;

    initMetrics();
  }

  private void initMetrics() {
    MetricRegistry metricsRegistry;
    metricsRegistry = new MetricRegistry();

    GraphiteReporter graphiteReporter;
    graphiteReporter =  GraphiteReporter.forRegistry(metricsRegistry)
        .prefixedWith("java.java-app-node")
        .convertRatesTo(TimeUnit.SECONDS)
        .convertDurationsTo(TimeUnit.MILLISECONDS)
        .filter(MetricFilter.ALL)
        .build(new Graphite(new InetSocketAddress("graphite-server-node", 2003)));

    ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metricsRegistry)
        .convertRatesTo(TimeUnit.SECONDS)
        .convertDurationsTo(TimeUnit.MILLISECONDS)
        .build();

    orderTotalsHistogram = metricsRegistry.histogram(name(Order.class, "orders-totals"));
    fruitHistograms = new HashMap<>();
    for (Map.Entry<String, Double> entry : fruits.entrySet()) {
      Histogram h = metricsRegistry.histogram(name(Fruit.class, entry.getKey(), "counts"));
      fruitHistograms.put(entry.getKey(), h);
    }

    graphiteReporter.start(1, TimeUnit.MINUTES);
    //consoleReporter.start(1, TimeUnit.SECONDS);
  }

  public List<Order> generateOrders(int numberOfOrders) {
    return IntStream.rangeClosed(1, numberOfOrders)
        .mapToObj(id -> new Order(id, pickFruits()))
        .peek(this::reportOrdersTotals)
        .collect(Collectors.toList());
  }

  private void reportOrdersTotals(Order order) {
    orderTotalsHistogram.update(Math.round(order.getTotal()));
  }

  private List<Fruit> pickFruits() {
    List<String> fruitNames = new ArrayList<>(fruits.keySet());
    Collections.shuffle(fruitNames);

    return fruitNames.subList(0, getProductsNumberInOrder(fruitNames)).stream()
        .map(this::createFruitSet)
        .peek(this::reportFruitsQty)
        .collect(Collectors.toList());
  }

  private void reportFruitsQty(Fruit fruit) {
    fruitHistograms.get(fruit.getName()).update(fruit.getQty());
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
