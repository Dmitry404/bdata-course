import org.apache.hadoop.io.Text;

import java.util.Comparator;
import java.util.List;

import hdfs.StocksCluster;
import utils.StocksDataPopulator;
import utils.StocksUtils;

public class EntryPoint {
  public static void main(String... args) {
    StocksCluster stockCluster = new StocksCluster();

    if (isDataLoadRequested(args)) {
      StocksDataPopulator dataPopulator = new StocksDataPopulator(stockCluster);
      StocksUtils.Source.getFilePaths().forEach(dataPopulator::populateDataFrom);
    } else {
      listAvailableStocks(stockCluster);
    }
  }

  private static boolean isDataLoadRequested(String... args) {
    return args.length == 1 && args[0].equals("--load_stocks_data_to_hdfs");
  }

  private static void listAvailableStocks(StocksCluster stockCluster) {
    List<String> years = stockCluster.listYears();
    years.sort(Comparator.reverseOrder());

    for (String year : years) {
      System.out.println(year + ":");
      for (String company : stockCluster.listCompaniesIn(year)) {
        System.out.println(" - " + company);
      }
    }
  }
}
