package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
  @FXML
  Label lblGreeting;

  @FXML
  Button btnSubmit, btnClose;

  @FXML
  TextField txtName;

  public void createGreeting(String tmp) {
    String curText = lblGreeting.getText();
    lblGreeting.setText(curText + " " + tmp);
  }


  @FXML
  public void handleButtonAction(ActionEvent event) throws IOException {
    Stage stage;
    Parent root;

    if(event.getSource() == btnSubmit) {
      System.out.println("Button Submit was clicked!");

      stage = (Stage)btnSubmit.getScene().getWindow();

      FXMLLoader loader = new FXMLLoader(getClass().getResource("Summary.fxml"));
      root = loader.load();

      MainController ctrl = (MainController)loader.getController();
      String userEntry = txtName.getText();
      ctrl.createGreeting(userEntry);
    } else {
      System.out.println("Button Close was clicked!");

      stage = (Stage)btnClose.getScene().getWindow();
      root =
      FXMLLoader.load(getClass().getResource("MainLayout.fxml"));


    }

    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }


}
