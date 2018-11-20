import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class AbsValueCheckerTest extends TestCase {
  @Parameterized.Parameter(0)
  public int inputNum;
  @Parameterized.Parameter(1)
  public int expected;
  private Main main = new Main();

  @Parameterized.Parameters
  public static Collection myNums() {
      return Arrays.asList(new Object[][]{
          {2,2},
          {0,0},
          {-4, 4}
      });
  }


  @Test
  public void testAbsValue() {
    System.out.println("Input: " + inputNum + " Expected: " + expected+  " Got: " + main.AbsValue(inputNum));
    assertEquals(expected, main.AbsValue(inputNum));
  }
}