package pong.state;

public class DormantState implements State {
  public FlowControl handle() {
    return FlowControl.Continue;
  }
  public void enter(){
    state.goalText.setText("READY PLAYER 1");
    state.goalText.setVisible(true);

    state.p1Score = state.p2Score = 0;
    state.p1ScoreLbl.setText(""+state.p1Score);
    state.p2ScoreLbl.setText(""+state.p2Score);
  }

  public void leave(){
    state.goalText.setVisible(false);

  }
}
