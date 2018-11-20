public class Main {
  int AbsValue(int x) {
    if (x > 0)
      return x;
    else if(x < 0)
      return -x;
    else
      return 0;
  }

  boolean isValidSSN(String ssn) {
    return ssn.matches("[1-9]{3}-[1-9]{2}-[1-9]{4}");
  }
}
