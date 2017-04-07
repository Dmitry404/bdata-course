package payments.mapreduce;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Set;
import java.util.TreeSet;


import payments.mapreduce.io.GroupedPaymentWritable;
import payments.mapreduce.io.PaymentWritable;

public class Payments {
  public final static String JOB_NAME = "PaymentsJob";

  public static class PaymentsMapper extends Mapper<LongWritable, Text, LongWritable, PaymentWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
      String[] tokens = value.toString().split(" ");
      if (tokens.length >= 5) {
        LongWritable paymentId = new LongWritable(Long.valueOf(tokens[2]));
        double total = Double.parseDouble(tokens[3]);
        String store = tokens[4];

        context.write(paymentId, new PaymentWritable(total, store));
      }
    }
  }

  public static class PaymentsReducer extends Reducer<LongWritable, PaymentWritable, LongWritable, GroupedPaymentWritable> {
    @Override
    protected void reduce(LongWritable key, Iterable<PaymentWritable> values, Context context) throws IOException, InterruptedException {
      double total = 0.0;
      Set<String> stores = new TreeSet<>();
      for (PaymentWritable payment : values) {
        total += payment.getTotal();
        stores.add(payment.getStore());
      }

      total = (new BigDecimal(total)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
      context.write(new LongWritable(0), new GroupedPaymentWritable(key.get(), total, stores));
    }
  }
}
