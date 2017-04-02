import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.junit.Before;
import org.junit.Test;

import payments.PaymentsMapper;
import payments.PaymentsReducer;

public class PaymentsMapReduceTests {
  private MapReduceDriver<NullWritable, Text, LongWritable, Text, NullWritable, Text> mrDriver;

  @Before
  public void setUp() throws Exception {
    mrDriver = MapReduceDriver.newMapReduceDriver(new PaymentsMapper(), new PaymentsReducer());
  }

  @Test
  public void testPayments() throws Exception {
    mrDriver.withInput(NullWritable.get(), new Text("2016-07-02 20:52:39 1 12.01 www.store1.com"));
    mrDriver.withInput(NullWritable.get(), new Text("2016-07-02 20:52:39 2 1.75 www.store1.com"));
    mrDriver.withInput(NullWritable.get(), new Text("2016-07-02 20:52:39 2 4.05 www.store2.com"));

    mrDriver.withOutput(NullWritable.get(), new Text("{\"id\":1,\"total\":12.01,\"stores\":[\"www.store1.com\"]}"));
    mrDriver.withOutput(NullWritable.get(), new Text("{\"id\":2,\"total\":5.80,\"stores\":[\"www.store1.com\",\"www.store2.com\"]}"));

    mrDriver.runTest();
  }

}
