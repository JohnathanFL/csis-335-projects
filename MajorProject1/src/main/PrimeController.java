/**
 * Major Project 1
 * CSIS 335 - GUIs]
 *
 * Author: Johnathan Lee
 * Due: 10/15/18
 *
 * A simple cash register interface. Allows adding customer details and ordering products, while also tracking how
 * many of each product is on hand.
 */

package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.math.BigDecimal;

public class PrimeController {
  private File workingDir; // For filechoosers.

  @FXML
  private TextField txtFName, txtLName, txtAddress, txtCity,
          txtState, txtZip, txtPhone, txtEmail, txtQuant, txtTotal;

  @FXML
  private Button btnSubCust, btnClearCust, btnSubOrder, btnClearOrder;

  @FXML
  private ComboBox comboCust, comboProd;

  @FXML
  private RadioButton radCash, radCred, radNextDay, radTwoDay, radStandard;

  @FXML
  private CheckBox checkWar;

  @FXML
  private TableView custTable, prodTable, orderTable;

  @FXML
  private Slider sliderQuant;

  // Deep breath before trying to say all this without stopping
  @FXML
  private TableColumn colCustAddr, colCustCell, colCustCity, colCustID, colCustFName, colCustLName, colCustState,
          colCustZip, colCustEmail, colOrderCustName, colOrderPayment, colOrderProdName, colOrderQuant, colOrderShipping,
          colOrderSubtotal, colOrderWarranty, colProdNumInStock, colProdProdID, colProdProdName, colProdUnitCost;

  @FXML
  private ToggleGroup shippingGroup, paymentGroup;

  private ObservableList<Customer> custList;
  private ObservableList<Order> orders;
  private ObservableList<Product> products;

  // When changes are made to the order, this is updated to match.
  // When changes are submitted, this is submitted and re-made.
  private Order protoOrder;

  /**
   * Adds handlers for all buttons, as well as handling tables.
   */
  void addHandlers() {

    this.initLists();
    this.initTables();

    this.comboCust.setItems(this.custList);
    this.comboProd.setItems(this.products);

    this.addOrderHandlers();
    this.addCustHandlers();
  }

  /**
   * Loads user selected files into the products/customers/orders databases
   */
  private void initLists() {
    Window window = txtFName.getScene().getWindow();
    FileChooser chooser = new FileChooser();
    chooser.setInitialDirectory(this.workingDir);
    chooser.setTitle("Choose a Customer DB");


    this.custList = FXCollections.observableArrayList(Customer.parseFile(chooser.showOpenDialog(window)));

    chooser.setTitle("Choose a Products DB");
    this.products = FXCollections.observableArrayList(Product.parseFile(chooser.showOpenDialog(window)));

    chooser.setTitle("Choose an Orders DB");
    this.orders = FXCollections.observableArrayList(Order.parseFile(chooser.showOpenDialog(window), this.custList,
            this.products));

    protoOrder = new Order(this.custList, this.products);
  }

  /**
   * Makes all tables point at the proper attributes.
   */
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
      colOrderShipping.setCellValueFactory(new PropertyValueFactory<Order, Order.Shipping>("shipping"));
      colOrderPayment.setCellValueFactory(new PropertyValueFactory<Order, Order.Payment>("payment"));
      colOrderWarranty.setCellValueFactory(new PropertyValueFactory<Order, Boolean>("hasWarranty"));
      colOrderSubtotal.setCellValueFactory(new PropertyValueFactory<Order, String>("subtotalFmt"));
    }
  }

  /**
   * Updates the txtTotal widget with the price of the current order.
   */
  private void updateSubtotal() {
    this.txtTotal.setText(this.protoOrder.getSubtotalFmt());
  }

  /**
   * Adds all onAction handlers for the order screen
   */
  private void addOrderHandlers() {
    final int
            CUST_VALID = 0,
            PROD_VALID = 1,
            SHIPPING_VALID = 2,
            PAYMENT_VALID = 3;

    boolean[] validMask = {false, false, false, false};

    this.btnSubOrder.setDisable(!testAll(validMask));


    {
      this.comboCust.valueProperty().addListener((e, old, newVal) -> {
        validMask[CUST_VALID] = newVal != null; // If a combo's value is null, then nothing is selected

        if(newVal != null)
          protoOrder.setCustID(((Customer)newVal).getCustID());

        if(testAll(validMask)) updateSubtotal();
        this.btnSubOrder.setDisable(!testAll(validMask));
      });
      this.comboProd.valueProperty().addListener((e, old, newVal) -> {
        validMask[PROD_VALID] = newVal != null;

        if(newVal != null)
          protoOrder.setProdID(((Product)newVal).getProdID());

        if(testAll(validMask)) updateSubtotal();
        this.btnSubCust.setDisable(!testAll(validMask));
        if(newVal != null)
          this.sliderQuant.setMax(((Product)newVal).getInStock());
        else
          this.sliderQuant.setMax(0.0);
      });

      this.txtQuant.setText("1");
      this.sliderQuant.setValue(1.0);

      this.sliderQuant.valueProperty().addListener((e, oldVal, newVal) -> {
        this.protoOrder.setQuantity(newVal.intValue());
        this.txtQuant.setText(Integer.toString(newVal.intValue()));
        if(testAll(validMask)) updateSubtotal();
      });

      this.paymentGroup.selectedToggleProperty().addListener((e, old, newV) -> {
        validMask[PAYMENT_VALID] = newV != null;
        if(newV != null)
          protoOrder.setPayment((Order.Payment)newV.getUserData());

        if(testAll(validMask)) updateSubtotal();
        this.btnSubOrder.setDisable(!testAll(validMask));


      });

      this.shippingGroup.selectedToggleProperty().addListener((e, old, newV) -> {
        validMask[SHIPPING_VALID] = newV != null;
        if(newV != null)
          protoOrder.setShipping((Order.Shipping)newV.getUserData());

        if(testAll(validMask)) updateSubtotal();
        this.btnSubOrder.setDisable(!testAll(validMask));


      });

      this.checkWar.selectedProperty().addListener((e, o, newVal) -> {
        this.protoOrder.setHasWarranty(newVal);
        if(testAll(validMask)) updateSubtotal();
      });
    }



    this.btnClearOrder.setOnAction(e -> {
      this.comboCust.setValue(null);
      this.comboProd.setValue(null);
      this.txtQuant.setText("0");
      this.sliderQuant.setValue(0.0);
      try {
        this.paymentGroup.getSelectedToggle().setSelected(false);
        this.shippingGroup.getSelectedToggle().setSelected(false);
      } catch (Exception ex) {
      }
      this.checkWar.setSelected(false);
      this.protoOrder = new Order(this.custList, this.products);
      this.txtTotal.clear();
      this.comboCust.requestFocus();
    });

    this.btnSubOrder.setOnAction(e -> {
      Product curProd = ((Product) this.comboProd.getValue());
      int quant = Integer.parseInt(this.txtQuant.getText());

      this.orders.add(new Order(
              this.custList,
              this.products,
              ((Customer) this.comboCust.getValue()).getCustID(),
              curProd.getProdID(),
              quant,
              (Order.Shipping) this.shippingGroup.getSelectedToggle().getUserData(),
              (Order.Payment) this.paymentGroup.getSelectedToggle().getUserData(),
              this.checkWar.isSelected())
      );

      curProd.setInStock(curProd.getInStock() - quant);
      this.products.remove(curProd);
      this.products.add(curProd);


      this.btnClearOrder.fire();
    });
  }

  /**
   * Adds all onAction hanlders for the customer adder screen.
   */
  private void addCustHandlers() {
    final int
            FNAME_VALID = 0,
            LNAME_VALID = 1,
            ADDR_VALID = 2,
            CITY_VALID = 3,
            STATE_VALID = 4,
            ZIP_VALID = 5,
            PHONE_VALID = 6,
            EMAIL_VALID = 7;

    boolean[] validMask = {false, false, false, false, false, false, false, false};

    this.btnSubCust.setDisable(!testAll(validMask));

    {
      this.txtFName.textProperty().addListener((e, old, newVal) -> {
        validMask[FNAME_VALID] = !newVal.isEmpty();
        this.btnSubCust.setDisable(!testAll(validMask));
      });
      this.txtLName.textProperty().addListener((e, old, newVal) -> {
        validMask[LNAME_VALID] = !newVal.isEmpty();
        this.btnSubCust.setDisable(!testAll(validMask));
      });
      this.txtAddress.textProperty().addListener((e, old, newVal) -> {
        validMask[ADDR_VALID] = !newVal.isEmpty();
        this.btnSubCust.setDisable(!testAll(validMask));
      });
      this.txtCity.textProperty().addListener((e, old, newVal) -> {
        validMask[CITY_VALID] = !newVal.isEmpty();
        this.btnSubCust.setDisable(!testAll(validMask));
      });
      this.txtState.textProperty().addListener((e, old, newVal) -> {
        validMask[STATE_VALID] = !newVal.isEmpty();
        this.btnSubCust.setDisable(!testAll(validMask));
      });
      this.txtZip.textProperty().addListener((e, old, newVal) -> {
        validMask[ZIP_VALID] = !newVal.isEmpty();
        this.btnSubCust.setDisable(!testAll(validMask));
      });
      this.txtPhone.textProperty().addListener((e, old, newVal) -> {
        validMask[PHONE_VALID] = !newVal.isEmpty();
        this.btnSubCust.setDisable(!testAll(validMask));
      });
      this.txtEmail.textProperty().addListener((e, old, newVal) -> {
        validMask[EMAIL_VALID] = !newVal.isEmpty();
        this.btnSubCust.setDisable(!testAll(validMask));
      });
    }
    this.btnClearCust.setOnAction(e -> {
      this.txtFName.clear();
      this.txtLName.clear();
      this.txtAddress.clear();
      this.txtCity.clear();
      this.txtState.clear();
      this.txtZip.clear();
      this.txtPhone.clear();
      this.txtEmail.clear();

      this.txtFName.requestFocus();
    });

    this.btnSubCust.setOnAction(e -> {
      // May the mighty Stroustrup forgive us our ugly code, as we forgive those who write ugly code we must read.
      this.custList.add(new Customer(null, this.txtFName.getText(), this.txtLName.getText(),
              this.txtAddress.getText(), this.txtCity.getText(), this.txtState.getText(), this.txtZip.getText(),
              this.txtPhone.getText(), this.txtEmail.getText()));

      this.btnClearCust.fire();
    });

  }

  /**
   * Checks if an array of bools is all true
   * @param ar The array to check
   * @return Whether EVERY item in the array is true
   */
  public static boolean testAll(boolean[] ar) {
    boolean res = ar[0];

    for (boolean val : ar)
      res = res && val;

    return res;
  }

  /**
   * Saves the databases back to user selected files.
   */
  public void showSavers() {
    Window window = txtFName.getScene().getWindow();
    FileChooser chooser = new FileChooser();
    FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Database files", "*.txt");
    chooser.getExtensionFilters().add(filter);
    chooser.setSelectedExtensionFilter(filter);

    chooser.setInitialDirectory(this.workingDir);

    chooser.setTitle("Choose a Customer DB to save to");
    Customer.serialize(chooser.showSaveDialog(window), this.custList);

    chooser.setTitle("Choose a Products DB to save to");
    Product.serialize(chooser.showSaveDialog(window), this.products);

    chooser.setTitle("Choose an Orders DB to save to");
    Order.serialize(chooser.showSaveDialog(window), this.orders);
  }

  @FXML
  private void initialize() {
    workingDir = new File(System.getProperty("user.dir"));

    radCash.setUserData(Order.Payment.Cash);
    radCred.setUserData(Order.Payment.Credit);
    radNextDay.setUserData(Order.Shipping.NextDay);
    radTwoDay.setUserData(Order.Shipping.TwoDay);
    radStandard.setUserData(Order.Shipping.Standard);

  }
}
