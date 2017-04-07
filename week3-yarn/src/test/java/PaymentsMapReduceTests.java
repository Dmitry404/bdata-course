import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import payments.mapreduce.PaymentsMR;
import payments.mapreduce.io.GroupedPaymentWritable;
import payments.mapreduce.io.PaymentWritable;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class PaymentsMapReduceTests {
  @Test
  public void testPaymentsMapper_with_PaymentWritable() throws Exception {
    MapDriver<LongWritable, Text, LongWritable, PaymentWritable> mapDriver = new MapDriver<>(new PaymentsMR.PaymentsMapper());

    String rawData =
        "2016-07-02 20:52:39 1 12.01 www.store1.com\n" +
        "2016-07-02 20:52:39 12 4.05 www.store2.com\n" +
        "2016-07-02 20:52:40 1 77.70 www.store3.com";

    splitByLines(rawData).forEach(line -> mapDriver.withInput(new LongWritable(0), new Text(line)));

    Pair<LongWritable, PaymentWritable> matchItem1 = createMapPair(1, 12.01, "www.store1.com");
    Pair<LongWritable, PaymentWritable> matchItem2 = createMapPair(12, 4.05, "www.store2.com");
    Pair<LongWritable, PaymentWritable> matchItem3 = createMapPair(1, 77.70, "www.store3.com");

    List<Pair<LongWritable, PaymentWritable>> mapResult = mapDriver.run();

    assertThat(mapResult, containsInAnyOrder(matchItem1, matchItem2, matchItem3));
  }

  private Pair<LongWritable, PaymentWritable> createMapPair(int paymentId, double total, String store) {
    return new Pair<>(new LongWritable(paymentId), new PaymentWritable(total, store));
  }

  private List<String> splitByLines(String rawData) {
    return Arrays.stream(rawData.split("\n")).collect(Collectors.toList());
  }

  @Test
  public void testPaymentsReducer_with_GroupedPaymentWritable() throws Exception {
    ReduceDriver<LongWritable, PaymentWritable, NullWritable, GroupedPaymentWritable> reduceDriver = new ReduceDriver<>(new PaymentsMR.PaymentsReducer());

    List<Pair<LongWritable, List<PaymentWritable>>> inputData = new ArrayList<>();
    inputData.add(createInputDataPair(1,
        new Pair<>(10.50, "www.store1.com"),
        new Pair<>(2.11, "www.store2.com")));
    inputData.add(createInputDataPair(2,
        new Pair<>(70.65, "www.store1.com")));

    inputData.forEach(reduceDriver::withInput);

    Pair<NullWritable, GroupedPaymentWritable> matchItem1 = createOutputDataPair(1,10.50 + 2.11, "www.store1.com", "www.store2.com");
    Pair<NullWritable, GroupedPaymentWritable> matchItem2 = createOutputDataPair(2, 70.65, "www.store1.com");

    List<Pair<NullWritable, GroupedPaymentWritable>> reduceResult = reduceDriver.run();

    assertThat(reduceResult, contains(matchItem1, matchItem2));
  }

  private Pair<NullWritable, GroupedPaymentWritable> createOutputDataPair(long paymentId, double total, String... stores) {
    return new Pair<>(NullWritable.get(), new GroupedPaymentWritable(paymentId, total, new TreeSet<>(Arrays.asList(stores))));
  }

  private Pair<LongWritable, List<PaymentWritable>> createInputDataPair(long paymentId, Pair<Double, String>... paymentEntries) {
    List<PaymentWritable> payments = Arrays.stream(paymentEntries)
        .map(payment -> new PaymentWritable(payment.getFirst(), payment.getSecond()))
        .collect(Collectors.toList());

    return new Pair<>(new LongWritable(paymentId), payments);
  }

  @Test
  public void testPayments_MapReduce_flow() throws Exception {
    MapReduceDriver<LongWritable, Text, LongWritable, PaymentWritable, NullWritable, GroupedPaymentWritable> mrDriver =
      new MapReduceDriver<>(new PaymentsMR.PaymentsMapper(), new PaymentsMR.PaymentsReducer());

    String rawData =
        "2016-07-02 20:52:39 1 12.01 www.store1.com\n" +
        "2016-07-02 20:52:39 1123 1.75 www.store1.com\n" +
        "2016-07-02 20:52:39 12 4.05 www.store2.com\n" +
        "2016-07-02 20:52:39 1 7.87 www.store1.com\n" +
        "2016-07-02 20:52:40 12 124.67 www.store2.com\n" +
        "2016-07-02 20:52:40 1 9.14 www.store3.com\n" +
        "2016-07-02 20:52:40 1123 14.75 www.store1.com\n" +
        "2016-07-02 20:52:40 12 54.95 www.store2.com\n" +
        "2016-07-02 20:52:40 1 77.70 www.store3.com\n" +
        "2016-07-02 20:52:40 12 1.99 www.store4.com";

    splitByLines(rawData).forEach(line -> mrDriver.withInput(new LongWritable(0), new Text(line)));

    mrDriver.withOutput(NullWritable.get(),
        new GroupedPaymentWritable(1, 106.72, getStoresFrom("www.store1.com", "www.store3.com")));
    mrDriver.withOutput(NullWritable.get(),
        new GroupedPaymentWritable(12, 185.66, getStoresFrom("www.store2.com", "www.store4.com")));
    mrDriver.withOutput(NullWritable.get(),
        new GroupedPaymentWritable(1123, 16.5, getStoresFrom("www.store1.com")));

    mrDriver.runTest();
  }

  private Set<String> getStoresFrom(String...stores) {
    return Arrays.stream(stores).collect(Collectors.toSet());
  }

}
