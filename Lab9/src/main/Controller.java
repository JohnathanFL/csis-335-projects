package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller {
  @FXML
  private Button btnGreet;

  @FXML
  private TextField txtGreet;

  @FXML
  private void handleButtonAction(ActionEvent e) {
    System.out.println("Clicked me clicked me now ya gotta null me");
    txtGreet.setText("Hello World!");
  }

  private void initialize() {

  }
}
