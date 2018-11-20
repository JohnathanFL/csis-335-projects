package pong;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 * Basic Vec2 implementation. Only supports the ops needed for pong
 */
public class Vec2 {
  public double x, y;

  public Vec2(Vec2 rhs) {
    this.x = rhs.x;
    this.y = rhs.y;
  }

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
   *
   * @param rhs Right side of the equation vec1 + vec2
   */
  public void add(Vec2 rhs) {
    this.x += rhs.x;
    this.y += rhs.y;

  }

  public void mult(double coeff) {
    this.x *= coeff;
    this.y *= coeff;
  }

  public void normalize() {
    double length = this.length();

    this.x /= length;
    this.y /= length;
  }

  public double length() {
    return Math.sqrt(this.x * this.x + this.y * this.y);
  }

  public double get360Angle() {
    // Angle in the first quadrant. ONLY IN THE FIRST QUADRANT, DUMMY. REMEMBER YOUR TRIG
    double angle = Math.toDegrees(Math.atan2(this.y, this.x));
    return angle;
  }

  public void setConstraints(Node node) {
    AnchorPane.setLeftAnchor(node, this.x);
    AnchorPane.setBottomAnchor(node, this.y);
  }

  public Vec2 clone() {
    return new Vec2(this);
  }

  @Override
  public String toString() {
    return "Vec2{" +
            "x=" + x +
            ", y=" + y +
            '}';
  }
}
