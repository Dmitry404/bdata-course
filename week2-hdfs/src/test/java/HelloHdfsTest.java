import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.Test;

import org.apache.hadoop.conf.Configuration;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

public class HelloHdfsTest {
  @Test
  public void name() throws Exception {

    Configuration conf = new Configuration();
    conf.set("fs.defaultFS","hdfs://localhost:9009");

    FileSystem fs = FileSystem.get(conf);

    for (int i = 0; i < 10; i++) {
      fs.create(new Path("test_" + i), (short) 2);
    }
    FileStatus[] files = fs.listStatus( new Path("/"));
    for (FileStatus file : files ){
      System.out.println(file.getPath().getName());
    }

    System.out.println(fs.getDefaultReplication(new Path("/test")));

    String text = "olol stas voditel' nlo";
    InputStream in = new BufferedInputStream(new ByteArrayInputStream(text.getBytes()));
    FSDataOutputStream out = fs.create(new Path("/test.txt"));
    IOUtils.copyBytes(in, out, conf);
  }


}