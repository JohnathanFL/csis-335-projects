package pong.state;

import pong.Vec2;

class WonRoundState implements State {
  int winner;

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
        state.p1Score++;
      else
        state.p2Score++;

      state.p1ScoreLbl.setText(Integer.toString(state.p1Score));
      state.p2ScoreLbl.setText(Integer.toString(state.p2Score));

      state.resetPos();
      state.pongAngle = 360;

      state.roundNum++;




      return FlowControl.LeaveState;
    }
  }

  public void enter() {

  }
}
