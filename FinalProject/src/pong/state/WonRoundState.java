package pong.state;

import pong.Vec2;

class WonRoundState implements State {
  int winner;

  public WonRoundState(int winner) {
    this.winner = winner;
  }

  public FlowControl handle(boolean[][] controlState) {
    //System.out.println("Handling win");
    if(state.pong.getLength() > 1.0) {
      state.goalText.setVisible(true);
      state.pong.setLength(state.pong.getLength() - state.pong.getLength() * 0.1667 / 10);

    } else {
      state.goalText.setVisible(false);

      state.stateStack.pop(); // Done with the win anim

      if(winner == 1)
        state.p1Score++;
      else
        state.p2Score++;

      state.p1ScoreLbl.setText(Integer.toString(state.p1Score));
      state.p2ScoreLbl.setText(Integer.toString(state.p2Score));

      state.pong.setLength(360);
      state.middle.setConstraints(state.pong);
      // -0.5 brings it into negative, *2.0 brings it back to a full [-1, 1] range
      state.pongVeloc = new Vec2((Math.random() - 0.5) * 2.0, (Math.random() - 0.5) * 2.0);
      state.pongVeloc.normalize();
      state.pongVeloc.mult(2.0);
      if(state.pongVeloc.length() >= 8.0)
        state.pongVeloc.mult(1/2.0);
    }

    return FlowControl.Continue;
  }

  public void enter() {

  }
}
