package hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;

import java.io.IOException;
import java.util.List;

import avro.data.Orders;

public class ParquetDataWriter extends DataWriterReader {
  public ParquetDataWriter(Configuration configuration, String path) {
    super(configuration, path);
  }

  public void write(List<Orders> ordersList) {
    try {
      ParquetWriter<Orders> writer = AvroParquetWriter.<Orders>builder(path)
          .withSchema(Orders.getClassSchema())
          .withConf(configuration)
          .build();

      for (Orders ordersItem : ordersList) {
        writer.write(ordersItem);
      }

      writer.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
