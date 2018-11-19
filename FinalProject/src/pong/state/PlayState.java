package pong.state;

import javafx.geometry.Bounds;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.Rectangle;
import pong.Controller;
import pong.Vec2;

public class PlayState implements State {

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
        state.stateStack.push(new WonRoundState(1));
        return FlowControl.Continue;
      }
      else if (state.pong.getBoundsInParent().intersects(state.paddle2.getBoundsInParent())) {
        System.out.println("Hit P2");
        state.pongVeloc.y *= -1;
        state.pongVeloc.mult(1.5);
      }
    } else { // Bottom half of screen. Could be colliding with P1
      if(pongBounds.intersects(state.bottomGoal.getBoundsInParent())) {
        state.stateStack.push(new WonRoundState(2));
        return FlowControl.Continue;
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
