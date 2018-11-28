package pong;

public enum IconSet {
  Standard(1),
  Monster(2),
  Suave(3),
  Kitten(4);

  public final int val;
  IconSet(int val) {this.val = val;}

  public static IconSet from(int val) {
    if(val == Monster.val)
      return Monster;
    else if(val == Suave.val)
      return Suave;
    else if(val == Kitten.val)
      return Kitten;
    else
      return Standard;
  }
}
