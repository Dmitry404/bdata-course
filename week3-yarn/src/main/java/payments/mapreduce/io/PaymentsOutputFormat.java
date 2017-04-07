package payments.mapreduce.io;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.InvalidJobConfException;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.OutputCommitter;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PaymentsOutputFormat extends OutputFormat<LongWritable, GroupedPaymentWritable> {
  @Override
  public RecordWriter getRecordWriter(TaskAttemptContext job) throws IOException, InterruptedException {
    return new PaymentsGroupRecordWriter(getOutputDirectoryPath(job), job.getConfiguration());
  }

  @Override
  public void checkOutputSpecs(JobContext context) throws IOException, InterruptedException {
    if (getOutputDirectoryPath(context) == null) {
      throw new InvalidJobConfException("No output directory set");
    }
  }

  private static Path getOutputDirectoryPath(JobContext context) {
    String name = context.getConfiguration().get(FileOutputFormat.OUTDIR);
    return name != null ? new Path(name) : null;
  }

  @Override
  public OutputCommitter getOutputCommitter(TaskAttemptContext context) throws IOException, InterruptedException {
    return new FileOutputCommitter(getOutputDirectoryPath(context), context);
  }

  static class PaymentsGroupRecordWriter extends RecordWriter<LongWritable, GroupedPaymentWritable> {
    private static final String OUTPUT_FILE_NAME = "part-r-00000";

    private Path outputFilePath;
    private Configuration configuration;

    PaymentsGroupRecordWriter(Path outputPath, Configuration configuration) {
      this.configuration = configuration;

      outputFilePath = new Path(outputPath, OUTPUT_FILE_NAME);
    }

    @Override
    public void write(LongWritable key, GroupedPaymentWritable value) throws IOException, InterruptedException {
      FileSystem fs = FileSystem.get(configuration);
      FSDataOutputStream fileOutStream;

      if (fs.exists(outputFilePath)) {
        fileOutStream = fs.append(outputFilePath);
      } else {
        fileOutStream = fs.create(outputFilePath);
      }

      ByteArrayInputStream textDataInputStream = new ByteArrayInputStream(getCsvText(value).getBytes());
      InputStream inputStream = new BufferedInputStream(textDataInputStream);

      IOUtils.copyBytes(inputStream, fileOutStream, configuration);
    }

    @Override
    public void close(TaskAttemptContext context) throws IOException, InterruptedException {
    }
  }

  private static String getCsvText(GroupedPaymentWritable value) {
    return value.getPaymentId() + "," + value.getTotal() + "," + StringUtils.join(value.getStores(), ":") + "\n";
  }
}
