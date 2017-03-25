package utils;

import java.util.Arrays;

public class StocksUtils {
  static public String prepareString(String given) {
    String[] elems = given.split(",");
    for(int i = 1; i < 5; i++) {
      elems[i] = String.format("%10.6f", Float.parseFloat(elems[i]));
    }
    return String.join("", Arrays.copyOf(elems, 5));
  }

  static public String getYear(String given) {
    return given.substring(0, 4);
  }
}
