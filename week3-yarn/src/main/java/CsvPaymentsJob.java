import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import payments.mapreduce.PaymentsMR;
import payments.mapreduce.io.PaymentWritable;
import payments.mapreduce.io.PaymentsOutputFormat;

public class CsvPaymentsJob extends Configured implements Tool {
  public static void main(String[] args) throws Exception {
    int exitCode = ToolRunner.run(new Configuration(), new CsvPaymentsJob(), args);
    System.exit(exitCode);
  }

  @Override
  public int run(String[] args) throws Exception {
    Configuration conf = getConf();
    Job job =Job.getInstance(conf, PaymentsMR.JOB_NAME);
    job.setJarByClass(CsvPaymentsJob.class);

    job.setMapperClass(PaymentsMR.PaymentsMapper.class);
    job.setReducerClass(PaymentsMR.PaymentsReducer.class);

    job.setOutputKeyClass(LongWritable.class);
    job.setOutputValueClass(PaymentWritable.class);

    FileInputFormat.addInputPath(job, new Path("/input.dat"));

    job.setOutputFormatClass(PaymentsOutputFormat.class);
    TextOutputFormat.setOutputPath(job, new Path("/output"));

    return job.waitForCompletion(true) ? 0 : 1;
  }

  public Configuration getConf() {
    Configuration configuration = new Configuration();
    configuration.set("fs.defaultFS","hdfs://hadoop-master-node:9009");

    return configuration;
  }
}
