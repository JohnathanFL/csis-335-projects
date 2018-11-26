package pong.state;

public class DormantState implements State {
  public FlowControl handle() {
    return FlowControl.Continue;
  }
  public void enter(){}// Perform all setup for this state
}
