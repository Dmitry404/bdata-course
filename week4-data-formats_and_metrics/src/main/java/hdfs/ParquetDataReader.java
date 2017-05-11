package hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import avro.data.Orders;

public class ParquetDataReader extends DataWriterReader {
  public ParquetDataReader(Configuration configuration, String path) {
    super(configuration, path);
  }

  public List<Orders> read() {
    try {
      ParquetReader<Orders> reader = AvroParquetReader.<Orders>builder(path).build();

      List<Orders> results = new ArrayList<>();
      Orders o;
      while((o = reader.read()) != null) {
        results.add(o);
      }
      return results;

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
