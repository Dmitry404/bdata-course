import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import payments.mapreduce.SimplePaymentsJobConf;

public class JsonPaymentsJobApp {
  public static void main(String[] args) throws Exception {
    SimplePaymentsJobConf jobConf = new SimplePaymentsJobConf(new Configuration(true));

    FileInputFormat.addInputPath(jobConf.getJob(), new Path("/input.dat"));
    FileOutputFormat.setOutputPath(jobConf.getJob(), new Path("/output"));

    int exitCode = jobConf.getJob().waitForCompletion(true) ? 0 : 1;
    System.exit(exitCode);
  }
}
