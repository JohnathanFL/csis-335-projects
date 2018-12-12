/**
 * Johnathan Lee
 * CSIS 335
 * Final Project
 * Due 12/12/18
 *
 * A classic game of pong, with a slight scoring tweak.
 */
package pong;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

/**
 * Basic Vec2 implementation. Only supports the ops needed for pong
 */
public class Vec2 {
  public double x, y;

  /**
   * Copy constructor. You can take the man out of the C++, but you can't take the C++ out of the man.
   *
   * @param rhs
   */
  public Vec2(Vec2 rhs) {
    this.x = rhs.x;
    this.y = rhs.y;
  }

  /**
   * Explicit constructor
   *
   * @param x The X component
   * @param y The Y component
   */
  public Vec2(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Get from anchor pane constraints
   *
   * @param node
   */
  public Vec2(Node node) {
    this.x = AnchorPane.getLeftAnchor(node);
    this.y = AnchorPane.getBottomAnchor(node);
  }

  /**
   * Because for some reason Java won't let you overload. (Unless it's a string, but we don't talk about that)
   * Adds rhs to this. DOES NOT RETURN ANYTHING, ONLY CHANGES THIS OBJECT. If you want to have it in another object, clone then do your ops
   *
   * @param rhs Right side of the equation vec1 + vec2
   */
  public void add(Vec2 rhs) {
    this.x += rhs.x;
    this.y += rhs.y;

  }

  /**
   * Multiplies both components by a scalar
   * @param coeff The number to mult by
   */
  public void mult(double coeff) {
    this.x *= coeff;
    this.y *= coeff;
  }

  /**
   * Turns this Vec2 into a unit vector (Same dir, length of 1)
   */
  public void normalize() {
    double length = this.length();

    this.x /= length;
    this.y /= length;
  }

  /**
   * @return The euclidean distance from origin to components, assuming a 2-dimensional, euclidean space is present.
   * Or, in normal person speak, the LENGTH of the vector.
   * Really, what did you expect here?
   */
  public double length() {
    return Math.sqrt(this.x * this.x + this.y * this.y);
  }

  /**
   * @return The angle (in deg) this vector makes with the vector [1, 0]
   */
  public double get360Angle() {
    double angle = -Math.toDegrees(Math.atan2(this.y, this.x));
    return angle;
  }

  /**
   * Sets the AnchcorPane left/bottom constraints to match this Vector's components.
   * @deprecated No longer using AnchorPanes, I'm just leaving this in here for reference.
   * @param node The node whose constraints are to be set.
   */
  public void setConstraints(Node node) {
    AnchorPane.setLeftAnchor(node, this.x);
    AnchorPane.setBottomAnchor(node, this.y);
  }

  /**
   * sugar for copy constructor
   * @return A new Vector with the same components.
   */
  public Vec2 clone() {
    return new Vec2(this);
  }

  public int quadrant() {
    double angle = this.get360Angle();
    if(angle > 0.0 && angle < 90.0)
      return 1;
    else if(angle > 90.0 && angle < 180.0)
      return 2;
    else if(angle < 0.0 && angle < -90.0)
      return 3;
    else if(angle >= -180.0 && angle < -90.0)
      return 4;
    else return Integer.MAX_VALUE;
  }

  /**
   * @return '<X, Y>'
   */
  @Override
  public String toString() {
    return String.format("<%f, %f>", this.x, this.y);
  }
}
