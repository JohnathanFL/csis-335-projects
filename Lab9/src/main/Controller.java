package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller {
  @FXML
  private Button btnHello;

  @FXML
  private Button btnGoodBye;

  @FXML
  private TextField txtGreet;

  @FXML
  private void handleButtonHello(ActionEvent e) {
    System.out.println("You clicked me!");
    txtGreet.setText("Hello World!");
  }

  @FXML
  private void handleButtonGoodBye(ActionEvent e) {
    System.out.println("Goodbye");
    txtGreet.setText("Goodbye!");
  }

  private void initialize() {

  }
}
