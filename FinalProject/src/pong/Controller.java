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
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import pong.state.FlowControl;
import pong.state.PlayState;
import pong.state.State;
import pong.state.StateInfo;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class Controller {
  public AnchorPane gameScene;
  public Label p1ScoreLbl;
  public Label p2ScoreLbl;
  public Label goalText;
  public Canvas gfx;

  boolean p2IsBot = false;

  //Deque<State> stateStack = new ArrayDeque<>();

  // {paddle1: {leftCtrl, rightCtrl}, paddle2: ..., pauseBtn
  public Map<String, Boolean> controls = new HashMap<>(),
  // gives us simple "just pressed" style capabilities
        lastControls = new HashMap<>();


  Integer handleAIInterval = 0;

  public void setupHandlers() {
    gameScene.getScene().setOnKeyReleased(key -> setKeyStatesTo(false, key.getCode()));
    gameScene.getScene().setOnKeyPressed(key -> {
      setKeyStatesTo(true, key.getCode());
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

    lastControls.put(which, controls.get(which));
    controls.put(which, bool);

    //System.out.println(this.controls);
  }

  public void drawPaddle(Vec2 pos, Paint color) {
    GraphicsContext ctx = gfx.getGraphicsContext2D();
    ctx.setLineWidth(5);

    ctx.setFill(color);

    ctx.fillRect(pos.x, pos.y, StateInfo.paddleSize.x, StateInfo.paddleSize.y);
  }

  public void drawPong(Vec2 pos, double angle, Paint color) {
    GraphicsContext ctx = gfx.getGraphicsContext2D();

    ctx.setLineWidth(5);
    ctx.setFill(color);
    ctx.setStroke(color);

    ctx.fillArc(pos.x, pos.y, StateInfo.pongSize.x, StateInfo.pongSize.y, 0, angle, ArcType.ROUND);
  }

  public void draw() {
    GraphicsContext ctx = gfx.getGraphicsContext2D();
    ctx.clearRect(0,0, StateInfo.extents.x, StateInfo.extents.y);

    StateInfo info = State.state;

    //System.out.println("Drawing to " + p1Pos);

    drawPaddle(info.paddle1Pos, Color.BLUE);
    drawPaddle(info.paddle2Pos, Color.RED);

    drawPong(info.pongPos, 360, Color.PURPLE);
  }

  public void initialize() {

    State.state.init(controls, p1ScoreLbl, p2ScoreLbl, goalText);
    State.state.stateStack.push(new PlayState());
    StateInfo info = State.state;

    // If these aren't pre-initialized, we'll get nullptr exceptions galore.
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
                              if(curState.handle() == FlowControl.LeaveState)
                                info.stateStack.pop();

                              draw();

                              System.out.println(info.stateStack);
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

    StateInfo state = State.state;

    if (state.pongPos.x < state.paddle2Pos.x) {
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
