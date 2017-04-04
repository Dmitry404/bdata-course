import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import payments.mapreduce.simple.SimplePaymentsJob;

public class SimplePaymentsJobApp {
  public static void main(String[] args) throws Exception {
    SimplePaymentsJob jobConf = new SimplePaymentsJob(new Configuration(true));

    FileInputFormat.addInputPath(jobConf.getJob(), new Path("/input.dat"));
    FileOutputFormat.setOutputPath(jobConf.getJob(), new Path("/output"));

    jobConf.getJob().waitForCompletion(true);
  }
}
