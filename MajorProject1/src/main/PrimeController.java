package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PrimeController {
  @FXML
  TextField txtFName, txtLName, txtAddress, txtCity,
          txtState, txtZip, txtPhone, txtEmail, txtQuant, txtTotal;

  @FXML
  Button btnSubCust, btnClearCust, btnSubOrder, btnClearOrder;

  @FXML
  ComboBox comboCust, comboProd;

  @FXML
  RadioButton radGold, radGob, radStork, radGrem, radSnail;

  @FXML
  CheckBox checkWar;

  @FXML
  TableView custTable, prodTable, orderTable;


  ObservableList<Customer> custList;
  ObservableList<Order> orders;
  ObservableList<Product> products;

  @FXML
  private void initialize() {
    custList = FXCollections.observableArrayList(Customer.parseFile("customers.txt"));
  }
}
