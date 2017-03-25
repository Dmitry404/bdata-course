package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StocksUtils {
  private static final int NUMBER_OF_COLUMNS = 5;
  private static final int COLUMN_WIDTH = 10;

  static public String packStockEntry(String given) {
    String[] elems = given.split(",");
    for(int i = 1; i < NUMBER_OF_COLUMNS; i++) {
      elems[i] = String.format("%10.6f", Float.parseFloat(elems[i]));
    }
    return String.join("", Arrays.copyOf(elems, NUMBER_OF_COLUMNS));
  }

  public static String getYear(String given) {
    return given.substring(0, NUMBER_OF_COLUMNS - 1);
  }

  public static List<String> unpackStockEntry(String given) {
    List<String> result = new ArrayList<>();
    int pos = 0;
    while (pos < given.length()) {
      String rawString = given.substring(pos, NUMBER_OF_COLUMNS * COLUMN_WIDTH + pos);
      StringBuilder entryCvsString = new StringBuilder();
      int internalPos = 0;

      while (internalPos < NUMBER_OF_COLUMNS * COLUMN_WIDTH) {
        entryCvsString.append(rawString.substring(internalPos, COLUMN_WIDTH + internalPos));
        entryCvsString.append(",");

        internalPos += COLUMN_WIDTH;
      }
      result.add(entryCvsString.toString().substring(0, entryCvsString.length() - 1)); // trim the last ','
      pos += NUMBER_OF_COLUMNS * COLUMN_WIDTH;
    }
    return result;
  }

  public static class Source {
    final static String STOCKS_DATA_DIR = "stocks";

    public static List<String> getFilePaths() {
      File stocksDataDir = new File(STOCKS_DATA_DIR);
      String[] files = stocksDataDir.list((dir, name) -> name.contains(".csv"));

      return files != null ? pathifyFiles(files) : Collections.emptyList();
    }

    private static List<String> pathifyFiles(String[] files) {
      return Arrays.stream(files)
          .map(fileName -> new File(STOCKS_DATA_DIR + File.separator + fileName).getAbsolutePath())
          .collect(Collectors.toList());
    }
  }
}
