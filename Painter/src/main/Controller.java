package main;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;

public class Controller {
  @FXML
  RadioButton blackRadioBtn, redRadioBtn, greenRadioBtn, blueRadioBtn, smallRadioBtn, mediumRadioBtn, largeRadioBtn;
  @FXML
  AnchorPane



  private enum PenSize {
    SMALL(2), MEDIUM(4), LARGE(6)

    private final int radius;

    PenSize(int radius) {
      this.radius = radius;
    }

    public int getRadius() {
      return radius;
    }
  }

}
