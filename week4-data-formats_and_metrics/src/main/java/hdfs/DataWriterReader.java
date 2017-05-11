package hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

abstract class DataWriterReader {
  Configuration configuration;
  Path path;

  DataWriterReader(Configuration configuration, String path) {
    this.configuration = configuration;
    this.path = new Path(path);
  }
}
