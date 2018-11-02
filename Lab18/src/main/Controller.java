package main;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Controller {
  static ResultSet rs;
  static Connection conn;

  @FXML
  public TableView<AddressInfo> tblView;
  @FXML
  public TableColumn<AddressInfo, Integer> colID;
  @FXML
  public  TableColumn<AddressInfo, String> colFirstName;
  @FXML
  public  TableColumn<AddressInfo, String> colLastName;
  @FXML
  public  TableColumn<AddressInfo, String> colEmail;
  @FXML
  public  TableColumn<AddressInfo, String> colPhone;

  ObservableList<AddressInfo> addresses;

  public void refresh() throws SQLException {
    rs = conn.createStatement().executeQuery("select * from Addresses");

    addresses.clear();
    while(rs.next()) {
      addresses.add(new AddressInfo(rs));
    }
  }

  public void initialize() {
    final String url = "jdbc:mysql://localhost:3306/leejo_addressbook?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false", username = "leejo", password = "OverlyUnderlyPoweredMS";
    addresses = FXCollections.observableArrayList();
    try {
      conn = DriverManager.getConnection(url, username, password);
      refresh();
    } catch (SQLException ex) {
      System.out.println(ex);
    }

    colID.setCellValueFactory(new PropertyValueFactory<AddressInfo, Integer>("id"));
    colFirstName.setCellValueFactory(new PropertyValueFactory<AddressInfo, String>("firstName"));
    colLastName.setCellValueFactory(new PropertyValueFactory<AddressInfo, String>("lastName"));
    colEmail.setCellValueFactory(new PropertyValueFactory<AddressInfo, String>("email"));
    colPhone.setCellValueFactory(new PropertyValueFactory<AddressInfo, String>("phone"));
    tblView.setItems(addresses);
  }
}
