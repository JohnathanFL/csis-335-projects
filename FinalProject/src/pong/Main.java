package pong;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader root = new FXMLLoader(getClass().getResource("Pong.fxml"));
        primaryStage.setTitle("PONG");
        primaryStage.setScene(new Scene(root.load(), 1600, 900));
        primaryStage.show();
        ((Controller)root.getController()).setupHandlers();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
