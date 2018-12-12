/**
 * Johnathan Lee
 * CSIS 335
 * Final Project
 * Due 12/12/18
 *
 * A classic game of pong, with a slight scoring tweak.
 */
package pong;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

  public static void main(String[] args) {
    Font.loadFont(Main.class.getResource("joystixmonospace.ttf").toExternalForm(), 10);

    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader root = new FXMLLoader(getClass().getResource("Pong.fxml"));
    primaryStage.setTitle("PONG");
    primaryStage.setScene(new Scene(root.load(), 1600, 900));
    primaryStage.show();
    ((Controller) root.getController()).setupHandlers();
  }
}
