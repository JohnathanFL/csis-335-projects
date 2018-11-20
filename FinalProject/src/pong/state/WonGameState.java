package pong.state;

class WonGameState implements State {
  public FlowControl handle() {

    return FlowControl.Continue;
  }

  public void enter() {

  }
}
