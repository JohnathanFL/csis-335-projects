package pong.state;

import javafx.scene.control.Label;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;
import pong.Vec2;

import java.util.ArrayDeque;
import java.util.Deque;

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
  public static final double paddleWidth = 150;
  public static final double paddleHeight = 50;
  public static final Vec2 middle = new Vec2(1600*0.75 / 2, 900 / 2);

  public Vec2 pongVeloc = new Vec2(-1, -1);
  public double speedMult = 5.0;

  public int roundNum = 1;

  public int p1Score = 0, p2Score = 0;

  public StateInfo() {
    stateStack = new ArrayDeque<>();
  }

  public void init(Arc pong, Rectangle paddle1, Rectangle paddle2, Rectangle topGoal, Rectangle bottomGoal,
               Label p1ScoreLbl, Label p2ScoreLbl, Label goalText) {
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
