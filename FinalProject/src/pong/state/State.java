/**
 * Johnathan Lee
 * CSIS 335
 * Final Project
 * Due 12/12/18
 *
 * A classic game of pong, with a slight scoring tweak.
 */
package pong.state;


/**
 * Interface for all states.
 */
public interface State {
  GameVars state = new GameVars();

  FlowControl handle(); // Update loop. Called every 16.66ms (60 FPS/UPS)

  void enter(); // Perform all setup for this state

  void leave(); //  Perform all cleanup for this state.
}



