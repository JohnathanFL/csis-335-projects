package main;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class Main extends Application {
    Stage primStage, splasher;

    @Override
    public void init() throws IOException {

    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        splasher = new Stage();
        splasher.initStyle(StageStyle.UNDECORATED);
        Parent splashRoot = FXMLLoader.load(getClass().getResource("splash.fxml"));

        splasher.setTitle("Welcome to the best calculator you'll ever use");
        splasher.setScene(new Scene(splashRoot));
        splasher.setResizable(false);
        splasher.initModality(Modality.APPLICATION_MODAL);
        splasher.setAlwaysOnTop(true);
        splasher.show();



        this.primStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("mainScene.fxml"));

        this.primStage.setTitle("Hello World");
        this.primStage.setScene(new Scene(root));


      AnimationTimer timer = new AnimationTimer() {
        long prev = -1, total = 0;

        @Override
        public void handle(long now) {
          if(prev != -1) {
            total += (now - prev);
            prev = now;

            if (TimeUnit.NANOSECONDS.toSeconds(total) >= 3) {
              splasher.close();
              primStage.show();
              this.stop();
            }
          } else{
            prev = now;
          }
        }
      };

      timer.start();
    }


    public static void main(String[] args) throws Exception {
      launch(args);
    }
}
