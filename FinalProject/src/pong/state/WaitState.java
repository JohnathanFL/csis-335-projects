/**
 * Johnathan Lee
 * CSIS 335
 * Final Project
 * Due 12/12/18
 *
 * A classic game of pong, with a slight scoring tweak.
 */
package pong.state;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import pong.Main;

import java.time.Duration;
import java.time.Instant;


public class WaitState implements State {
  Instant endAt;
  State nextState;
  private MediaPlayer mediaPlayer = new MediaPlayer(new Media(Main.class.getResource("Begin.wav").toExternalForm()));

  public WaitState(long secToWait) {
    endAt = Instant.now().plusMillis(secToWait * 1000);
  }

  public WaitState(long secToWait, State nextState) {
    this(secToWait);
    this.nextState = nextState;
  }

  public FlowControl handle() {

    state.goalText.setText("Game begins in " + (Duration.between(Instant.now(), endAt)).getSeconds());
    if (Instant.now().isAfter(endAt)) {
      if (nextState == null)
        return FlowControl.LeaveState;
      else {
        state.nextState = nextState;
        return FlowControl.TransitionTo;
      }
    } else
      return FlowControl.Continue;
  }

  public void enter() {
    System.out.println("Enter WaitState");
    mediaPlayer.play();
    mediaPlayer.setCycleCount(Integer.MAX_VALUE);

    state.goalText.setVisible(true);
  } // Perform all setup for this state

  public void leave() {
    System.out.println("Leave WaitState");
    mediaPlayer.stop();

    state.goalText.setVisible(false);
    //System.out.println("Left WaitState at " + this.timeLeft);
  } //  Perform all cleanup for this state.

}
