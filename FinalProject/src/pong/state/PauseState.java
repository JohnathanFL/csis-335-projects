package pong.state;

public class PauseState implements State {
  public FlowControl handle() {
    if(state.controls.get("Pause"))
      return FlowControl.LeaveState;
    else
      return FlowControl.Continue;
  }

  public void enter() {

  }

  public void leave() {

  }
}
