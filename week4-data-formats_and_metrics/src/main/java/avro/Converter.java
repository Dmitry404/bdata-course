package avro;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import avro.data.Order;
import generator.FruitStore;

public class Converter {
  public static List<Order> toAvroOrders(List<FruitStore.Order> orders) {
    return orders.stream()
        .map(Converter::mapFruitOrderToAvro)
        .collect(Collectors.toList());
  }

  private static Order mapFruitOrderToAvro(FruitStore.Order order) {
    return Order.newBuilder()
        .setId(order.getId())
        .setTotal(order.getTotal())
        .setGoods(mapGoods(order))
        .build();
  }

  private static Map<CharSequence, Double> mapGoods(FruitStore.Order order) {
    return order.getFruits().stream()
        .collect(Collectors.toMap(FruitStore.Fruit::getName, fruit -> fruit.getQty() * fruit.getPrice()));
  }
}
