import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

  @Test
  @DisplayName("First myAbs test")
  void firstMyAbs() {
    int expected = 0;
    int num = 0;
    int result = Main.myAbs(num);
    assertEquals(expected, result);
  }
  @Test
  @DisplayName("Second myAbs test")
  void secondMyAbs() {
    int expected = 98;
    int num = 98;
    int result = Main.myAbs(num);
    assertEquals(expected, result);
  }
}