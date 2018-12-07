package pong.state;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import pong.Main;
import pong.Vec2;

class WonRoundState implements State {
  int winner;

//  MediaPlayer mediaPlayer = new MediaPlayer(new Media(Main.class.getResource("Main.mp3").toExternalForm()));

  public WonRoundState(int winner) {
    this.winner = winner;
  }

  public FlowControl handle() {
    //System.out.println("Handling win");
    if(state.pongAngle > 1.0) {
      state.goalText.setVisible(true);
      state.goalText.setText("GOAAAAAAAAAAAAAL!");
      state.pongAngle = state.pongAngle - state.pongAngle * 0.1667 / 10;

      return FlowControl.Continue;
    } else {
      state.goalText.setVisible(false);

      if(winner == 1)
        state.p1Score += 1 * state.pongVeloc.length();
      else
        state.p2Score += 1 * state.pongVeloc.length();

      state.p1ScoreLbl.setText(Integer.toString(state.p1Score));
      state.p2ScoreLbl.setText(Integer.toString(state.p2Score));

      state.resetPos();
      state.pongAngle = 360;

      state.roundNum++;


      if(state.roundNum < 3) {
        state.nextState = new WaitState(4);
        return FlowControl.TransitionTo;
      } else {
        return FlowControl.LeaveState;
      }
    }
  }

  public void enter() {

  }

  public void leave() {

  }
}
