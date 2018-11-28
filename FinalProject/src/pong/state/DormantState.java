package pong.state;

public class DormantState implements State {
  public FlowControl handle() {
    return FlowControl.Continue;
  }
  public void enter(){
    state.goalText.setText("READY PLAYER 1");
    state.goalText.setVisible(true);
  }// Perform all setup for this state

  public void leave(){
    state.goalText.setVisible(false);
  }
}
