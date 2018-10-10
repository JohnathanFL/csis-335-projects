package main;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

public class Controller {
  @FXML
  Button btnInfo, btnWarn, btnError, btnConfirm, btnCustom, btnText, btnChoice;

  @FXML
  TextArea txtConfirm, txtCustom, txtInput, txtChoice;


  @FXML
  private void initialize() {
    btnInfo.setOnAction(e -> {
      Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successful Login to Database");
      alert.setTitle("Information");
      alert.setHeaderText("Success");
      alert.show();
    });

    btnWarn.setOnAction(e -> {
      Alert alert = new Alert(Alert.AlertType.WARNING, "Battery is running low!");
      alert.setTitle("Warning");
      alert.setHeaderText("Battery Status");
      alert.show();
    });


    btnError.setOnAction(e -> {
      Alert alert = new Alert(Alert.AlertType.ERROR, "You must enter only digits for the years worked!");
      alert.setTitle("Error");
      alert.setHeaderText("Digits Only");
      alert.show();
    });

    btnConfirm.setOnAction(e -> {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "/home/oakenbow/Documents/customers.txt");
      alert.setTitle("Delete File");
      ButtonType ok = new ButtonType("Ok"), cancel = new ButtonType("Cancel");
      alert.getButtonTypes().clear();
      alert.getButtonTypes().addAll(ok, cancel);
      ButtonType selected = alert.showAndWait().get();
      if(selected == ok)
        txtConfirm.setText("File Deleted!");
      else
        txtConfirm.setText("Cancelled: NOT deleted!");
    });

    btnCustom.setOnAction(e -> {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Select Language");
      ButtonType py = new ButtonType("Python"), java = new ButtonType("Java"), cs = new ButtonType("C#");
      alert.getButtonTypes().clear();
      alert.getButtonTypes().addAll(py, java, cs);
      ButtonType selected = alert.showAndWait().get();
      if(selected == py)
        txtCustom.setText("You chose Python!");
      else if(selected == java)
        txtCustom.setText("You chose Java, heretic!");
      else
        txtCustom.setText("You chose C#!");
    });

    btnText.setOnAction(e -> {
      TextInputDialog dia = new TextInputDialog();
      dia.setHeaderText("Enter your major!");
      dia.setContentText("Major: ");
      dia.setTitle("Major");
      Optional<String> out = dia.showAndWait();
      if(out.isPresent())
        txtInput.setText(out.get());
    });

    btnChoice.setOnAction(e -> {
      String intro = "CSIS152 Into Programming 1a Brekke",
          gui = "CSIS335 GUI Programming Ficek",
          app = "CSIS365 Mobile App Prog & Dev Ficek";
      ChoiceDialog<String> dia = new ChoiceDialog(intro, gui, app);
      dia.setTitle("MSUM CSIS Courses");
      dia.setHeaderText("Select a Course: ");
      dia.setContentText("Course;: ");

      Optional<String> finalChoice = dia.showAndWait();
      if(finalChoice.get() == intro)
        txtChoice.setText("Intro Programming");
      else if(finalChoice.get() == gui)
        txtChoice.setText("GUI Programming");
      else
        txtChoice.setText("Mobile App Prog");

    });

  }

}
