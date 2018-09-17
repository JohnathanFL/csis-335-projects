/**
 * Author: Johnathan Lee
 * Program 2
 * Due Date: 09/12/18
 * Handed in 9/12/18
 *
 * Calculator:
 *
 * Calculates an employee's pay based on a 40-hour workweek, giving time-and-a-half for overtime, using a GUI.
 **/

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Calculator extends Application {
  public final BigDecimal minForOvertime = new BigDecimal(40);
  public final BigDecimal overtimeMultiplier = new BigDecimal(1.5);
  private final DecimalFormat outFormat = new DecimalFormat("$###,###,###.00"); //>! Applied to outText
  private final double globalInset = 10.0;

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

    this.calcButton.setOnAction(event -> {
      this.validateAndCalc();
      this.reset();
    });
  }

  /**
   * createLayout
   *
   * Creates all UI controls and adds them to the scene/stage.
   *
   * Also sets a validator lambda on the rate and hours fields.
   **/
  private void createLayout() {
    this.root = new GridPane();
    this.root.setPadding(new Insets(globalInset,globalInset,globalInset,globalInset));

    this.scene = new Scene(this.root);

    this.root.setVgap(10);

    { // All fields
      this.nameField = new TextField("");
      this.root.add(this.nameField, 2, 2);
      // No special format rules for this, as it's not even used.

      this.hoursField = new TextField("");
      this.root.add(this.hoursField, 2, 3);
      this.hoursField.textProperty().addListener((ob, oldVal, newVal) -> {
        if(!newVal.matches("^(\\d+(\\.\\d*)?)?$"))
          this.hoursField.setText(oldVal);
      });


      this.rateField = new TextField("");
      this.rateField.textProperty().addListener((ob, oldVal, newVal) -> {
        if(!newVal.matches("(^(\\d+)(\\.\\d{0,2})?$)|(^$)"))
          this.rateField.setText(oldVal);
      });
      this.root.add(this.rateField, 2, 4);
    }

    { // Labels
      this.outText = new Text("$...");
      this.root.add(outText, 2, 5);

      Text topText = new Text("Employee Information");
      topText.setFont(Font.font("Sans Serif", FontWeight.BOLD, 18.0));
      // Unfortunately, this doesn't seem to do anything even with a textalignment.center
      this.root.add(topText, 1, 1, 2, 1);

      this.root.add(new Text("Employee Name: "), 1, 2);
      this.root.add(new Text("Hours: "), 1, 3);
      this.root.add(new Text("Gross Pay: "), 1, 4);
      this.root.add(new Text("Calculated Pay: "), 1, 5);
    }

    // And finally, the button
    this.calcButton = new Button("Calculate Gross Pay");
    this.calcButton.setDefaultButton(true); // Make enter redirect to this button
    this.root.add(this.calcButton, 1, 6);



    stage.setTitle("Pay Rate Calculator");
    stage.setScene(scene);
    stage.show();
  }

  /**
   * A reimplementation of String.replace for TextField
   * Replaces occurrences of pattern in field's text with newVal
   *
   * @param field The field to replace text in.
   * @param pattern The pattern to find
   * @param newVal The value to replace pattern with.
   **/
  private static void replace(TextField field, String pattern, String newVal) {
    field.setText(field.getText().replace(pattern, newVal));
  }

  /**
   * Removes any commas from hoursField and rateField, as BigDecimal somehow doesn't handle those.
   * Despite it supposedly parsing numbers. After replacing, it calls calcPay
   **/
  private void validateAndCalc() {
    // If there's nothing there, no point in getting exceptions from BigDecimal
    if(!this.hoursField.getText().isEmpty() && !this.rateField.getText().isEmpty()) {

      // Needed since I allow for commas in my fields.
      replace(this.hoursField, ",", "");
      replace(this.rateField, ",", "");

      calcPay();
    }
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
  }


  /**
   * Resets all fields and sets focus to the nameField
   */
  private void reset() {
    this.rateField.clear();
    this.hoursField.clear();
    this.nameField.clear();

    this.nameField.requestFocus();
  }

}
