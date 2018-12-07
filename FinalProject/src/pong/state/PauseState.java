package pong.state;

import java.time.Instant;

public class PauseState implements State {
  Instant endAt = Instant.now().plusMillis(1000);

  public FlowControl handle() {
    if(state.controls.get("Pause") && Instant.now().isAfter(endAt))
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
