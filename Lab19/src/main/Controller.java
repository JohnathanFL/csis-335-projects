package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
  public PieChart pieChart;
  ObservableList<PieChart.Data> data;

  public void initialize(URL url, ResourceBundle rb) {
    data = FXCollections.observableArrayList(
        new PieChart.Data("Apples", 50),
        new PieChart.Data("Oranges", 25),
        new PieChart.Data("GrapeFruit", 25)
    );

    pieChart.setData(data);
  }
}
