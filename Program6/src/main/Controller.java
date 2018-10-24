/**
 * Author: Johnathan Lee
 * MSUM CSIS 335 - Program 6
 *
 * Due 10/26/18
 *
 * Connects to a databse of customers and products, then retrieves a local copy and allows the user to move through/update it.
 */
package main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.*;

/**
 * Basic rundown for how this all works:
 *
 * 1. Connect to the database, pull down all info and store it in local ArrayLists of Customers/Products
 * 2. Update labels to make it work
 * 3. User clicks...
 *    3.1 Refresh -> Goes back to step 1
 *    3.2 Submit customer -> INSERT INTO's a new customer, goes back to step 1
 *    3.3 Submit Product -> INSERT INTO's a new Product, goes back to step 1
 */
public class Controller {
  @FXML
  Button addCustBtn, addProdBtn,
      prevProdBtn, nextProdBtn, prevCustBtn, nextCustBtn,
      refreshBtn;

  @FXML
  Label prodIDLbl, custIDLbl, custFirstLbl, custLastLbl, custPhoneLbl, prodDescLbl, prodCostLbl, prodStockLbl;

  @FXML
  TextField prodDescField, unitCostField, qtyField, firstNameField, lastNameField, phoneField;


  Connection conn;

  PreparedStatement custSelector, prodSelector, custAdder, prodAdder;

  ResultSet customers, products;

  /**
   * Pulls down all information from the server, saving us from having to make a new query for every click
   */
  public void refresh() {


    try {
      this.customers = this.custSelector.executeQuery();
      this.products = this.prodSelector.executeQuery();

      this.customers.next();
      this.products.next();

    } catch (SQLException ex) {
      System.out.println("Failed to execute queries");
      System.out.println(ex);
      return;
    }

    updateLabels();

  }

  /**
   * Updates labels depending on the current customer/product index
   */
  public void updateLabels() {

    try {
      this.custIDLbl.setText(this.customers.getString(1));
      this.custFirstLbl.setText(this.customers.getString(2));
      this.custLastLbl.setText(this.customers.getString(3));
      this.custPhoneLbl.setText(this.customers.getString(4));

      this.prodIDLbl.setText(this.products.getString(1));
      this.prodDescLbl.setText(this.products.getString(2));
      this.prodCostLbl.setText(this.products.getString(3));
      this.prodStockLbl.setText(this.products.getString(4));
    } catch (SQLException e) {
      System.out.println(e);
    }
  }

  /**
   * Tests an array to see if it's all true.
   * Really need to move to a bitset
   * @param bools Array to test
   * @return if the array is all true
   */
  public boolean testAll(boolean[] bools) {
    boolean res = bools[0];
    for (boolean b : bools)
      res = res && b;

    return res;
  }

  /**
   * Clear all fields
   */
  public void clears() {
    this.qtyField.clear();
    this.unitCostField.clear();
    this.phoneField.clear();
    this.firstNameField.clear();
    this.prodDescField.clear();
    this.lastNameField.clear();
  }

  @FXML
  public void initialize() {

    try {
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/leejo_prog6?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false", "leejo", "OverlyUnderlyPoweredMS");
      this.custSelector = this.conn.prepareStatement("SELECT * FROM Customer;");
      this.prodSelector = this.conn.prepareStatement("SELECT * FROM Product;");
      this.custAdder = this.conn.prepareStatement("INSERT INTO Customer (firstName, lastName, phone) VALUES (?, ?, ?);");
      this.prodAdder = this.conn.prepareStatement("INSERT INTO Product (prodDesc, unitCost, qtyOnHand) VALUES (?, ?, ?);");
    } catch (SQLException e) {
      System.out.println(e);
    }

    refresh();
    updateLabels();
    refreshBtn.setOnAction(e -> refresh());

    nextProdBtn.setOnAction(e -> {
      try {
        if(!this.products.next())
          this.products.absolute(1);
      } catch (SQLException ex) {
        System.out.println(ex);
      }
      updateLabels();
    });
    prevProdBtn.setOnAction(e -> {
      try {
        if(!this.products.previous())
          this.products.last();
      } catch (SQLException ex) {
        System.out.println(ex);
      }
      updateLabels();
    });

    nextCustBtn.setOnAction(e -> {
      try {
        if(!this.customers.next())
          this.customers.absolute(1);
      } catch (SQLException ex) {
        System.out.println(ex);
      }
      updateLabels();
    });
    prevCustBtn.setOnAction(e -> {
      try {
        if(!this.customers.previous())
          this.customers.last();
      } catch (SQLException ex) {
        System.out.println(ex);
      }
      updateLabels();
    });


    {
      boolean[] valids = {false, false, false};
      this.firstNameField.textProperty().addListener((e, o, newVal) -> {
        valids[0] = !newVal.isEmpty();
        this.addCustBtn.setDisable(!testAll(valids));
      });
      this.lastNameField.textProperty().addListener((e, o, newVal) -> {
        valids[1] = !newVal.isEmpty();
        this.addCustBtn.setDisable(!testAll(valids));
      });
      this.phoneField.textProperty().addListener((e, o, newVal) -> {
        valids[2] = !newVal.isEmpty();
        this.addCustBtn.setDisable(!testAll(valids));
      });
    }
    {
      boolean[] valids = {false, false, false};
      this.prodDescField.textProperty().addListener((e, o, newVal) -> {
        valids[0] = !newVal.isEmpty();
        this.addProdBtn.setDisable(!testAll(valids));
      });
      this.unitCostField.textProperty().addListener((e, o, newVal) -> {
        if(!newVal.matches("(\\d+\\.?\\d*)?")) {
          System.out.println(newVal);
          this.unitCostField.setText(o);
        }
        else {
          valids[1] = !newVal.isEmpty();
          this.addProdBtn.setDisable(!testAll(valids));
        }
      });
      this.qtyField.textProperty().addListener((e, o, newVal) -> {
        if(!newVal.matches("\\d*")) {
          System.out.println(newVal);
          this.qtyField.setText(o);
        }
        else {

          valids[2] = !newVal.isEmpty();
          this.addProdBtn.setDisable(!testAll(valids));
        }
      });
    }

    this.addProdBtn.setDisable(true);
    this.addCustBtn.setDisable(true);

    this.addCustBtn.setOnAction(e -> {
      try {
        setAll(this.custAdder, this.firstNameField.getText(), this.lastNameField.getText(), this.phoneField.getText());
        System.out.println(this.custAdder);
        this.custAdder.execute();
        refresh();
      } catch (Exception ex) {
        System.out.println(ex);
      }
      clears();
    });

    this.addProdBtn.setOnAction(e -> {
      try {
        setAll(this.prodAdder, this.prodDescField.getText(), this.unitCostField.getText(), this.qtyField.getText());
        System.out.println(this.prodAdder);
        this.prodAdder.execute();
        refresh();
      } catch (Exception ex) {
        System.out.println(ex);
      }
      clears();
    });

  }

  /**
   * Sugar for setting a bunch of parameters for a PreparedStatement
   * @param stmt Statement to set params on
   * @param things All things to set in stmt. First entered is index=1
   */
  public static void setAll(PreparedStatement stmt, Object ...things) {
    try {
      int i = 0;
      for (Object thing : things) {
        stmt.setObject(++i, thing);
      }
    } catch (SQLException e) {
      System.out.println(e);
    }
  }
}
