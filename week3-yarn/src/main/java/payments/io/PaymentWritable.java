package payments.io;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class PaymentWritable implements Writable {
  private double total;
  private String store;

  public PaymentWritable() {}

  public PaymentWritable(double total, String store) {
    this.total = total;
    this.store = store;
  }


  @Override
  public void write(DataOutput out) throws IOException {
    out.writeDouble(total);
    out.writeUTF(store);
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    total = in.readDouble();
    store = in.readUTF();
  }

  public double getTotal() {
    return total;
  }

  public String getStore() {
    return store;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PaymentWritable that = (PaymentWritable) o;

    return Double.compare(that.total, total) == 0 && (store != null ? store.equals(that.store) : that.store == null);
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = Double.doubleToLongBits(total);
    result = (int) (temp ^ (temp >>> 32));
    result = 31 * result + (store != null ? store.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PaymentWritable{" +
        "total=" + total +
        ", store='" + store + '\'' +
        '}';
  }
}
