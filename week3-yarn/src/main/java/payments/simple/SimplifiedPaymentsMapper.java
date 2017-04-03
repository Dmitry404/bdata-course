package payments.simple;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class SimplifiedPaymentsMapper extends Mapper<NullWritable, Text, LongWritable, Text> {
  @Override
  protected void map(NullWritable key, Text value, Context context) throws IOException, InterruptedException {
    String[] tokens = value.toString().split(" ");
    if (tokens.length >= 5) {
      LongWritable id = new LongWritable(Long.valueOf(tokens[2]));
      Text paymentData = new Text(tokens[3] + ":" + tokens[4]);

      context.write(id, paymentData);
    }
  }
}
