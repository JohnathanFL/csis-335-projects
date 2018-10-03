package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
  @FXML
  Button btnShowScene2;

  @FXML
  Button btnBackToMain;

  @FXML
  private void handleBtnShowScene2(ActionEvent e) throws IOException {
    Stage stage = (Stage)btnShowScene2.getScene().getWindow();
    Parent root = FXMLLoader.load(getClass().getResource("scene2.fxml"));
    stage.setScene(new Scene(root));
    System.out.println("Switching to Scene2");
  }

  @FXML
  private void handleBtnBackToMain(ActionEvent e) throws IOException {
    Stage stage = (Stage)btnBackToMain.getScene().getWindow();
    Parent root = FXMLLoader.load(getClass().getResource("scene1.fxml"));
    stage.setScene(new Scene(root));
    System.out.println("Returning to main scene");
  }



}
