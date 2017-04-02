package sandbox;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class MapReduceGreetingsWordCount {
  private MapReduceDriver<NullWritable, Text, Text, LongWritable, Text, Text> mrDriver;

  @Before
  public void setUp() throws Exception {
    mrDriver = MapReduceDriver.newMapReduceDriver(new HelloMapper(), new HelloReducer());
  }

  @Test
  public void testGreetengsCount() throws Exception {
    //K1, V1 - input = nothing, text (two values separated with a comma)
    //K2, V2 - intermediate = text, 1 (word hit, 1)
    //K3, V3 - output = text, text (unique word, occurrence counter as a string)

    mrDriver.withInput(NullWritable.get(), new Text("hello, worm"));
    mrDriver.withInput(NullWritable.get(), new Text("hello, john"));
    mrDriver.withInput(NullWritable.get(), new Text("hello, worm"));
    mrDriver.withInput(NullWritable.get(), new Text("hello, jane"));
    mrDriver.withInput(NullWritable.get(), new Text("hello, john"));
    mrDriver.withInput(NullWritable.get(), new Text("hello, worm"));

    // note: output is sorted by key
    mrDriver.withOutput(new Text("jane"), new Text("1"));
    mrDriver.withOutput(new Text("john"), new Text("2"));
    mrDriver.withOutput(new Text("worm"), new Text("3"));

    mrDriver.runTest();
  }

  private class HelloMapper extends Mapper<NullWritable, Text, Text, LongWritable> {
    @Override
    protected void map(NullWritable key, Text value, Context context) throws IOException, InterruptedException {
      String[] greeting = value.toString().split(",");
      if (greeting.length >= 2) {
        context.write(new Text(greeting[1].trim()), new LongWritable(1));
      }
    }
  }

  private class HelloReducer extends Reducer<Text, LongWritable, Text, Text> {
    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
      int sum = 0;
      for (LongWritable num : values) {
        sum += num.get();
      }

      context.write(key, new Text(Long.toString(sum)));
    }
  }
}
