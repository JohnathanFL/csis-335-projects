package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.FileSystem;
import java.util.Observable;

public class PrimeController {
  File workingDir;

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
  Slider sliderQuant;

  @FXML
  TableColumn colCustAddr, colCustCell, colCustCity, colCustID, colCustFName, colCustLName, colCustState,
          colCustZip, colCustEmail, colOrderCustName, colOrderPayment, colOrderProdName, colOrderQuant, colOrderShipping,
          colOrderSubtotal, colOrderWarranty, colProdNumInStock, colProdProdID, colProdProdName, colProdUnitCost;

  @FXML
  ToggleGroup shippingGroup, paymentGroup;

  ObservableList<Customer> custList;
  ObservableList<Order> orders;
  ObservableList<Product> products;

  Order protoOrder = new Order(this.custList, this.products);

  void addHandlers() {

    this.initLists();
    this.initTables();

    this.comboCust.setItems(this.custList);
    this.comboProd.setItems(this.products);

    this.btnClearCust.setOnAction(e -> {
      this.txtFName.clear();
      this.txtLName.clear();
      this.txtAddress.clear();
      this.txtCity.clear();
      this.txtState.clear();
      this.txtZip.clear();
      this.txtPhone.clear();
      this.txtEmail.clear();
    });

    this.btnSubCust.setOnAction(e -> {
      // May the mighty Stroustrup forgive us our ugly code, as we forgive those who write ugly code we must read.
      this.custList.add(new Customer(null, this.txtFName.getText(), this.txtLName.getText(),
              this.txtAddress.getText(), this.txtCity.getText(), this.txtState.getText(), this.txtZip.getText(),
              this.txtPhone.getText(), this.txtEmail.getText()));

      this.btnClearCust.fire();
    });

    this.btnClearOrder.setOnAction(e -> {
      this.comboCust.setValue(null);
      this.comboProd.setValue(null);
      this.txtQuant.setText("0");
      this.sliderQuant.setValue(0.0);
      try {
        this.paymentGroup.getSelectedToggle().setSelected(false);
        this.shippingGroup.getSelectedToggle().setSelected(false);
      } catch (Exception ex) {}
      this.checkWar.setSelected(false);
      this.protoOrder = new Order(this.custList, this.products);
    });

    this.btnSubOrder.setOnAction(e -> {

    });
  }

  private void initLists() {
    Window window = txtFName.getScene().getWindow();
    FileChooser chooser = new FileChooser();
    chooser.setInitialDirectory(this.workingDir);
    chooser.setTitle("Choose a Customer DB");

    chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Database Files", ".txt"));
    this.custList = FXCollections.observableArrayList(Customer.parseFile(chooser.showOpenDialog(window)));

    chooser.setTitle("Choose a Products DB");
    this.products = FXCollections.observableArrayList(Product.parseFile(chooser.showOpenDialog(window)));

    chooser.setTitle("Choose an Orders DB");
    this.orders = FXCollections.observableArrayList(Order.parseFile(chooser.showOpenDialog(window), this.custList,
            this.products));
  }

  private void initTables() {
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
      colProdProdName.setCellValueFactory(new PropertyValueFactory<Product, String>("prodName"));
      colProdUnitCost.setCellValueFactory(new PropertyValueFactory<Product, BigDecimal>("unitCostFmt"));
    }

    { // Order display columns
      colOrderCustName.setCellValueFactory(new PropertyValueFactory<Order, String>("fullName"));
      colOrderProdName.setCellValueFactory(new PropertyValueFactory<Order, String>("prodName"));
      colOrderQuant.setCellValueFactory(new PropertyValueFactory<Order, Integer>("quantity"));
      colOrderShipping.setCellValueFactory(new PropertyValueFactory<Order, String>("shippingName"));
      colOrderPayment.setCellValueFactory(new PropertyValueFactory<Order, String>("paymentName"));
      colOrderWarranty.setCellValueFactory(new PropertyValueFactory<Order, Boolean>("hasWarranty"));
      colOrderSubtotal.setCellValueFactory(new PropertyValueFactory<Order, String>("subtotalFmt"));
    }
  }

  @FXML
  private void initialize() {
    workingDir = new File(System.getProperty("user.dir"));
  }
}
