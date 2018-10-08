package main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Controller for mainScene.fxml
 */
public class Controller {

  private static final String
          /** Any positive or negative number. If it has a decimal place, it must have numbers after the decimal place*/
          isNumber = "^-?(\\d+)(\\.\\d+)?$",
          /** An optional '-' followed by 1 or more digits and an optional decimal place and optional digits */
          isValid = "^(-?(\\d+)?(\\.\\d*)?)?$";

  /**
   * Numbers extracted from field1/2, respectively
   */
  private Double num1, num2;

  @FXML
  Button btnAdd, btnSub, btnMul, btnDiv;

  @FXML
  TextField field1, field2, answerField;

  /**
   * Syntactic sugar for disabling a bunch of buttons based on an array of flags.
   *
   * If ALL of the bools are true, then the buttons are enabled. Otherwise, they are disabled.
   * @param bools All the flags to disable based on
   * @param btns The buttons to enable/disable
   */
  public void enableAll(boolean[] bools, Button...btns) {
    boolean finalRes = bools[0];
    for(boolean b : bools)
      finalRes = finalRes && b;

    for(Button btn : btns)
      btn.setDisable(!finalRes);
  }

  public void initialize() {
    answerField.setEditable(false);

    boolean[] canDiv = {true}; // Array to get around lambda restrictions. Could use an atomic, but that's a hassle.
    boolean[] valids = {false, false};
    enableAll(valids, btnSub, btnMul, btnAdd, btnDiv);

    field1.textProperty().addListener((e, oldVal, newVal) -> {
      if(!newVal.matches(isValid)) {
        field1.setText(oldVal);
      }else {
        valids[0] = newVal.matches(isNumber);
        if(valids[0])
          num1 = Double.parseDouble(newVal);
      }

      enableAll(valids, btnSub, btnMul, btnAdd);

      btnDiv.setDisable(!canDiv[0] || btnAdd.isDisable());

      System.out.println(valids[0]);
    });

    field2.textProperty().addListener((e, oldVal, newVal) -> {
      boolean isNumber = newVal.matches(this.isNumber);
      if(!newVal.matches(isValid)) {
        field2.setText(oldVal);
      }else {
        if(!newVal.isEmpty() && isNumber) {
          // Problem doesn't want us to div by 0.
          canDiv[0] = (Double.parseDouble(newVal) != 0);
          valids[1] = true;
          num2 = Double.parseDouble(field2.getText());
        } else {
          canDiv[0] = false;
          valids[1] = false;
        }
      }
        enableAll(valids, btnSub, btnMul, btnAdd);

        btnDiv.setDisable(!canDiv[0] || btnAdd.isDisable());

        System.out.println(valids[1]);
    });

  }

  /**
   * Saves some typing in add/div/sub/mul
   */
  private void clearFields() {
    this.field1.clear();
    this.field2.clear();
  }

  /**
   * Handle add button pressed.
   */
  @FXML
  private void add() {
    answerField.setText(String.format("%.4f + %.4f = %.4f", num1, num2, num1 + num2));
    clearFields();
  }
  /**
   * Handle div button pressed.
   */
  @FXML
  private void div() {
    answerField.setText(String.format("%.4f / %.4f = %.4f", num1, num2, num1 / num2));
    clearFields();
  }
  /**
   * Handle sub button pressed.
   */
  @FXML
  private void sub() {
    answerField.setText(String.format("%.4f - %.4f = %.4f", num1, num2, num1 - num2));
    clearFields();
  }
  /**
   * Handle mul button pressed.
   */
  @FXML
  private void mul() {
    answerField.setText(String.format("%f * %f = %f", num1, num2, num1 * num2));
    clearFields();
  }
}
