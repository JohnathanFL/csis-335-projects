package pong.state;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import pong.Controller;
import pong.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.Instant;

public class WonGameState implements State {
  Instant endAt;
  int winner;


//  MediaPlayer mediaPlayer = new MediaPlayer(new Media(Main.class.getResource("Main.mp3").toExternalForm()));

  WonGameState() {}

  public FlowControl handle() {
    if(Instant.now().isAfter(endAt)) {
      state.nextState = new WaitState(4, new PlayState());
      return FlowControl.TransitionTo;
    }
    return FlowControl.Continue;
  }
  public void enter() {
    endAt = Instant.now().plusMillis(3 * 1000);
    state.goalText.setVisible(true);
    int winnerText = (state.p1Score > state.p2Score ? 1 : 2);
    winner = (winnerText == 1 ? State.state.p1.id : State.state.p2.id);

    state.goalText.setText("PLAYER " + winnerText + " WINS!");
  }
  public void leave() {
    PreparedStatement inserter = Controller.addGameWon;

    // Record the game
    Controller.call(inserter, state.p1.id, state.p2.id, state.p1Score, state.p2Score, winner);

    // Reset everything

    state.roundNum = 0;
    System.out.println("Set roundNum");
    state.p1Score = 0;
    state.p2Score = 0;

    state.p1ScoreLbl.setText("" + state.p1Score);
    state.p2ScoreLbl.setText("" + state.p2Score);
    state.goalText.setVisible(false);

    Controller.refresh();
  }
}
