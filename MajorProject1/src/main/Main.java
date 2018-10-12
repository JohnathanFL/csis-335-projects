package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.omg.CORBA.PRIVATE_MEMBER;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PrimeScene.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(loader.load(), 1600, 600));


        PrimeController controller = loader.getController();


        primaryStage.show();
        controller.addHandlers();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
