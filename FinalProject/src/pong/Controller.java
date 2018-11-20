package pong;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import pong.state.PlayState;
import pong.state.State;
import pong.state.StateInfo;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class Controller {
  public AnchorPane gameScene;
  public Arc pong;
  public Rectangle paddle1, paddle2;
  public Rectangle topGoal, bottomGoal;
  public Label p1ScoreLbl;
  public Label p2ScoreLbl;
  public Label goalText;
  public Canvas gfx;

  boolean p2IsBot = true;

  //Deque<State> stateStack = new ArrayDeque<>();

  // {paddle1: {leftCtrl, rightCtrl}, paddle2: ..., pauseBtn
  public Map<String, Boolean> controls = new HashMap<>();


  Integer handleAIInterval = 0;

  public void setupHandlers() {
    gameScene.getScene().setOnKeyReleased(key -> setKeyStatesTo(false, key.getCode()));
    gameScene.getScene().setOnKeyPressed(key -> {
      setKeyStatesTo(true, key.getCode());

      if (key.getCode() == KeyCode.ESCAPE) {
        controls.put("Pause", true);
      }
    });
  }

  public void setKeyStatesTo(boolean bool, KeyCode key) {
    String which;
    switch (key) {
      case LEFT:
        which = "P1Left";
        break;

      case RIGHT:
        which = "P1Right";
        break;

      case A:
        which = "P2Left";
        break;

      case D:
        which = "P2Right";
        break;

      case ESCAPE:
        which = "Pause";
        break;

      default:
        return;
    }

    controls.put(which, bool);

    //System.out.printf("Paddle1: %b, %b\nPaddle2: %b, %b\n\n", controlStates[0][0], controlStates[0][1], controlStates[1][0], controlStates[1][1]);
  }

  public void initialize() {

    State.state.init(controls, pong, paddle1, paddle2, topGoal, bottomGoal, p1ScoreLbl, p2ScoreLbl, goalText);
    State.state.stateStack.push(new PlayState());
    StateInfo info = State.state;

    String[] usedMappings = {"P1Left", "P2Left", "P1Right", "P2Right", "Pause"};
    for(String str : usedMappings)
      controls.put(str, false);

    Timeline tick = TimelineBuilder
            .create()
            .keyFrames(
                    new KeyFrame(
                            new Duration(16.667), // 60 FPS
                            ev -> {
                              if(p2IsBot)
                                handleAI();

                              State curState = info.stateStack.getFirst();
                              curState.handle();
                            }
                    )
            )
            .cycleCount(Timeline.INDEFINITE)
            .build();

    tick.play();

  }

  private void handleAI() {
    // Only handle every 100 ticks
    if (handleAIInterval++ < 10)
      return;
    else
      handleAIInterval = 0;

    Vec2 paddlePos = new Vec2(paddle2), pongPos = new Vec2(pong);

    if (pongPos.x < paddlePos.x) {
      controls.put("P2Left", true);
      controls.put("P2Right", false);
    } else {
      controls.put("P2Left", false);
      controls.put("P2Right", true);
    }
  }



  public enum Dir {
    Left, Right
  }
}
