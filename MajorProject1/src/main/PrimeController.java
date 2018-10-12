package main;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;

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


  @FXML
      TableColumn colCustAddr, colCustCell, colCustCity, colCustID, colCustFName, colCustLName, colCustState,
      colCustZip, colCustEmail, colOrderCustName, colOrderPayment, colOrderProdName, colOrderQuant, colOrderShipping,
      colOrderSubtotal, colOrderWarranty, colProdNumInStock, colProdProdID, colProdProdName, colProdUnitCost;

  ObservableList<Customer> custList;
  ObservableList<Order> orders;
  ObservableList<Product> products;

  @FXML
  private void initialize() {
    this.custList = FXCollections.observableArrayList(Customer.parseFile("customers.txt"));
    this.products = FXCollections.observableArrayList(Product.parseFile("products.txt"));
    this.orders = FXCollections.observableArrayList(Order.parseFile("orders.txt"));

    this.custTable.setItems(this.custList);
    this.orderTable.setItems(this.orders);
    this.prodTable.setItems(this.products);

    { // Customer display columns
      colCustID.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("custID"));
      colCustFName.setCellValueFactory(new PropertyValueFactory<Customer, String>("firstName"));
      colCustLName.setCellValueFactory(new PropertyValueFactory<Customer, String>("lastName"));
      colCustAddr.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
      colCustCity.setCellValueFactory(new PropertyValueFactory<Customer, String>("city"));
      colCustState.setCellValueFactory(new PropertyValueFactory<Customer, String>("state"));
      colCustZip.setCellValueFactory(new PropertyValueFactory<Customer, String>("zip"));
      colCustCell.setCellValueFactory(new PropertyValueFactory<Customer, String>("cell"));
      colCustEmail.setCellValueFactory(new PropertyValueFactory<Customer, String>("email"));
    }

    { // Product display columns
      colProdProdID.setCellValueFactory(new PropertyValueFactory<Product, Integer>("prodID"));
      colProdNumInStock.setCellValueFactory(new PropertyValueFactory<Product, Integer>("inStock"));
      colProdProdName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
      colProdUnitCost.setCellValueFactory(new PropertyValueFactory<Product, BigDecimal>("inStock"));
    }

    { // Order display columns
      colOrderCustName.setCellValueFactory(new PropertyValueFactory<Order, String>("fullName"));
      colOrderProdName.setCellValueFactory(new PropertyValueFactory<Order,String>("prodName"));
      colOrderQuant.setCellValueFactory(new PropertyValueFactory<Order, Integer>("quantity"));
      colOrderShipping.setCellValueFactory(new PropertyValueFactory<Order, String>("shippingName"));
      colOrderPayment.setCellValueFactory(new PropertyValueFactory<Order, String>("paymentName"));
      colOrderWarranty.setCellValueFactory(new PropertyValueFactory<Order, Boolean>("hasWarranty"));
      colOrderSubtotal.setCellValueFactory(new PropertyValueFactory<Order, BigDecimal>("subtotal"));
    }


  }
}
