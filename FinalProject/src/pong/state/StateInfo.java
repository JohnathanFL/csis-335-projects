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
  public Deque<State> stateStack;
  public Arc pong = null;
  public Rectangle paddle1 = null, paddle2 = null;
  public Rectangle topGoal = null, bottomGoal = null;
  public Label p1ScoreLbl = null;
  public Label p2ScoreLbl = null;
  public Label goalText = null;

  public static final double maxX = (1600 * 0.75); // Game scene takes 75% of the stage
  public static final Vec2 middle = new Vec2(1600*0.75 / 2, 900 / 2);
  public static final Vec2 paddleSize = new Vec2(150, 25);

  Map<String, Boolean> controls;

  public Vec2 pongVeloc = new Vec2(-1, -1), pongPos = middle.clone();
  public double speedMult = 5.0;

  public int roundNum = 1;

  public int p1Score = 0, p2Score = 0;

  public StateInfo() {
    stateStack = new ArrayDeque<>();
  }

  public void init(Map<String, Boolean> controls, Arc pong, Rectangle paddle1, Rectangle paddle2, Rectangle topGoal, Rectangle bottomGoal,
               Label p1ScoreLbl, Label p2ScoreLbl, Label goalText) {
    this.controls = controls;
    this.pong = pong;
    this.paddle1 = paddle1;
    this.paddle2 = paddle2;
    this.topGoal = topGoal;
    this.bottomGoal = bottomGoal;
    this.p1ScoreLbl = p1ScoreLbl;
    this.p2ScoreLbl = p2ScoreLbl;
    this.goalText = goalText;
  }
}
