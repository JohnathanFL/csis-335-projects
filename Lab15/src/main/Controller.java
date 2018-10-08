package main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {
  @FXML
  Button btnInfo, btnWarn, btnError, btnConfirm, btnCustom, btnText, btnChoice;

  @FXML
  TextArea txtConfirm, txtCustom, txtInput, txtChoice;


  private void initialize() {
    btnInfo.setOnAction(e -> {
      System.out.println("Test");


      Dialog<Boolean> dia = new Dialog<>();
      dia.setHeaderText("Success");
      dia.setContentText("Successful Login!");


      dia.show();
    });
  }

}
