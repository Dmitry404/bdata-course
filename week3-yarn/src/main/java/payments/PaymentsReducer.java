package payments;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Set;
import java.util.TreeSet;

import payments.io.GroupedPaymentWritable;
import payments.io.PaymentWritable;

public class PaymentsReducer extends Reducer<LongWritable, PaymentWritable, NullWritable, GroupedPaymentWritable> {

  @Override
  protected void reduce(LongWritable key, Iterable<PaymentWritable> values, Context context) throws IOException, InterruptedException {
    double total = 0.0;
    Set<String> stores = new TreeSet<>();
    for (PaymentWritable payment : values) {
      total += payment.getTotal();
      stores.add(payment.getStore());
    }

    total = (new BigDecimal(total)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    context.write(NullWritable.get(), new GroupedPaymentWritable(key.get(), total, stores));
  }
}
