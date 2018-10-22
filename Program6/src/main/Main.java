/**
 * Author: Johnathan Lee
 * MSUM CSIS 335 - Program 6
 *
 * Due 10/26/18
 *
 * Connects to a databse of customers and products, then retrieves a local copy and allows the user to move through/update it.
 */
package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        primaryStage.setTitle("Program 6 SaleCo Interface");
        primaryStage.setScene(new Scene(root, 600, 250));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
