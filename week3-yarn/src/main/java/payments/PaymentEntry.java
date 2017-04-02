package payments;

import java.util.Set;

public class PaymentEntry {
  private final long id;
  private final Double total;
  private final Set<String> stores;

  public PaymentEntry(long id, double total, Set<String> stores) {
    this.id = id;
    this.total = total;
    this.stores = stores;
  }
}
