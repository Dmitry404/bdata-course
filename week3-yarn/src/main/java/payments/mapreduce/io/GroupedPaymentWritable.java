package payments.mapreduce.io;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class GroupedPaymentWritable implements WritableComparable<GroupedPaymentWritable> {
  private long paymentId;
  private double total;
  private Set<String> stores;

  public GroupedPaymentWritable() {}

  public GroupedPaymentWritable(long paymentId, double total, Set<String> stores) {
    this.paymentId = paymentId;
    this.total = total;
    this.stores = stores;
  }

  @Override
  public int compareTo(GroupedPaymentWritable o) {
    return Long.compare(paymentId, o.paymentId);
  }

  @Override
  public void write(DataOutput out) throws IOException {
    out.writeLong(paymentId);
    out.writeDouble(total);
    out.writeInt(stores.size());
    for (String store : stores) {
      out.writeUTF(store);
    }
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    paymentId = in.readLong();
    total = in.readDouble();
    stores = new HashSet<>();
    int storesSize = in.readInt();
    while (storesSize > 0) {
      stores.add(in.readUTF());
      storesSize--;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    GroupedPaymentWritable that = (GroupedPaymentWritable) o;

    return paymentId == that.paymentId && Double.compare(that.total, total) == 0 && (stores != null ? stores.equals(that.stores) : that.stores == null);
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = (int) (paymentId ^ (paymentId >>> 32));
    temp = Double.doubleToLongBits(total);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    result = 31 * result + (stores != null ? stores.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "GroupedPaymentWritable{" +
        "paymentId=" + paymentId +
        ", total=" + total +
        ", stores=[" + StringUtils.join(stores, ",") + "]" +
        '}';
  }
}
