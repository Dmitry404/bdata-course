package payments;

import java.util.Set;

public class GroupedPayment {
  private final long id;
  private final double total;
  private final Set<String> stores;

  public GroupedPayment(long id, double total, Set<String> stores) {
    this.id = id;
    this.total = total;
    this.stores = stores;
  }
}
