import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import payments.simple.SimplifiedPaymentsMapper;
import payments.simple.SimplifiedPaymentsReducer;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;


public class PaymentsSimplifiedMapReduceTests {
  @Test
  public void testPaymentsMapper_result_shouldContainAllPayments() throws Exception {
    MapDriver<NullWritable, Text, LongWritable, Text> mapDriver = new MapDriver<>(new SimplifiedPaymentsMapper());

    String rawData =
      "2016-07-02 20:52:39 1 12.01 www.store1.com\n" +
      "2016-07-02 20:52:39 12 4.05 www.store2.com\n" +
      "2016-07-02 20:52:40 1 77.70 www.store3.com";

    String[] lines = rawData.split("\n");
    for (String input : lines) {
      mapDriver.withInput(NullWritable.get(), new Text(input));
    }

    List<Pair<LongWritable, Text>> mapResult = mapDriver.run();

    Pair<LongWritable, Text> matchItem1 = createMapPair(1, "12.01:www.store1.com");
    Pair<LongWritable, Text> matchItem2 = createMapPair(12, "4.05:www.store2.com");
    Pair<LongWritable, Text> matchItem3 = createMapPair(1, "77.70:www.store3.com");

    assertThat(mapResult, hasSize(lines.length));
    assertThat(mapResult, containsInAnyOrder(matchItem1, matchItem2, matchItem3));
  }

  private Pair<LongWritable, Text> createMapPair(long paymentId, String paymentData) {
    return new Pair<>(new LongWritable(paymentId), new Text(paymentData));
  }

  @Test
  public void testPaymentsReducer_result_shouldContainGroupedPayments_orderedBy_ID_ASC() throws Exception {
    ReduceDriver<LongWritable, Text, NullWritable, Text> reduceDriver = new ReduceDriver<>(new SimplifiedPaymentsReducer());

    List<Text> items1 = Arrays.asList(new Text("12.01:www.store1.com"), new Text("77.70:www.store3.com"));
    List<Text> items2 = Arrays.asList(new Text("4.05:www.store2.com"));

    reduceDriver.withInput(new LongWritable(1), items1);
    reduceDriver.withInput(new LongWritable(2), items2);

    List<Pair<NullWritable, Text>> result = reduceDriver.run();

    String jsonResult1 = "{\"id\":1,\"total\":89.71,\"stores\":[\"www.store1.com\",\"www.store3.com\"]}";
    String jsonResult2 = "{\"id\":2,\"total\":4.05,\"stores\":[\"www.store2.com\"]}";

    Pair<NullWritable, Text> matchItem1 = createReducePair(jsonResult1);
    Pair<NullWritable, Text> matchItem2 = createReducePair(jsonResult2);

    assertThat(result, hasSize(2));
    assertThat(result, contains(matchItem1, matchItem2));
  }

  private Pair<NullWritable, Text> createReducePair(String grouppedPaymentsAsJson) {
    return new Pair<>(NullWritable.get(), new Text(grouppedPaymentsAsJson));
  }

  @Test
  public void testPayments_withSimplePaymentReducer_mapReduceCycle() throws Exception {
    MapReduceDriver<NullWritable, Text, LongWritable, Text, NullWritable, Text> mrDriver =
        MapReduceDriver.newMapReduceDriver(new SimplifiedPaymentsMapper(), new SimplifiedPaymentsReducer());

    mrDriver.withInput(NullWritable.get(), new Text("2016-07-02 20:52:39 1 12.01 www.store1.com"));
    mrDriver.withInput(NullWritable.get(), new Text("2016-07-02 20:52:39 2 1.75 www.store1.com"));
    mrDriver.withInput(NullWritable.get(), new Text("2016-07-02 20:52:39 2 4.05 www.store2.com"));

    mrDriver.withOutput(NullWritable.get(), new Text("{\"id\":1,\"total\":12.01,\"stores\":[\"www.store1.com\"]}"));
    mrDriver.withOutput(NullWritable.get(), new Text("{\"id\":2,\"total\":5.80,\"stores\":[\"www.store1.com\",\"www.store2.com\"]}"));

    mrDriver.runTest();
  }
}
