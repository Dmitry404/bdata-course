package hdfs;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.AvroFSInput;
import org.apache.hadoop.fs.FileContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import avro.data.Orders;

public class AvroDataReader extends DataWriterReader {
  public AvroDataReader(Configuration configuration, String path) {
    super(configuration, path);
  }

  public List<Orders> read() {
    try {
      DatumReader<Orders> datumReader = new SpecificDatumReader<>(Orders.class);
      AvroFSInput avroFSInput = new AvroFSInput(FileContext.getFileContext(configuration), path);
      DataFileReader<Orders> fileReader = new DataFileReader<>(avroFSInput, datumReader);

      List<Orders> results = new ArrayList<>();
      while(fileReader.hasNext()) {
        Orders o = fileReader.next();
        results.add(o);
      }
      return results;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
