package main;

import Connectivity.ConnectionClass;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Controller {
  @FXML
  public Button btnShowFirst;
  @FXML
  public Label lblFirst;

  public void initialize() {
    ConnectionClass conn = new ConnectionClass();

    btnShowFirst.setOnAction(e -> {
      try {
        Statement s = conn.getConnection().createStatement();
        ResultSet res = s.executeQuery("select FirstName from Addresses;");
        res.next();
        lblFirst.setText(res.getString(1));
      } catch (SQLException ex) {
        System.out.println(ex);
      }
    });
  }
}
