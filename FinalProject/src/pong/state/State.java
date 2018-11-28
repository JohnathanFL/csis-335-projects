package pong.state;


public interface State {
  GameVars state = new GameVars();

  FlowControl handle();
  void enter(); // Perform all setup for this state
  void leave(); //  Perform all cleanup for this state.
}



