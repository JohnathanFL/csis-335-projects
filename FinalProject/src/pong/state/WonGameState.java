package pong.state;

class WonGameState implements State {
  public FlowControl handle(boolean[][] controlState) {

    return FlowControl.Continue;
  }

  public void enter() {

  }
}
