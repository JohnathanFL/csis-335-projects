/**
 * Author: Johnathan Lee
 * Due Date: 09/12/18
 *
 * Calculator:
 *
 * Calculates an employee's pay based on a 40-hour workweek, giving time-and-a-half for overtime, using a GUI.
 **/

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Calculator extends Application {
  public final BigDecimal minForOvertime = new BigDecimal(40);
  public final BigDecimal overtimeMultiplier = new BigDecimal(1.5);
  private final DecimalFormat outFormat = new DecimalFormat("$###,###,###.00");

  private Scene scene;
  private Stage stage;
  private GridPane root;

  private TextField nameField, hoursField, rateField;
  private Text outText;
  private Button calcButton;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    this.stage = stage;
    createLayout();

    this.calcButton.setOnAction(event -> this.validateAndCalc());
  }

  /**
   * createLayout
   *
   * Creates all UI controls and adds them to the scene/stage.
   **/
  private void createLayout() {
    this.root = new GridPane();
    this.scene = new Scene(this.root);

    this.root.setVgap(10);

    { // All fields
      this.nameField = new TextField("name");
      this.root.add(this.nameField, 2, 2);

      this.hoursField = new TextField("hours");
      this.root.add(this.hoursField, 2, 3);

      this.rateField = new TextField("pay rate");
      this.root.add(this.rateField, 2, 4);
    }

    { // Labels
      this.outText = new Text("$");
      this.root.add(outText, 2, 5);

      Label topText = new Label("Employee Information");
      this.root.add(topText, 1, 1);

      this.root.add(new Label("Employee Name: "), 1, 2);
      this.root.add(new Label("Hours: "), 1, 3);
      this.root.add(new Label("Gross Pay: "), 1, 4);
      this.root.add(new Label("Calculated Pay: "), 1, 5);
    }

    this.calcButton = new Button("Calculate Gross Pay");
    this.calcButton.setDefaultButton(true); // Make enter redirect to this button
    this.root.add(this.calcButton, 1, 6);



    stage.setTitle("Pay Rate Calculator");
    stage.setScene(scene);
    stage.show();

  }

  /**
   * First validates hours and rate textfields, then invokes calcPay
   **/
  private void validateAndCalc() {
    boolean invalid = false;
    final String validHoursChars = "0123456789";
    final String validPayChars = validHoursChars + ".";

    BigDecimal hours, grossPay;

    for (char c : this.hoursField.getText().toCharArray())
      if (validHoursChars.indexOf(c) == -1) // If c is not a valid char for this field
        invalid = true;

    for (char c : this.rateField.getText().toCharArray())
      if (validPayChars.indexOf(c) == -1)
        invalid = true;

    if (!invalid)
      calcPay();


  }

  /**
   * Calculates pay using the following formulae:
   *
   * if hours <= 40: hours * rate
   * if hours > 40: (40 * rate) + (hours - 40) * (rate * 1.5)
   *
   * Outputs into the outText attribute.
   **/
  private void calcPay() {
    BigDecimal hours, rate, finalPay;

    hours = new BigDecimal(this.hoursField.getText().trim());
    rate = new BigDecimal(this.rateField.getText().trim());

    BigDecimal overTime = hours.subtract(minForOvertime);
    if(overTime.intValue() > 0)
      // (40 * rate) + (overtime * rate * 1.5)
      finalPay = minForOvertime.multiply(rate).add(overTime.multiply(rate.multiply(overtimeMultiplier)));
    else
      finalPay = hours.multiply(rate);




    this.outText.setText(outFormat.format(finalPay));

    this.rateField.clear();
    this.hoursField.clear();
    this.nameField.clear();

    this.nameField.requestFocus();
  }

}
