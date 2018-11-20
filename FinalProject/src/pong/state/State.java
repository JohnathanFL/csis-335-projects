package pong.state;


public interface State {
  StateInfo state = new StateInfo();

  FlowControl handle();
  void enter(); // Perform all setup for this state
}



