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

import java.net.URISyntaxException;

/**
 * Just sits there, doing nothing. Has nice music though.
 */
public class DormantState implements State {
  MediaPlayer mediaPlayer;

  public DormantState() {
    try {
      String uri = Main.class.getResource("Opening.wav").toURI().toString();
      System.out.println("Loading " + uri);
      Media song = new Media(uri);
      System.out.println(song.getDuration());
      mediaPlayer = new MediaPlayer(song);
    } catch (URISyntaxException ex) {
      System.out.println("Failed to load DormantState media player because: " + ex);
    }
  }

  public FlowControl handle() {
    return FlowControl.Continue;
  }

  public void enter() {
    state.goalText.setText("READY PLAYER 1");
    state.goalText.setVisible(true);

    state.p1Score = state.p2Score = 0;
    state.p1ScoreLbl.setText("" + state.p1Score);
    state.p2ScoreLbl.setText("" + state.p2Score);

    mediaPlayer.play();
    mediaPlayer.setCycleCount(Integer.MAX_VALUE);

    state.resetPos();
  }

  public void leave() {
    state.goalText.setVisible(false);

    mediaPlayer.pause();
  }
}
