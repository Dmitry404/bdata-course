package hdfs;

import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import java.io.IOException;
import java.util.List;

import avro.data.Orders;

public class AvroDataWriter extends DataWriterReader {

  public AvroDataWriter(Configuration configuration, String path) {
    super(configuration, path);
  }

  public void write(List<Orders> ordersList) {
    DatumWriter<Orders> datumWriter = new SpecificDatumWriter<>(Orders.class);
    DataFileWriter<Orders> fileWriter = new DataFileWriter<>(datumWriter);

    try (FileSystem fs = FileSystem.get(configuration)) {
      fileWriter.create(Orders.getClassSchema(), fs.create(path));

      for (Orders ordersItem : ordersList) {
        fileWriter.append(ordersItem);
      };

      fileWriter.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
