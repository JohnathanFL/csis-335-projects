package pong.state;

import pong.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;

class WonGameState implements State {
  double timer;
  int winner;

  WonGameState() {
    timer = 5.0;
    state.goalText.setVisible(true);
    winner = (state.p1Score > state.p2Score ? 1 : 2);
    state.goalText.setText("PLAYER " + winner + " WINS!");
  }

  public FlowControl handle() {
    if(timer <= 0) {

      PreparedStatement inserter = Controller.addGameWon;

      Controller.call(inserter, state.p1ID, state.p2ID, state.p1Score, state.p2Score, winner);

      state.roundNum = 0;
      state.p1Score = 0;
      state.p2Score = 0;

      state.p1ScoreLbl.setText("" + state.p1Score);
      state.p2ScoreLbl.setText("" + state.p2Score);
      state.goalText.setVisible(false);

      return FlowControl.LeaveState;
    } else
      timer -= 1.0 * 0.01667;

    System.out.println(timer);

    return FlowControl.Continue;
  }

  public void enter() {

  }
}
