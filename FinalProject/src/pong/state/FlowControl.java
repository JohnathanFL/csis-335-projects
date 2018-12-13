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
 * Sugar for StateMachine logic
 */
public enum FlowControl {
  Continue,
  LeaveState,
  TransitionTo; /**If set, then state.nextState should not be null **/
}
