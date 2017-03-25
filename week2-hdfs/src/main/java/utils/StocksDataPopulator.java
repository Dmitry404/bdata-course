package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import hdfs.StocksCluster;

public class StocksDataPopulator {
  private StocksCluster cluster;

  public StocksDataPopulator(StocksCluster cluster) {
    this.cluster = cluster;
  }

  public void populateDataFrom(String fileName) {
    System.out.println("Populating Stocks data from: " + fileName);

    File companyStocksDataFile = new File(fileName);
    String company = getCompanyNameFrom(companyStocksDataFile);

    try (
        InputStream inputStream = new FileInputStream(new File(fileName));
        Reader reader = new InputStreamReader(inputStream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(reader);
    ) {
      bufferedReader.readLine(); // skip headers

      String currentYear = "";
      String stocksData = "";
      while (true) {
        String dayEntry = bufferedReader.readLine();
        if (dayEntry != null) {
          String year = StocksUtils.getYear(dayEntry);
          if (currentYear.equals(year) || currentYear.isEmpty()) {
            currentYear = year;

            stocksData += StocksUtils.packStockEntry(dayEntry);
          } else {
            cluster.writeStocksDataToHdfs(currentYear, company, stocksData);

            currentYear = year;
            stocksData = StocksUtils.packStockEntry(dayEntry);
          }
        } else {
          cluster.writeStocksDataToHdfs(currentYear, company, stocksData);

          break;
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private String getCompanyNameFrom(File companyStocksData) {
    return companyStocksData.getName().substring(0, companyStocksData.getName().lastIndexOf("."));
  }
}
