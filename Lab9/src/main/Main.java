package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    URL fxml = getClass().getResource("/main/sample.fxml");
    Parent root = FXMLLoader.load(fxml);
    stage.setTitle("Hello World!");
    stage.setScene(new Scene(root));
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
