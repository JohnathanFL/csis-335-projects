import com.sun.org.apache.xpath.internal.operations.Bool;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.lang.model.util.ElementScanner6;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

@RunWith(Parameterized.class)
public class SSNCheckerTest extends TestCase {

  public String ssn;
  public String expected;

  public SSNCheckerTest(String ssn, String expected) {
    this.ssn = ssn;
    this.expected = expected;
  }

  private Main main = new Main();

  private static final String FALSE = "FALSE", TRUE = "TRUE";

  @Parameterized.Parameters
  public static Iterable<String[]> params() {
      return Arrays.asList(new String[][]{
          {"00-000-0000", FALSE},
          {"113-14-1244", TRUE},
          {"sfsdjhskjdf", FALSE}});
  }


  @Test
  public void testIsValidSSN() {
    System.out.println(expected);
    System.out.println("Input: " + ssn + " Expected: " + expected + " Got: " + main.isValidSSN(ssn));

    boolean ex = (expected == TRUE);
    assertEquals(ex, main.isValidSSN(ssn));
  }
}