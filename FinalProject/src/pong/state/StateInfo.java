package pong.state;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.scene.control.Label;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;
import pong.Vec2;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

// I know, I know. Global state. In my defense, JavaFX ain't exactly built properly for a game to begin with.
public class StateInfo {
  public static final Vec2 extents = new Vec2(1600 * 0.75, 900);
  public static final Vec2 middle = new Vec2(extents.x / 2, extents.y / 2);
  public static final Vec2 paddleSize = new Vec2(150, 25);
  public static final Vec2 pongSize = new Vec2(25, 25);
  public Deque<State> stateStack;

  // JavaFX stuff
  public Label p1ScoreLbl = null;
  public Label p2ScoreLbl = null;
  public Label goalText = null;
  public double pongAngle = 360.0;
  public Vec2 pongVeloc,
          pongPos,
          paddle1Pos,
          paddle2Pos;
  public double speedMult = 5.0;
  public int roundNum = 0;
  public int p1Score = 0, p2Score = 0;
  Map<String, Boolean> controls;

  public int p1ID, p2ID;

  public StateInfo() {
    stateStack = new ArrayDeque<>();
    resetPos();
  }

  public void resetPos() {
    pongPos = new Vec2(middle);
    paddle1Pos = new Vec2(middle.x, extents.y - (paddleSize.y + 15));
    paddle2Pos = new Vec2(middle.x, 0 + (paddleSize.y + 15));

    do {
      // -0.5 brings it into negative, *2.0 brings it back to a full [-1, 1] range
      pongVeloc = new Vec2((Math.random() - 0.5) * 2.0, (Math.random() - 0.5) * 2.0);
      pongVeloc.normalize();
      pongVeloc.mult(2.0);
      if (pongVeloc.length() >= 8.0)
        pongVeloc.mult(1 / 2.0);
    } while(Math.abs(pongVeloc.y) < 0.5);
  }

  public void init(Map<String, Boolean> controls, Label p1ScoreLbl, Label p2ScoreLbl, Label goalText) {
    this.controls = controls;
    this.p1ScoreLbl = p1ScoreLbl;
    this.p2ScoreLbl = p2ScoreLbl;
    this.goalText = goalText;
  }
}
