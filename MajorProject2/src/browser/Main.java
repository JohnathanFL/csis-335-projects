/**
 * Author: Johnathan Lee
 * MSUM CSIS 335
 *
 * Major Project 2
 * Due 11/12/18
 *
 *
 * A browser, using a mysql database in conjunction with built in web views and pie charts for showing stats.
 */

package browser;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.concurrent.TimeUnit;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Browser.fxml"));
        primaryStage.setTitle("crepuscular");
        primaryStage.setScene(new Scene(root, 1600, 900));


        Parent splashRoot = FXMLLoader.load(getClass().getResource("Splash.fxml"));
        Stage splash = new Stage(StageStyle.TRANSPARENT);
        splash.setScene(new Scene(splashRoot));
        splash.initModality(Modality.NONE);
        splash.setResizable(false);
        splash.show();

        AnimationTimer timer = new AnimationTimer() {
            long start = -1;

            @Override
            public void handle(long now) {
                if(start == -1)
                    start = now;

                System.out.println(now - start);

                if(now - start >= TimeUnit.SECONDS.toNanos(3)) {
                    splash.close();
                    primaryStage.show();
                    this.stop();
                }

            }
        };

        timer.start();


    }


    public static void main(String[] args) {
        launch(args);
    }
}
