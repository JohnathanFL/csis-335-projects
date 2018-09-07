import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Calculator extends Application {
  public final BigDecimal minForOvertime = new BigDecimal(40);
  public final BigDecimal overtimeMultiplier = new BigDecimal(1.5);
  private final MathContext roundingCtx = new MathContext(2, RoundingMode.UP);

  private Scene scene;
  private Stage stage;
  private GridPane root;

  private TextField nameField, hoursField, rateField;
  private Label outLabel;
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
      this.outLabel = new Label("$");
      this.root.add(outLabel, 2, 5);

      Label topText = new Label("Employee Information");
      this.root.add(topText, 1, 1);

      this.root.add(new Label("Employee Name: "), 1, 2);
      this.root.add(new Label("Hours: "), 1, 3);
      this.root.add(new Label("Gross Pay: "), 1, 4);
      this.root.add(new Label("Calculated Pay: "), 1, 5);
    }

    this.calcButton = new Button("Calculate Gross Pay");
    this.root.add(this.calcButton, 1, 6);


    stage.setTitle("Pay Rate Calculator");
    stage.setScene(scene);
    stage.show();

  }

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


    finalPay.round(roundingCtx);

    this.outLabel.setText("$" + finalPay.toString());
  }

}
