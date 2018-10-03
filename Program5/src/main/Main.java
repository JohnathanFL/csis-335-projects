package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.concurrent.TimeUnit;

class Splasher extends Application {
  Stage stage;

  @Override
  public void start(Stage primStage) throws Exception {
    System.out.println("Initializing splasher...");

    Stage splasher = new Stage();
    splasher.initStyle(StageStyle.UNDECORATED);
    Parent splashRoot = FXMLLoader.load(Class.forName("main.Main").getResource("splash.png"));
    splasher.setTitle("Welcome to the best calculator you'll ever use");
    splasher.setScene(new Scene(splashRoot));
    splasher.show();
  }
}

public class Main extends Application {
    Stage primStage;

    @Override
    public void start(Stage primaryStage) throws Exception{

        TimeUnit.SECONDS.sleep(3);


        this.primStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("mainScene.fxml"));
        this.primStage.setTitle("Hello World");
        this.primStage.setScene(new Scene(root, 300, 275));
        this.primStage.show();
    }


    public static void main(String[] args) throws Exception {
      Thread t2 = new Thread(() -> Splasher.launch(args));
      t2.start();
      launch(args);
    }
}
