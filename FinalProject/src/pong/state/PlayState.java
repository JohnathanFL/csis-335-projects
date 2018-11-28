package pong.state;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import pong.Controller;
import pong.Vec2;

public class PlayState implements State {

  public FlowControl handle() {

    if(state.roundNum == 3) {
      state.stateStack.push(new WonGameState());
      return FlowControl.Continue;
    }


    if (state.controls.get("P1Left"))
      movePaddle(state.paddle1Pos, Controller.Dir.Left);
    if (state.controls.get("P1Right"))
      movePaddle(state.paddle1Pos, Controller.Dir.Right);
    if (state.controls.get("P2Right"))
      movePaddle(state.paddle2Pos, Controller.Dir.Right);
    if (state.controls.get("P2Left"))
      movePaddle(state.paddle2Pos, Controller.Dir.Left);
    if(state.controls.get("Pause"))
      state.stateStack.push(new PauseState());

    //System.out.println("From " + pongPos);

    final Bounds arenaBounds = new BoundingBox(0, 0, GameVars.extents.x, GameVars.extents.y);

    Bounds p1Bounds = new BoundingBox(state.paddle1Pos.x, state.paddle1Pos.y, GameVars.paddleSize.x,
            GameVars.paddleSize.y),
            p2Bounds = new BoundingBox(state.paddle2Pos.x, state.paddle2Pos.y, GameVars.paddleSize.x,
                    GameVars.paddleSize.y),
            pongBounds = new BoundingBox(state.pongPos.x, state.pongPos.y, GameVars.pongSize.x,
                    GameVars.pongSize.y);

    if(!arenaBounds.contains(GameVars.middle.x, state.pongPos.y))
      state.stateStack.push(new WonRoundState(1));
    else if(!arenaBounds.contains(GameVars.middle.x,state.pongPos.y + GameVars.pongSize.y))
      state.stateStack.push(new WonRoundState(2));


    if(!arenaBounds.contains(state.pongPos.x - 1.0, GameVars.middle.y) || !arenaBounds.contains((state.pongPos.x + GameVars.pongSize.x + 1.0), GameVars.middle.y))
      state.pongVeloc.x *= -1;

    if (pongBounds.intersects(p1Bounds) || pongBounds.intersects(p2Bounds)) {
      state.pongVeloc.y *= -1; // Reflect
      state.pongVeloc.mult(1.5); // Get faster
    }


    state.pongPos.add(state.pongVeloc);

    //System.out.println("To " + pongPos);

    return FlowControl.Continue;
  }

  public void movePaddle(Vec2 pos, Controller.Dir dir) {
    if (dir == Controller.Dir.Right) {
      pos.x += 2 * state.speedMult;
    } else if (dir == Controller.Dir.Left) {
      pos.x -= 2 * state.speedMult;
    }

    if (pos.x <= 1)
      pos.x = 1.01;
    else if (pos.x >= (GameVars.extents.x - GameVars.paddleSize.x))
      pos.x = GameVars.extents.x - GameVars.paddleSize.x;

    //System.out.println("Moved a paddle to " + pos + "(" + dir + ")");
  }

  public void enter() {

  }

  public void leave() {

  }

}
