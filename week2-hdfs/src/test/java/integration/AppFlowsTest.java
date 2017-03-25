package integration;

import org.junit.Test;

public class AppFlowsTest {
  @Test
  public void listPath() throws Exception {
    // list years
    // list companies
  }

  @Test
  public void readPath() throws Exception {
    // read params - you need year and stock and a limit
    // locate a file on HDFS
    // read (limit*n) bytes
    // convert to string (simplified way, just a string, no objects/formatters required)
    // #Date, #Open, #High, #Low, #Close
  }

  @Test
  public void writePath() throws Exception {
    // read from file
    // reduce each line
    // if valid find the path
    // append to hdfs onto correct path, e.g. 2017/AAPL.dat
    // if not valid, just skip
  }
}
