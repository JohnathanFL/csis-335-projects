/**
 * Johnathan Lee
 * CSIS 335
 * Final Project
 * Due 12/12/18
 *
 * A classic game of pong, with a slight scoring tweak.
 */
package pong.state;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import pong.Controller;
import pong.Main;
import pong.Vec2;

import java.time.Instant;
import java.util.Random;

public class PlayState implements State {

  public int maxSpeed = 24;
  MediaPlayer music = new MediaPlayer(new Media(Main.class.getResource("Facehammer.wav").toExternalForm()));
  private MediaPlayer hitSound = new MediaPlayer(new Media(Main.class.getResource("Hit.wav").toExternalForm()));
  private Instant lastPause = Instant.now();

  public FlowControl handle() {

    if (state.roundNum == 3) {
      System.out.println("A game was won!");
      state.nextState = new WonGameState();
      return FlowControl.TransitionTo;
    }


    if (state.controls.get("P1Left"))
      movePaddle(state.paddle1Pos, Controller.Dir.Left);
    if (state.controls.get("P1Right"))
      movePaddle(state.paddle1Pos, Controller.Dir.Right);
    if (state.controls.get("P2Right"))
      movePaddle(state.paddle2Pos, Controller.Dir.Right);
    if (state.controls.get("P2Left"))
      movePaddle(state.paddle2Pos, Controller.Dir.Left);

    Instant now = Instant.now();
    if (state.controls.get("Pause") && now.isAfter(lastPause.plusMillis(500))) {
      System.out.println("Last paued at " + lastPause);
      lastPause = now;
      state.stateStack.push(new PauseState());
    }

    //System.out.println("From " + pongPos);

    final Bounds arenaBounds = new BoundingBox(0, 0, GameVars.extents.x, GameVars.extents.y);

    Bounds p1Bounds = new BoundingBox(state.paddle1Pos.x, state.paddle1Pos.y, GameVars.paddleSize.x,
        GameVars.paddleSize.y),
        p2Bounds = new BoundingBox(state.paddle2Pos.x, state.paddle2Pos.y, GameVars.paddleSize.x,
            GameVars.paddleSize.y),
        pongBounds = new BoundingBox(state.pongPos.x, state.pongPos.y, GameVars.pongSize.x,
            GameVars.pongSize.y);

    if (!arenaBounds.contains(GameVars.middle.x, state.pongPos.y))
      state.stateStack.push(new WonRoundState(1));
    else if (!arenaBounds.contains(GameVars.middle.x, state.pongPos.y + GameVars.pongSize.y))
      state.stateStack.push(new WonRoundState(2));


    if (!arenaBounds.contains(state.pongPos.x - 1.0, GameVars.middle.y) || !arenaBounds.contains((state.pongPos.x + GameVars.pongSize.x + 1.0), GameVars.middle.y)) {
      hitSound.stop();
      hitSound.play();

      state.pongVeloc.x *= -1;
    }

    if (pongBounds.intersects(p1Bounds) || pongBounds.intersects(p2Bounds)) {
      hitSound.stop();
      hitSound.play();

      state.pongVeloc.y *= -1; // Reflect
      Vec2 curVeloc = state.pongVeloc.clone();
      curVeloc.mult(1 / curVeloc.length());
      state.pongVeloc.add(curVeloc); // Get faster
      if (state.pongVeloc.length() > maxSpeed) {
        state.pongVeloc.mult(1 / state.pongVeloc.length());
        state.pongVeloc.mult(maxSpeed);
      }

      Random rand = new Random(Instant.now().getEpochSecond());
      curVeloc.x += rand.nextDouble() * 10.0 - 5.0; // x += (rand from (-5, 5)
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
    // Hackish solution. Makes sure we don't get stuck in the pause state.
    lastPause = Instant.now();
    music.play();
    music.setCycleCount(Integer.MAX_VALUE);
  }

  public void leave() {
    System.out.println("Leave PlayState");
    music.pause();
  }

}
