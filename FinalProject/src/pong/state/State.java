package pong.state;


public interface State {
  StateInfo state = new StateInfo();

  FlowControl handle(boolean[][] controlState);
  void enter(); // Perform all setup for this state
}



