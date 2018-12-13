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

import java.time.Instant;

/**
 * Tells the players there was a goal, then goes back on its business
 */
class WonRoundState implements State {
  int winner;
  Instant endAt = Instant.now().plusMillis(1500);

  MediaPlayer mediaPlayer = new MediaPlayer(new Media(Main.class.getResource("Goal.wav").toExternalForm()));

  public WonRoundState(int winner) {
    this.winner = winner;
  }

  public FlowControl handle() {
    //System.out.println("Handling win");
    if (Instant.now().isAfter(endAt)) {
      if (state.roundNum + 1 < 3) {
        System.out.println("ROUND NUM: " + state.roundNum);
        state.nextState = new WaitState(4);
        return FlowControl.TransitionTo;
      } else {
        System.out.println("Got here!");
        return FlowControl.LeaveState;
      }
    } else
      return FlowControl.Continue;
  }

  public void enter() {
    mediaPlayer.play();

    state.goalText.setVisible(true);
    state.goalText.setText("GOAAAAAAAAAAAAAL!");
  }

  public void leave() {
    mediaPlayer.stop();

    state.goalText.setVisible(false);

    if (winner == 1)
      state.p1Score += 1 * state.pongVeloc.length();
    else
      state.p2Score += 1 * state.pongVeloc.length();

    state.p1ScoreLbl.setText(Integer.toString(state.p1Score));
    state.p2ScoreLbl.setText(Integer.toString(state.p2Score));

    state.resetPos();
    state.pongAngle = 360;

    state.roundNum++;
  }
}
