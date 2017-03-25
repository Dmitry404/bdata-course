import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import hdfs.StocksCluster;
import utils.StocksDataPopulator;
import utils.StocksUtils;

public class EntryPoint {
  public static void main(String... args) {
    StocksCluster stockCluster = new StocksCluster(getHostFrom(args));

    if (isDataLoadRequested(args)) {
      StocksDataPopulator dataPopulator = new StocksDataPopulator(stockCluster);
      StocksUtils.Source.getFilePaths().forEach(dataPopulator::populateDataFrom);
    } else if (isStocksReadRequested(args)) {
      printStocksFor(stockCluster, args[3], args[4]);
    } else if (isStocksListingRequested(args)) {
      listAvailableStocks(stockCluster);
    } else {
      System.out.println("No valid params were passed:");
      Arrays.stream(args).forEach(System.out::println);
    }
  }

  private static void printStocksFor(StocksCluster stockCluster, String company, String year) {
    List<String> stocksData = stockCluster.readStocksData(company, year);

    if (stocksData.size() > 0) {
      System.out.println("  #Date   |  #Open   |  #High   |   #Low   |  #Close");
      stocksData.stream()
          .map(s -> s.replace(",", "|"))
          .forEach(System.out::println);
    } else {
      System.out.printf("No Stock data found for %s in %s%n", company, year);
    }
  }

  private static void listAvailableStocks(StocksCluster stockCluster) {
    System.out.println("Listing all available Companies per Year");

    List<String> years = stockCluster.listYears();
    years.sort(Comparator.reverseOrder());

    for (String year : years) {
      System.out.println(year + ":");
      for (String company : stockCluster.listCompaniesIn(year)) {
        System.out.println(" - " + company);
      }
    }
  }

  private static String getHostFrom(String... args) {
    return args.length < 1 ? "localhost" : args[1];
  }

  private static boolean isDataLoadRequested(String... args) {
    return args.length >= 2 && args[0].equals("--load_stocks_data_to");
  }

  private static boolean isStocksReadRequested(String... args) {
    return args.length >= 4 && args[0].equals("--host") && args[2].equals("--read");
  }

  private static boolean isStocksListingRequested(String... args) {
    return args.length >= 3 && args[0].equals("--host") && args[2].equals("--list");
  }
}
