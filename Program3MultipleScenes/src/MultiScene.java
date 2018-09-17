import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.ArrayList;

public class MultiScene extends Application {
  private ArrayList<Employee> employees;
  private ArrayList<Pair<String, Integer>> validLogins;
  private Stage stage;
  private Scene loginScene, adderScene, displayScene;


  public static void main(String[] args) {
    launch(args);
  }

  private void createDisplayUI() {
  }

  @Override
  public void start(Stage primStage) {
    this.validLogins = new ArrayList<>(1);
    this.validLogins.add(new Pair<>("admin", "guest".hashCode()));
    System.out.println("guest".hashCode());

    createLoginUI();
    createAdderUI();

    this.stage = primStage;
    this.stage.setScene(loginScene);
    this.stage.show();
  }

  private void createLoginUI() {
    GridPane grid = new GridPane();
    grid.add(new Text("Username: "), 0, 0);
    grid.add(new Text("Password: "), 0, 1);

    TextField userField = new TextField();
    PasswordField passField = new PasswordField();


    grid.add(userField, 1, 0);
    grid.add(passField, 1, 1);


    Button loginButton = new Button("Login");
    loginButton.setDefaultButton(true);
    loginButton.setDisable(true);


    // in order: UserName is valid, Password is valid, entire form is valid so can login
    boolean[] canClickFlags = {false, false, false};


    userField.textProperty().addListener((o, oldText, newText) -> {
      canClickFlags[0] = !newText.isEmpty();

      canClickFlags[2] = canClickFlags[0] && canClickFlags[1];
      loginButton.setDisable(!canClickFlags[2]);

      System.out.println(canClickFlags[0]);
    });
    passField.textProperty().addListener((o, oldText, newText) -> {
      canClickFlags[1] = !newText.isEmpty();

      canClickFlags[2] = canClickFlags[0] && canClickFlags[1];
      loginButton.setDisable(!canClickFlags[2]);
    });

    loginButton.setOnAction(e -> {
      if (canClickFlags[2]) {
        String username = userField.getText();
        int passHash = passField.getText().hashCode();

        for (Pair<String, Integer> validPair : this.validLogins)
          if (validPair.getKey().equals(username) && validPair.getValue().intValue() == passHash) {
            login();
            return;
          }
      }
    });


    grid.add(loginButton, 0, 3, 2, 1);


    this.loginScene = new Scene(grid);


  }

  private void createAdderUI() {
  }

  private void login() {
    System.out.println("Got to login!");
  }

}
