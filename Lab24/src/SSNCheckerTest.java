import com.sun.org.apache.xpath.internal.operations.Bool;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class SSNCheckerTest extends TestCase {

  @Parameterized.Parameter(0)
  public Object ssn;
  @Parameterized.Parameter(1)
  public Object expected;
  private Main main = new Main();

  @Parameterized.Parameters
  public static Map<String, Boolean> params() {

        {"00-000-0000", false},
        {"11-124-1244",true},
        {"sfsdjhskjdf", false}
    });
  }


  @Test
  public void testIsValidSSN() {
    System.out.println("Input: " + ssn + " Expected: " + expected + " Got: " + main.isValidSSN((String)ssn));
    assertEquals(expected, main.isValidSSN((String)ssn));
  }
}