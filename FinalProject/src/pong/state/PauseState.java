/**
 * Johnathan Lee
 * CSIS 335
 * Final Project
 * Due 12/12/18
 *
 * A classic game of pong, with a slight scoring tweak.
 */
package pong.state;

import java.time.Instant;

/**
 * Holds the game dormant.
 * Unlike dormant state, this one can be exited with the Pause key.
 */
public class PauseState implements State {
  Instant endAt = Instant.now().plusMillis(1000);

  public FlowControl handle() {
    if (state.controls.get("Pause") && Instant.now().isAfter(endAt))
      return FlowControl.LeaveState;
    else
      return FlowControl.Continue;
  }

  public void enter() {
    state.goalText.setVisible(true);
    state.goalText.setText("  PAUSED");
  }

  public void leave() {
    state.goalText.setVisible(false);
  }
}
