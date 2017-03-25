package hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StocksCluster {
  private Configuration configuration;

  public StocksCluster() {
    configuration = new Configuration();
    configuration.set("fs.defaultFS","hdfs://localhost:9009");
  }

  public void writeStocksDataToHdfs(String year, String company, String stocksData) {
    try (FileSystem fs = FileSystem.get(configuration);
         ByteArrayInputStream textDataInputStream = new ByteArrayInputStream(stocksData.getBytes());
         InputStream inputStream = new BufferedInputStream(textDataInputStream);
    ) {
      Path yearDir = new Path("/" + year);
      if (!fs.exists(yearDir)) {
        fs.mkdirs(yearDir);
      }
      Path stocksFile = new Path(yearDir, company + ".dat");
      FSDataOutputStream fileOutStream = fs.create(stocksFile);

      IOUtils.copyBytes(inputStream, fileOutStream, configuration);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
