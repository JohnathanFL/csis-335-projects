package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Painter extends Application {

  @Override
  public void start(Stage primStage) throws Exception{
    Parent root = FXMLLoader.load(getClass().getResource("Painter.fxml"));
    primStage.setScene(new Scene(root));
    primStage.setTitle("Painter");
    primStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
