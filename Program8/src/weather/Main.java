/**
 * Author: Johnathan Lee
 * CSIS 335
 *
 * Program 8 Due 11/26/18
 * Displays a simple 5 day forecast from the OpenWeathermap.org API
 */

package weather;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
        primaryStage.setTitle("Weather Info");
        primaryStage.setScene(new Scene(root, 1700, 750));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
