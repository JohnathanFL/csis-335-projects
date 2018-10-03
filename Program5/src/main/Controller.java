package main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller {
  private static final String masterInputMatch = "^((\\d+,?)(\\.\\d*)?)?$";


  @FXML
  Button btnAdd, btnSub, btnMul, btnDiv;

  @FXML
  TextField field1, field2, answerField;

  public void enableAll(boolean[] bools, Button...btns) {
    boolean finalRes = bools[0];
    for(boolean b : bools)
      finalRes = finalRes && b;

    for(Button btn : btns)
      btn.setDisable(finalRes);
  }

  public void initialize() {
    answerField.setEditable(false);

    boolean[] valids = {false, false};
    enableAll(valids, btnSub, btnMul, btnDiv, btnAdd);

    field1.textProperty().addListener((e, oldVal, newVal) -> {
      if(!newVal.matches(masterInputMatch))
        field1.setText(oldVal);

      valids[0] = !field1.getText().isEmpty();
      enableAll(valids, btnAdd, btnMul, btnSub);
      btnDiv.setDisable(btnDiv.isDisable() || btnAdd.isDisable());
    });

    field2.textProperty().addListener((e, oldVal, newVal) -> {
      boolean validDiv = true;

      if(!newVal.matches(masterInputMatch))
        field2.setText(oldVal);
      else
        validDiv = (Double.parseDouble(newVal) != 0);

        btnDiv.setDisable(!validDiv);
    });
  }
}
