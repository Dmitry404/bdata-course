package payments;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

import payments.io.PaymentWritable;

public class PaymentsMapper extends Mapper<NullWritable, Text, LongWritable, PaymentWritable> {
  @Override
  protected void map(NullWritable key, Text value, Context context) throws IOException, InterruptedException {
    String[] tokens = value.toString().split(" ");
    if (tokens.length >= 5) {
      LongWritable paymentId = new LongWritable(Long.valueOf(tokens[2]));
      double total = Double.parseDouble(tokens[3]);
      String store = tokens[4];

      context.write(paymentId, new PaymentWritable(total, store));
    }
  }
}
