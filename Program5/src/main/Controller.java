package main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller {
  private static final String masterInputMatch = "^((\\d+,?)(\\.\\d*)?)?$";

  private Double num1, num2;

  @FXML
  Button btnAdd, btnSub, btnMul, btnDiv;

  @FXML
  TextField field1, field2, answerField;

  public void enableAll(boolean[] bools, Button...btns) {
    boolean finalRes = bools[0];
    for(boolean b : bools)
      finalRes = finalRes && b;

    for(Button btn : btns)
      btn.setDisable(!finalRes);
  }

  public void initialize() {
    answerField.setEditable(false);

    boolean[] canDiv = {true}; // Array to get around lambda restrictions
    boolean[] valids = {false, false};
    enableAll(valids, btnSub, btnMul, btnAdd, btnDiv);

    field1.textProperty().addListener((e, oldVal, newVal) -> {
      if(!newVal.matches(masterInputMatch))
        field1.setText(oldVal);

      valids[0] = !field1.getText().isEmpty();
      enableAll(valids, btnAdd, btnMul, btnSub);
      btnDiv.setDisable(btnAdd.isDisable() || !canDiv[0]);

      if(!field1.getText().isEmpty())
        num1 = Double.parseDouble(field1.getText());
    });

    field2.textProperty().addListener((e, oldVal, newVal) -> {

      if(!newVal.matches(masterInputMatch)) {
        field2.setText(oldVal);
      }else {


        if(!newVal.isEmpty()) {
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
    });
  }


  @FXML
  private void add() {
    answerField.setText(new Double(num1 + num2).toString());
  }

  @FXML
  private void div() {
    answerField.setText(new Double(num1 / num2).toString());
  }

  @FXML
  private void sub() {
    answerField.setText(new Double(num1 - num2).toString());
  }

  @FXML
  private void mul() {
    answerField.setText(new Double(num1 * num2).toString());
  }
}
