package pong.state;

class PauseState implements State {
  public FlowControl handle(boolean[][] controlState) {
    if(controlState[2][0])
      return FlowControl.LeaveState;
    else
      return FlowControl.Continue;
  }

  public void enter() {

  }
}
