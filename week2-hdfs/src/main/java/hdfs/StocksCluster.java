package hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import utils.StocksUtils;

public class StocksCluster {
  private Configuration configuration;

  public StocksCluster() {
    this("localhost");
  }

  public StocksCluster(String host) {
    System.out.println("Configuring hdfs cluster with host: " + host);

    configuration = new Configuration();
    configuration.set("fs.defaultFS","hdfs://" + host + ":9009");
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

  public List<String> listYears() {
    try (FileSystem fs = FileSystem.get(configuration)) {
      return Arrays.stream(fs.listStatus(new Path("/")))
          .filter(FileStatus::isDirectory)
          .map(fStat -> fStat.getPath().getName())
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public List<String> listCompaniesIn(String year) {
    try (FileSystem fs = FileSystem.get(configuration)) {
       return Arrays.stream(fs.listStatus(new Path("/" + year)))
          .map(fStat -> fStat.getPath().getName())
          .map(name -> name.substring(0, name.lastIndexOf(".")))
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public List<String> readStocksData(String company, String year) {
    System.out.println("Reading Stocks data of: " + year + "/" + company);

    try (FileSystem fs = FileSystem.get(configuration);
         ByteArrayOutputStream out = new ByteArrayOutputStream();
    ) {
      Path stocksDataFile = new Path(String.format("/%s/%s.dat", year, company));
      if (fs.exists(stocksDataFile)) {
        FSDataInputStream in = fs.open(stocksDataFile);

        IOUtils.copyBytes(in, out, configuration);

        return StocksUtils.unpackStockEntry(new String(out.toByteArray()));
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return Collections.emptyList();
  }
}
