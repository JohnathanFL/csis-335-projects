package pong;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import sun.awt.AWTAccessor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Stack;

public class Controller {
  public AnchorPane gameScene;
  public Arc pong = null;
  public Rectangle paddle1 = null, paddle2 = null;
  public Rectangle topGoal = null, bottomGoal = null;
  public Label p1ScoreLbl = null;
  public Label p2ScoreLbl = null;
  public Label goalText = null;

  boolean p2IsBot = true;

  Deque<State> stateStack = new ArrayDeque<>();

  // {paddle1: {leftCtrl, rightCtrl}, paddle2: ..., pauseBtn
  boolean[][] controlStates = {{false, false}, {false, false}, {false}};


  Integer handleAIInterval = 0;

  public void setupHandlers() {
    gameScene.getScene().setOnKeyReleased(key -> setKeyStatesTo(false, key.getCode()));
    gameScene.getScene().setOnKeyPressed(key -> {
      setKeyStatesTo(true, key.getCode());

      if (key.getCode() == KeyCode.ESCAPE) {
        controlStates[2][0] = true;
      }
    });
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

  public void initialize() {

    State.state.init(pong, paddle1, paddle2, topGoal, bottomGoal, p1ScoreLbl, p2ScoreLbl, goalText);
    stateStack.push(new PlayState());

    Timeline tick = TimelineBuilder
            .create()
            .keyFrames(
                    new KeyFrame(
                            new Duration(16.667), // 60 FPS
                            ev -> {
                              if(p2IsBot)
                                handleAI();

                              State curState = stateStack.getFirst();
                              //System.out.println(stateStack.getFirst());
                              curState.handle(controlStates);
                            }
                    )
            )
            .cycleCount(Timeline.INDEFINITE)
            .build();

    tick.play();

  }

  public void handlePlaying() {



  }

  public void handlePaused() {

  }

  public void handleWin(State which) {

  }

  private void handleAI() {
    // Only handle every 100 ticks
    if (handleAIInterval++ < 10)
      return;
    else
      handleAIInterval = 0;

    boolean[] ctrls = controlStates[1]; // P2 is index 1
    double xPos = AnchorPane.getLeftAnchor(paddle2),
            pongXPos = AnchorPane.getLeftAnchor(pong);

    if (pongXPos < xPos) {
      ctrls[0] = true;
      ctrls[1] = false;
    } else {
      ctrls[1] = true;
      ctrls[0] = false;
    }
  }



  public enum Dir {
    Left, Right
  }
}
