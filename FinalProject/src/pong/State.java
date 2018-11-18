package pong;

import com.sun.org.apache.bcel.internal.generic.LALOAD;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;

import java.util.ArrayDeque;
import java.util.Deque;

// I know, I know. Global state. In my defense, JavaFX ain't exactly built properly for a game to begin with.
class StateInfo {
  Deque<State> stateStack;
  Arc pong = null;
  Rectangle paddle1 = null, paddle2 = null;
  Rectangle topGoal = null, bottomGoal = null;
  Label p1ScoreLbl = null;
  Label p2ScoreLbl = null;
  Label goalText = null;

  static final double maxX = (1600 * 0.75); // Game scene takes 75% of the stage
  static final double paddleWidth = 150;
  static final double paddleHeight = 50;
  static final Vec2 middle = new Vec2(1600*0.75 / 2, 900 / 2);

  Vec2 pongVeloc = new Vec2(-1, -1);
  double speedMult = 5.0;

  int roundNum = 1;

  int p1Score = 0, p2Score = 0;

  StateInfo() {
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

enum FlowControl {
  Continue,
  LeaveState
}

public interface State {
  StateInfo state = new StateInfo();

  FlowControl handle(boolean[][] controlState);
  void enter(); // Perform all setup for this state
}

class WonRoundState implements State {
  int winner;

  public FlowControl handle(boolean[][] controlState) {
    if(state.pong.getLength() > 1.0) {
      state.goalText.setVisible(true);
      state.pong.setLength(state.pong.getLength() - state.pong.getLength() * 0.1667 / 10);

    } else {
      state.goalText.setVisible(false);

      state.stateStack.pop(); // Done with the win anim

      if(winner == 1)
        state.p1Score++;
      else
        state.p2Score++;

      state.p1ScoreLbl.setText(Integer.toString(state.p1Score));
      state.p2ScoreLbl.setText(Integer.toString(state.p2Score));

      state.pong.setLength(360);
      state.middle.setConstraints(state.pong);
      // -0.5 brings it into negative, *2.0 brings it back to a full [-1, 1] range
      state.pongVeloc = new Vec2((Math.random() - 0.5) * 2.0, (Math.random() - 0.5) * 2.0);
      state.pongVeloc.normalize();
      state.pongVeloc.mult(2.0);
      if(state.pongVeloc.length() >= 8.0)
        state.pongVeloc.mult(1/2.0);
    }

    return FlowControl.Continue;
  }

  public void enter() {

  }
}



class WonGameState implements State {
  public FlowControl handle(boolean[][] controlState) {

    return FlowControl.Continue;
  }

  public void enter() {

  }
}

class PlayState implements State {

  public void movePaddle(Rectangle paddle, Controller.Dir dir) {
    Vec2 pos = new Vec2(paddle);
    if (dir == Controller.Dir.Right) {

      if (pos.x < (state.maxX - state.paddleWidth))
        pos.x += 2 * state.speedMult;
      else
        pos.x = state.maxX - state.paddleWidth;
    } else if (dir == Controller.Dir.Left) {
      if (pos.x > 0)
        pos.x -= 2 * state.speedMult;
      else
        pos.x = 0.0;
    }

    pos.setConstraints(paddle);
  }

  public FlowControl handle(boolean[][] controlState) {
    if (controlState[0][0])
      movePaddle(state.paddle1, Controller.Dir.Left);

    if (controlState[0][1])
      movePaddle(state.paddle1, Controller.Dir.Right);

    if (controlState[1][1])
      movePaddle(state.paddle2, Controller.Dir.Right);

    if (controlState[1][0])
      movePaddle(state.paddle2, Controller.Dir.Left);

    Vec2 pongPos = new Vec2(state.pong);

    //System.out.println("From " + pongPos);

    if (pongPos.x <= 1 || pongPos.x >= (state.maxX - 30.0))
      state.pongVeloc.x *= -1;

    Bounds pongBounds = state.pong.getBoundsInParent();

    if (pongPos.y >= 450) { // Top half of screen. Could be colliding with P2

      if(pongBounds.intersects(state.topGoal.getBoundsInParent())) {
        //state.stateStack.push(State.P1Scored);
        pongPos.y = 900 - (25 + 15);
        pongPos.setConstraints(state.pong);
      }
      else if (state.pong.getBoundsInParent().intersects(state.paddle2.getBoundsInParent())) {
        System.out.println("Hit P2");
        state.pongVeloc.y *= -1;
        state.pongVeloc.mult(1.5);
      }
    } else { // Bottom half of screen. Could be colliding with P1
      if(pongBounds.intersects(state.bottomGoal.getBoundsInParent())) {
        //state.stateStack.push(State.P2Scored);
        pongPos.y = 15;
        pongPos.setConstraints(state.pong);
      }
      else if (state.pong.getBoundsInParent().intersects(state.paddle1.getBoundsInParent())) {
        System.out.println("Hit P1");
        state.pongVeloc.y *= -1;
        state.pongVeloc.mult(1.5);
      }
    }

    // This one crashes the program. Left in for the laughs it caused
    //pong.setStartAngle( pong.getStartAngle() + (pong.getStartAngle() - pongVeloc.get360Angle() * 0.1667));

    state.pong.setStartAngle(state.pong.getStartAngle() + (state.pongVeloc.get360Angle() - state.pong.getStartAngle()) * (0.1667 / 2));


    pongPos.add(state.pongVeloc);


    pongPos.setConstraints(state.pong);

    //System.out.println("To " + pongPos);

    return FlowControl.Continue;
  }

  public void enter() {

  }

}

class PauseState implements State {
  public FlowControl handle(boolean[][] controlState) {
    if(controlState[2][0])
      return FlowControl.LeaveState;
    else
      return FlowControl.Continue;
  }

  public void enter() {

  }
}

