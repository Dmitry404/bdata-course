package payments.mapreduce.simple;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Set;
import java.util.TreeSet;

import payments.json.GroupedPayment;

public class PaymentsJob {
  public static class SimplifiedPaymentsMapper extends Mapper<NullWritable, Text, LongWritable, Text> {
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

  public static class SimplifiedPaymentsReducer extends Reducer<LongWritable, Text, NullWritable, Text> {
    private Gson gson = new GsonBuilder()
        .registerTypeAdapter(Double.class, createDoubleSerializer())
        .create();

    private JsonSerializer<Double> createDoubleSerializer() {
      return (value, type, context) ->
          new JsonPrimitive((new BigDecimal(value)).setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    @Override
    protected void reduce(LongWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
      Set<String> stores = new TreeSet<>();
      double total = 0.0;

      for (Text value : values) {
        String[] paymentData = value.toString().split(":");
        if (paymentData.length >= 2) {
          total += Double.parseDouble(paymentData[0]);
          stores.add(paymentData[1]);
        }
      }

      if (!stores.isEmpty()) {
        GroupedPayment payment = new GroupedPayment(key.get(), total, stores);
        context.write(NullWritable.get(), getPaymentJson(payment));
      }
    }

    private Text getPaymentJson(GroupedPayment paymentEntry) {
      return new Text(gson.toJson(paymentEntry, GroupedPayment.class));
    }
  }
}
