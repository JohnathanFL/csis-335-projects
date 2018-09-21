import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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

    this.stage.setResizable(true);
    this.stage.setTitle("Login");
    this.stage.setScene(loginScene);
    this.stage.show();
  }

  private void createLoginUI() {
    GridPane grid = new GridPane();
    grid.setVgap(2);

    grid.setPadding(new Insets(5,5,5,5));
    grid.add(new Text("Username: "), 0, 0);
    grid.add(new Text("Password: "), 0, 1);

    TextField userField = new TextField();
    PasswordField passField = new PasswordField();


    grid.add(userField, 1, 0);
    grid.add(passField, 1, 1);


    Button loginButton = new Button("Login");
    loginButton.setDefaultButton(true);
    loginButton.setDisable(true);
    loginButton.setMaxWidth(Double.MAX_VALUE);


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

        for (Pair<String, Integer> validPair : this.validLogins) {
          if (validPair.getKey().equals(username) && validPair.getValue() == passHash) {
            userField.clear();
            passField.clear();
            this.stage.setScene(this.adderScene);
            this.stage.setTitle("Add Employees");
            makePopup(Alert.AlertType.INFORMATION, "Login Successful!");

            return;
          }
        }

        // Didn't find a valid username/password combo
        passField.clear();
        userField.clear();
        userField.requestFocus();
        makePopup(Alert.AlertType.ERROR, "Incorrect username and/or password!");
    });


    grid.add(loginButton, 0, 3, 2, 1);


    this.loginScene = new Scene(grid);


  }

  private void makePopup(Alert.AlertType type, String text) {
    Alert alert = new Alert(type);
    alert.setContentText(text);
    alert.showAndWait();
  }
  private void createAdderUI() {
    GridPane grid = new GridPane();
    grid.setVgap(2);
    grid.setPadding(new Insets(5,5,5,5));

    { // Textfield labels
      grid.add(new Text("First Name: "), 0, 0);
      grid.add(new Text("Last Name: "), 0, 1);
      grid.add(new Text("Salary: "), 0, 2);
    }
    // Respectively: First Name, Last Name, Salary
    boolean[] validFields = {false, false, false};
    TextField firstField = new TextField(), lastField = new TextField(), salaryField = new TextField();
    grid.add(firstField, 1, 0);
    grid.add(lastField, 1,1);
    grid.add(salaryField, 1, 2);

    Button submit = new Button("Submit");
    submit.setDisable(true);
    submit.setDefaultButton(true);

    { // Greying out submit button + salary input validation
      firstField.textProperty().addListener((a, b, newText) -> {
        validFields[0] = !newText.isEmpty();
        submit.setDisable(!(validFields[0] && validFields[1] && validFields[2]));
      });

      lastField.textProperty().addListener((a, b, newText) -> {
        validFields[1] = !newText.isEmpty();
        submit.setDisable(!(validFields[0] && validFields[1] && validFields[2]));
      });
      salaryField.textProperty().addListener((a, oldText, newText) -> {
        if (!newText.matches("\\d*"))
          salaryField.setText(oldText);
        validFields[2] = !salaryField.getText().isEmpty();
        submit.setDisable(!(validFields[0] && validFields[1] && validFields[2]));
      });
    }

    submit.setOnAction(e -> {
      this.employees.add(new Employee(firstField.getText(), lastField.getText(), Float.parseFloat(salaryField.getText())));
      System.out.println(this.employees);
      String empty = "";
      firstField.setText(empty);
      lastField.setText(empty);
      salaryField.setText(empty);
      firstField.requestFocus();
    });
    grid.add(submit, 0, 3);

    Button close = new Button("Close");
    close.setOnAction(e -> {
      String empty = "";
      firstField.setText(empty);
      lastField.setText(empty);
      salaryField.setText(empty);
      this.stage.setScene(this.loginScene);
      this.stage.setTitle("Login");
      makePopup(Alert.AlertType.INFORMATION, "Logged out");

    });
    grid.add(close, 1, 3);

    Button display = new Button("Display");
    display.setOnAction(e -> {
      this.stage.setTitle("Employee List");
      this.stage.setScene(displayScene);
    });

    grid.add(new HBox(5, submit, close, display), 0, 3, 3, 1);


    this.adderScene = new Scene(grid);

  }

  private void createDisplayUI() {
    TableView table = new TableView();
    table.setEditable(true);
    TableColumn firstNameCol = new TableColumn("First Name"), lastNameCol = new TableColumn("Last Name"), salaryCol =
            new TableColumn("Salary"), bonusCol = new TableColumn("Bonus Rate");
    table.setEditable(true);
    firstNameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("firstName"));
    lastNameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("lastName"));
    salaryCol.setCellValueFactory(new PropertyValueFactory<Employee, Float>("salary"));
    bonusCol.setCellValueFactory(new PropertyValueFactory<Employee, Float>("bonusRate"));

    table.setItems(this.employees);
    table.getColumns().addAll(firstNameCol, lastNameCol, salaryCol, bonusCol);

    Button backBtn = new Button("Back");
    backBtn.setDefaultButton(true);
    backBtn.setOnAction(e -> {
      this.stage.setTitle("Add Employees");
      this.stage.setScene(adderScene);
    });
    backBtn.setAlignment(Pos.CENTER);
    backBtn.setMaxWidth(Double.MAX_VALUE);
    backBtn.setPadding(new Insets(5,0,5,0));

    VBox vb = new VBox();
    vb.getChildren().addAll(table, backBtn);
    vb.setFillWidth(true);
    vb.setPrefWidth(500);



    this.displayScene = new Scene(vb);
  }

}
