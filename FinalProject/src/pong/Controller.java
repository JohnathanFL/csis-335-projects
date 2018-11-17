package pong;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Stack;

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

  boolean[][] controlStates = {{false, false}, {false, false}}; // {paddle1: {leftCtrl, rightCtrl}, paddle2: ...

  public AnchorPane gameScene;
  public Circle pong;
  public Rectangle paddle1;
  public Rectangle paddle2;

  double speedMult = 5.0;

  public void movePaddle(Rectangle paddle, Dir dir) {
    double xPos = AnchorPane.getLeftAnchor(paddle);
    if (dir == Dir.Right) {

      if (xPos < (maxX - paddleWidth))
        AnchorPane.setLeftAnchor(paddle, xPos + 2 * speedMult);
      else
        AnchorPane.setLeftAnchor(paddle, maxX - paddleWidth);
    } else {
      if (xPos > 0)
        AnchorPane.setLeftAnchor(paddle, xPos - 2 * speedMult);
      else
        AnchorPane.setLeftAnchor(paddle, 0.0);
    }
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
        if (stateStack.getFirst() != State.Paused) stateStack.push(State.Paused);
        else stateStack.pop();
      }
    });
  }

  public void handlePaused() {

  }

  public void handlePlaying() {
    if (controlStates[0][0])
      movePaddle(paddle1, Dir.Left);

    if (controlStates[0][1])
      movePaddle(paddle1, Dir.Right);

    if (controlStates[1][1])
      movePaddle(paddle2, Dir.Right);

    if (controlStates[1][0])
      movePaddle(paddle2, Dir.Left);
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
                  System.out.println(stateStack.getFirst());
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
