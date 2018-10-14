package main;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.concurrent.TimeUnit;

public class Main extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("PrimeScene.fxml"));
    primaryStage.setTitle("SaleCo Ordering Interface");
    primaryStage.setScene(new Scene(loader.load(), 1600, 600));


    PrimeController controller = loader.getController();

    primaryStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, e -> {
      controller.showSavers();
    });


    FXMLLoader splashLoader = new FXMLLoader(getClass().getResource("Splash.fxml"));
    Stage splashStage = new Stage();

    splashStage.setScene(new Scene(splashLoader.load()));


    // practically straight from Program5. Still surprised jfx has no built in for a splash screen.
    AnimationTimer timer = new AnimationTimer() {
      long /** Last timestamp recorded */
              prev = -1,
      /** Total NANOSECONDS, NOT SECONDS elapsed */
      total = 0;


      @Override
      public void handle(long now) {
        if (prev != -1) {
          total += (now - prev);
          prev = now;
          // Close after 3 seconds
          if (TimeUnit.NANOSECONDS.toSeconds(total) >= 3) {
            splashStage.close();

            primaryStage.show();
            // Other setup for the controller. Needs to be called after the window is created, stupidly.
            controller.addHandlers();


            this.stop();
          }
        } else { // Gotta have a start
          prev = now;
        }
      }
    };

  }
}
