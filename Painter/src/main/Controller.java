package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Controller {
  @FXML
  RadioButton blackRadioBtn, redRadioBtn, greenRadioBtn, blueRadioBtn, smallRadioBtn, mediumRadioBtn, largeRadioBtn;
  @FXML
  AnchorPane drawingAnchorPane;
  @FXML
  ToggleGroup drawColor, sizeToggleGroup;

  private PenSize radius = PenSize.MEDIUM;
  private Paint brushColor = Color.BLACK;


  public void initialize() {
    blackRadioBtn.setUserData(Color.BLACK);
    redRadioBtn.setUserData(Color.RED);
    greenRadioBtn.setUserData(Color.GREEN);
    blueRadioBtn.setUserData(Color.BLUE);

    smallRadioBtn.setUserData(PenSize.SMALL);
    mediumRadioBtn.setUserData(PenSize.MEDIUM);
    largeRadioBtn.setUserData(PenSize.LARGE);

    this.drawColor.selectedToggleProperty().addListener((e, oldVal, newVal) -> this.colorRadioButtonSelected());
    this.sizeToggleGroup.selectedToggleProperty().addListener((e, oldVal, newVal) -> this.sizeRadioButtonSelected());

  }

  @FXML
  private void drawingAreaMouseDragged(MouseEvent e) {
    Circle newCircle = new Circle(e.getX(), e.getY(), this.radius.getRadius(), this.brushColor);
    drawingAnchorPane.getChildren().add(newCircle);
  }


  public void colorRadioButtonSelected() {
    this.brushColor = (Color)this.drawColor.getSelectedToggle().getUserData();
  }

  @FXML
  private void sizeRadioButtonSelected() {
    this.radius = (PenSize) this.sizeToggleGroup.getSelectedToggle().getUserData();
  }

  @FXML
  private void undoButtonPressed(ActionEvent e) {
    int count = drawingAnchorPane.getChildren().size();
    if(count > 0)
      drawingAnchorPane.getChildren().remove(count - 1);
  }

  @FXML
  private void clearButtonPressed(ActionEvent e) {
    drawingAnchorPane.getChildren().clear();
  }

  private enum PenSize {
    SMALL(2), MEDIUM(4), LARGE(6);

    private final int radius;

    PenSize(int radius) {
      this.radius = radius;
    }

    public int getRadius() {
      return radius;
    }
  }

}
