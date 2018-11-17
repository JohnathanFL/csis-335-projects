package pong;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import sun.awt.AWTAccessor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Stack;

class Vec2 {
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
   * @param node
   */
  public Vec2(Node node) {
    this.x = AnchorPane.getLeftAnchor(node);
    this.y = AnchorPane.getBottomAnchor(node);
  }

  /**
   * Because for some reason Java won't let you overload. (Unless it's a string, but we don't talk about that)
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

  public void setConstraints(Node node) {
    AnchorPane.setLeftAnchor(node, this.x);
    AnchorPane.setBottomAnchor(node, this.y);
  }

  @Override
  public String toString() {
    return "Vec2{" +
            "x=" + x +
            ", y=" + y +
            '}';
  }

  double x, y;
}

public class Controller {
  static final double maxX = (1600 * 0.75); // Game scene takes 75% of the stage
  static final double paddleWidth = 150;
  static final double paddleHeight = 50;

  public enum Dir {
    Left, Right
  }

  public enum State {
    Playing, Paused, P1Scored, P2Scored, Quitting
  }

  Deque<State> stateStack = new ArrayDeque<>();
  boolean p2IsBot = true;

  int p1Score = 0, p2Score = 0;

  boolean[][] controlStates = {{false, false}, {false, false}}; // {paddle1: {leftCtrl, rightCtrl}, paddle2: ...

  public AnchorPane gameScene;
  public Circle pong;
  public Rectangle paddle1;
  public Rectangle paddle2;

  double speedMult = 5.0;
  Vec2 pongVeloc = new Vec2(-1,-1);


  public void movePaddle(Rectangle paddle, Dir dir) {
    Vec2 pos = new Vec2(paddle);
    if (dir == Dir.Right) {

      if (pos.x < (maxX - paddleWidth))
        pos.x += 2 * speedMult;
      else
        pos.x = maxX - paddleWidth;
    } else if (dir == Dir.Left){
      if (pos.x > 0)
        pos.x -= 2 * speedMult;
      else
        pos.x = 0.0;
    }

    pos.setConstraints(paddle);
  }

  public void setKeyStatesTo(boolean bool, KeyCode key) {
    switch (key) {
      case LEFT:
        controlStates[0][0] = bool;
        break;

      case RIGHT:
        controlStates[0][1] = bool;
        break;

      case A:
        controlStates[1][0] = bool;
        break;

      case D:
        controlStates[1][1] = bool;

      default:
        return;
    }

    //System.out.printf("Paddle1: %b, %b\nPaddle2: %b, %b\n\n", controlStates[0][0], controlStates[0][1], controlStates[1][0], controlStates[1][1]);
  }

  public void setupHandlers() {
    gameScene.getScene().setOnKeyReleased(key -> setKeyStatesTo(false, key.getCode()));
    gameScene.getScene().setOnKeyPressed(key -> {
      setKeyStatesTo(true, key.getCode());

      if (key.getCode() == KeyCode.ESCAPE) {
        if (stateStack.getFirst() != State.Paused) // Don't push infinite pauses onto the stack.
          stateStack.push(State.Paused);
        else
          stateStack.pop();
      }
    });
  }

  public void handlePaused() {

  }


  Integer handleAIInterval = 0;
  private void handleAI() {
    // Only handle every 100 ticks
    if(handleAIInterval++ < 10)
      return;
    else
      handleAIInterval = 0;

    boolean[] ctrls = controlStates[1]; // P2 is index 1
    double xPos = AnchorPane.getLeftAnchor(paddle2),
            pongXPos = AnchorPane.getLeftAnchor(pong);

    if(pongXPos <  xPos) {
      ctrls[0] = true;
      ctrls[1] = false;
    }
    else {
      ctrls[1] = true;
      ctrls[0] = false;
    }
  }

  public void handlePlaying() {
    if(p2IsBot)
      handleAI();

    if (controlStates[0][0])
      movePaddle(paddle1, Dir.Left);

    if (controlStates[0][1])
      movePaddle(paddle1, Dir.Right);

    if (controlStates[1][1])
      movePaddle(paddle2, Dir.Right);

    if (controlStates[1][0])
      movePaddle(paddle2, Dir.Left);

    Vec2 pongPos = new Vec2(pong);

    //System.out.println("From " + pongPos);

    if(pongPos.x <= 1 || pongPos.x >= (maxX - 1.0))
      pongVeloc.x *= -1;

    if(pongPos.y >= 450) { // Top half of screen. Could be colliding with P2
      if(pong.getBoundsInParent().intersects(paddle2.getBoundsInParent())) {
        System.out.println("Hit P2");
        pongVeloc.y *= -1;
        pongVeloc.mult(1.5);
      }
    } else { // Bottom half of screen. Could be colliding with P1
      if(pong.getBoundsInParent().intersects(paddle1.getBoundsInParent())) {
        System.out.println("Hit P1");
        pongVeloc.y *= -1;
        pongVeloc.mult(1.5);
      }
    }

    pongPos.add(pongVeloc);


    pongPos.setConstraints(pong);

    //System.out.println("To " + pongPos);


  }

  public void handleWin(State which) {

  }

  public void initialize() {
    stateStack.push(State.Quitting); // Should be the bottom-most state, so we can pop back to it.
    stateStack.push(State.Playing);

    Timeline tick = TimelineBuilder
        .create()
        .keyFrames(
            new KeyFrame(
                new Duration(16.667), // 60 FPS
                ev -> {
                  State curState = stateStack.getFirst();
                  //System.out.println(stateStack.getFirst());
                  switch (curState) {
                    case Quitting:
                      // TODO: Cleanup if needed
                      break;

                    case Playing:
                      handlePlaying();
                      break;

                    case Paused:
                      handlePaused();
                      break;

                    case P1Scored:
                    case P2Scored:
                      handleWin(curState);
                      break;

                    default:
                      break;
                  }
                }
            )
        )
        .cycleCount(Timeline.INDEFINITE)
        .build();

    tick.play();

  }
}
