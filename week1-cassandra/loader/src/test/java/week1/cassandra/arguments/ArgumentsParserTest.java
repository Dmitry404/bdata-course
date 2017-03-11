package week1.cassandra.arguments;

import org.junit.Test;

import arguments.ArgumentsParser;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ArgumentsParserTest {
  @Test
  public void testHostIsPresent() throws Exception {
    String[] args = "--host 127.0.0.1".split(" ");

    ArgumentsParser arguments = new ArgumentsParser(args);

    assertThat(arguments.get("host"), is("127.0.0.1"));
  }

  @Test
  public void testArgumentIsNotPresent_fallbackValueIsPresented() throws Exception {
    ArgumentsParser arguments = new ArgumentsParser(new String[0]);
    arguments.addDefault("host", "127.0.0.1");

    assertThat(arguments.get("host"), is("127.0.0.1"));
  }

  @Test (expected = RuntimeException.class)
  public void testArgumentIsNotPresent_noFallbackValue_emptyArgs() throws Exception {
    ArgumentsParser arguments = new ArgumentsParser(new String[0]);

    arguments.get("host");
  }

  @Test (expected = RuntimeException.class)
  public void testArgumentIsNotPresent_noFallbackValue_someArgsThere() throws Exception {
    String[] args = "--parallelism 2 --something fakeValue".split(" ");
    ArgumentsParser arguments = new ArgumentsParser(args);

    arguments.get("host");
  }

  @Test (expected = RuntimeException.class)
  public void testArgumentValueIsLost() throws Exception {
    String[] args = "--host".split(" ");
    ArgumentsParser arguments = new ArgumentsParser(args);

    arguments.get("host");
  }

  @Test (expected = RuntimeException.class)
  public void testArgumentValueIsLost_twoArgumentsCase() throws Exception {
    String[] args = "--host --parallelism".split(" ");
    ArgumentsParser arguments = new ArgumentsParser(args);

    arguments.get("host");
  }

  @Test (expected = RuntimeException.class)
  public void testArgumentValueIsLost_ThreeArgumentsCase() throws Exception {
    String[] args = "--host 127.0.0.1 --parallelism --something fakeValue".split(" ");
    ArgumentsParser arguments = new ArgumentsParser(args);

    arguments.get("parallelism");
  }

  @Test
  public void nonDefaultValueShouldBeTaken_ifArgPresented() throws Exception {
    String[] args = "--host 172.17.0.2".split(" ");
    ArgumentsParser arguments = new ArgumentsParser(args);

    arguments.addDefault("host", "127.0.0.1");

    assertThat(arguments.get("host"), is("172.17.0.2"));
  }
}
