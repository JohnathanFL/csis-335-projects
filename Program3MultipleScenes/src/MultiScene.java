import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;

public class MultiScene extends Application {
  private ObservableList<Employee> employees;
  private ArrayList<Pair<String, Integer>> validLogins;
  private Stage stage;
  private Scene loginScene, adderScene, displayScene;


  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primStage) {
    this.validLogins = new ArrayList<>(1);
    this.validLogins.add(new Pair<>("admin", /* I don't intend to ever even start such bad practices*/"guest".hashCode()));
    this.employees = FXCollections.observableArrayList();

    this.stage = primStage;
    this.createLoginUI();
    this.createAdderUI();
    this.createDisplayUI();


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


    // in order: UserName is valid, Password is valid,
    boolean[] canClickFlags = {false, false};

    userField.textProperty().addListener((o, oldText, newText) -> {
      canClickFlags[0] = !newText.isEmpty();
      loginButton.setDisable(!(canClickFlags[0] && canClickFlags[1]));
    });
    passField.textProperty().addListener((o, oldText, newText) -> {
      canClickFlags[1] = !newText.isEmpty();
      loginButton.setDisable(!(canClickFlags[0] && canClickFlags[1]));
    });

    loginButton.setOnAction(e -> {
        String username = userField.getText();
        int passHash = passField.getText().hashCode();

        for (Pair<String, Integer> validPair : this.validLogins)
          if (validPair.getKey().equals(username) && validPair.getValue() == passHash) {
            loginSuccessful();
            return;
          }

          // Didn't find a valid username/password combo
        loginFailed();
    });


    grid.add(loginButton, 0, 3, 2, 1);


    this.loginScene = new Scene(grid);


  }

  private void loginFailed() {}
  private void loginSuccessful(){
    Stage popup = new Stage();

    popup.setTitle("Login success!");
    popup.initModality(Modality.APPLICATION_MODAL);
    popup.initOwner(this.stage);

    GridPane grid = new GridPane();

    grid.add(new Text("Login successful!"), 0,0);

    Button accept = new Button("Ok");
    accept.setOnAction(e -> {
      this.stage.setScene(adderScene);
      popup.close();
    });
    accept.setDefaultButton(true);
    grid.add(accept, 1,0);

    popup.setScene(new Scene(grid));
    popup.show();
  }
  private void createAdderUI() {
    GridPane grid = new GridPane();

    grid.add(new Text("First Name: "), 0,0);
    grid.add(new Text("Last Name: "), 0,1);

    // Respectively: First Name, Last Name, Salary
    boolean[] validFields = {false, false, false};
    TextField firstField = new TextField(), lastField = new TextField(), salaryField = new TextField();


    grid.add(firstField, 1, 0);
    grid.add(lastField, 1,1);
    grid.add(salaryField, 1, 2);

    Button submit = new Button("Submit");

    firstField.textProperty().addListener((a,b, newText) -> {
      validFields[0] = !newText.isEmpty();
      submit.setDisable(!(validFields[0] && validFields[1] && validFields[2]));
    });

    lastField.textProperty().addListener((a,b,newText) -> {
      validFields[1] = !newText.isEmpty();
      submit.setDisable(!(validFields[0] && validFields[1] && validFields[2]));
    });
    salaryField.textProperty().addListener((a,b,newText) -> {
      validFields[2] = !newText.isEmpty();
      submit.setDisable(!(validFields[0] && validFields[1] && validFields[2]));
    });

    submit.setOnAction(e -> {
      this.employees.add(new Employee(firstField.getText(), lastField.getText(), Float.parseFloat(salaryField.getText())));
      System.out.println(this.employees);
      String empty = "";
      firstField.setText(empty);
      lastField.setText(empty);
      salaryField.setText(empty);
    });
    grid.add(submit, 0, 3);

    Button close = new Button("Close");
    close.setOnAction(e -> {
      String empty = "";
      firstField.setText(empty);
      lastField.setText(empty);
      salaryField.setText(empty);
      this.stage.setScene(this.loginScene);
    });
    grid.add(close, 1, 3);

    Button display = new Button("Display");
    display.setOnAction(e -> {
      this.stage.setScene(displayScene);
    });

    grid.add(display, 3,3);


    this.adderScene = new Scene(grid);

  }

  private void createDisplayUI() {
    TableView table = new TableView();

    TableColumn firstNameCol = new TableColumn("First Name"), lastNameCol = new TableColumn("Last Name"), salaryCol = new TableColumn("Salary");
    table.setEditable(true);
    firstNameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("firstName"));
    lastNameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("lastName"));
    salaryCol.setCellValueFactory(new PropertyValueFactory<Employee, Float>("salary"));

    table.setItems(this.employees);
    table.getColumns().addAll(firstNameCol, lastNameCol, salaryCol);


    VBox vb = new VBox();
    vb.getChildren().add(table);



    this.displayScene = new Scene(vb);
  }

}
