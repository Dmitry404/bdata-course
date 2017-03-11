package arguments;

import java.util.HashMap;
import java.util.Map;

public class ArgumentsParser {
  private Map<String, String> argumentsMap = new HashMap<>();

  public ArgumentsParser(String[] args) {
    for (int i = 0; i < args.length; i++) {
      String arg = args[i];
      if (arg.startsWith("--") && args.length > i + 1) {
        String value = args[i + 1];
        if (value.startsWith("--")) {
          throw new RuntimeException("missing value of " + arg);
        }
        argumentsMap.put(arg, value);
      }
    }
  }

  public String get(String paramName) {
    if (!argumentsMap.containsKey("--" + paramName)) {
      throw new RuntimeException(paramName + " is required");
    }
    return argumentsMap.get("--" + paramName);
  }

  public void addDefault(String arg, String value) {
    argumentsMap.put("--" + arg, value);
  }

  @Override
  public String toString() {
    StringBuilder out = new StringBuilder("Arguments{\n");
    argumentsMap.forEach(
        (key, value) -> {
          out.append(key);
          out.append("=");
          out.append(value);
          out.append("\n");
        }
    );

    out.append("}");

    return out.toString();
  }
}
