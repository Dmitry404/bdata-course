import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import utils.StocksUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StocksUtilsTest {
  @Test
  public void lengthIsReduced() throws Exception {
    String given = "2017-03-24,820.080017,821.929993,808.890015,814.429993,1970900,814.429993\n";

    assertThat(StocksUtils.prepareString(given).length(), is(50));
  }

  @Test
  public void lengthIsCorrectlyReduced() throws Exception {
    String case1 = "2017-03-24,820.080017,821.929993,808.890015,814.429993,1970900,814.429993\n";
    String case2 = "2017-03-24,82.080017,82.929993,80.890015,81.429993,1970900,81.429993\n";
    String case3 = "2017-03-24,8.080017,8.929993,8.890015,8.429993,1970900,8.429993\n";

    assertThat("3.6f", StocksUtils.prepareString(case1), is("2017-03-24820.080017821.929993808.890015814.429993"));
    assertThat("2.6f", StocksUtils.prepareString(case2), is("2017-03-24 82.080017 82.929993 80.890015 81.429993"));
    assertThat("1.6f", StocksUtils.prepareString(case3), is("2017-03-24  8.080017  8.929993  8.890015  8.429993"));

  }

  @Test
  public void readDataSimulation() throws Exception {
    String readData =
        "2017-03-24,141.50,141.740005,140.350006,140.639999,22025300,140.639999\n" +
        "2017-03-23,141.259995,141.580002,140.610001,140.919998,20285700,140.919998\n" +
        "2016-03-22,139.850006,141.600006,139.759995,141.419998,25787600,141.419998\n" +
        "2015-03-21,142.110001,142.800003,139.729996,139.839996,39116800,139.839996\n" +
        "2015-03-20,140.399994,141.50,140.229996,141.460007,20213100,141.460007\n";

    Map<String, String> acc = new HashMap<>();

    for (String stockData : readData.split("\n")) {
      String year = StocksUtils.getYear(stockData);
      String prices = acc.getOrDefault(year, "");
      acc.put(year, prices + StocksUtils.prepareString(stockData));
    }

    assertThat("Length 2 of 2017:", acc.get("2017").length(), is(2 * 50));
    assertThat("Length 1 of 2016:", acc.get("2016").length(), is(50));
    assertThat("Length 2 of 2015:", acc.get("2015").length(), is(2 * 50));
  }

  @Test
  public void getAYear() throws Exception {
    String one = "2017-03-24,820.080017,821.929993,808.890015,814.429993,1970900,814.429993\n";
    String two = "2016-03-24,820.080017,821.929993,808.890015,814.429993,1970900,814.429993\n";

    assertThat(StocksUtils.getYear(one), is("2017"));
    assertThat(StocksUtils.getYear(two), is("2016"));
  }


}
