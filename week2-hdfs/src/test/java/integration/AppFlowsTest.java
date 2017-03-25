package integration;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hdfs.StocksCluster;
import utils.StocksDataPopulator;
import utils.StocksUtils;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;


public class AppFlowsTest {
  @Test
  public void loadStocksSimulation_withRealData() throws Exception {
    FakeStocksCluster clusterMock = new FakeStocksCluster();
    StocksDataPopulator dataPopulator = new StocksDataPopulator(clusterMock);

    StocksUtils.Source.getFilePaths().forEach(dataPopulator::populateDataFrom);

    assertThat("Years written:", clusterMock.getYears(), containsInAnyOrder(
      "2017", "2016", "2015", "2014", "2013", "2012", "2011", "2010",
        "2009", "2008", "2007", "2006", "2005", "2004", "2003", "2002", "2001",
        "2000", "1999", "1998", "1997", "1996", "1995", "1994", "1993", "1992",
        "1991", "1990", "1989", "1988", "1987", "1986", "1985", "1984", "1983",
        "1982", "1981", "1980"
        ));
    assertThat("Companies written:", clusterMock.getCompanies(), containsInAnyOrder(
        "GOOG", "FB", "AAPL", "MSFT", "RHT"
    ));
  }

  @Test
  public void loadStocks_toHDFS() throws Exception {
    StocksCluster cluster = new StocksCluster();
    StocksDataPopulator dataPopulator = new StocksDataPopulator(cluster);

    StocksUtils.Source.getFilePaths().forEach(dataPopulator::populateDataFrom);
  }

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


  private class FakeStocksCluster extends StocksCluster {
    private Set<String> yearsWritten = new HashSet<>();
    private Set<String> companiesWritten = new HashSet<>();

    @Override
    public void writeStocksDataToHdfs(String year, String company, String stocksData) {
      yearsWritten.add(year);
      companiesWritten.add(company);
    }

    public Set<String> getYears() {
      return yearsWritten;
    }

    public Set<String> getCompanies() {
      return companiesWritten;
    }
  }
}
