/**
 * Johnathan Lee
 * CSIS 335
 * Final Project
 * Due 12/12/18
 *
 * A classic game of pong, with a slight scoring tweak.
 */
package pong;

public enum BgSet {
  None(0),
  Scenery(1),
  Radiance(2);

  public final int val;

  BgSet(int val) {
    this.val = val;
  }

  public static BgSet from(int val) {
    if (val == Scenery.val)
      return Scenery;
    else if (val == Radiance.val)
      return Radiance;
    else
      return None;
  }
}
