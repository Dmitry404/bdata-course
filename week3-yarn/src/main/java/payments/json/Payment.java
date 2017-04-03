package payments.json;

public class Payment {
  private final double total;
  private final String store;

  public Payment(Double total, String store) {
    this.total = total;
    this.store = store;
  }
}
