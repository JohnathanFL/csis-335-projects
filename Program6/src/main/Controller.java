package main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import jdk.nashorn.internal.runtime.ECMAException;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;

class Customer {

  public Customer(String id, String first, String last, String phone) {
    this.id = id;
    this.first = first;
    this.last = last;
    this.phone = phone;
  }

  String id, first, last, phone;

  @Override
  public String toString() {
    return String.format("ID: %s, Name: %s, %s, Phone: %s", this.id, this.last, this.first, this.phone);
  }
}

class Product {
  public Product(String id, String desc, String cost, String inStock) {
    this.id = id;
    this.desc = desc;
    this.cost = cost;
    this.inStock = inStock;
  }

  @Override
  public String toString() {
    return String.format("ID: %s, Desc: %s, Cost: %s, Quantity on Hand: %s", this.id, this.desc, this.cost, this.inStock);
  }

  String id, desc, cost, inStock;
}


public class Controller {
  @FXML
  Button addCustBtn, addProdBtn,
      prevProdBtn, nextProdBtn, prevCustBtn, nextCustBtn,
      refreshBtn;

  @FXML
  Label prodLbl, custLbl;

  @FXML
  TextField prodDescField, unitCostField, qtyField, firstNameField, lastNameField, phoneField;

  int curCustIndex = 0, curProdIndex = 0;
  int numCust = 0, numProd = 0;

  ArrayList<Customer> customers = new ArrayList<>();
  ArrayList<Product> products = new ArrayList<>();

  Connection conn;

  public void refresh() {


    ResultSet customers, products;
    try {
      Statement statement = this.conn.createStatement();
      customers = statement.executeQuery("SELECT * FROM Customer");


      this.customers.clear();
      while (customers.next())
        this.customers.add(new Customer(customers.getString(1), customers.getString(2), customers.getString(3), customers.getString(4)));

      products = statement.executeQuery("SELECT * FROM Product");

      this.products.clear();
      while (products.next())
        this.products.add(new Product(products.getString(1), products.getString(2), products.getString(3), products.getString(4)));

    } catch (SQLException ex) {
      System.out.println("Failed to execute queries");
      System.out.println(ex);
      return;
    }

    this.numCust = this.customers.size();
    this.numProd = this.products.size();

  }

  public void updateLabels() {
    this.prodLbl.setText(this.products.get(this.curProdIndex).toString());
    this.custLbl.setText(this.customers.get(this.curCustIndex).toString());
  }

  public int clamp(int num, int min, int max) {
    return (num > max ? max : (num < min ? min : num));
  }

  public boolean testAll(boolean[] bools) {
    boolean res = bools[0];
    for (boolean b : bools)
      res = res && b;

    return res;
  }

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
    } catch (SQLException e) {
      System.out.println(e);
    }

    refresh();
    updateLabels();
    refreshBtn.setOnAction(e -> refresh());

    nextProdBtn.setOnAction(e -> {
      this.curProdIndex = (this.curProdIndex + 1) % this.numProd;
      updateLabels();
    });
    prevProdBtn.setOnAction(e -> {
      this.curProdIndex = ((this.curProdIndex - 1) + this.numProd) % this.numProd;
      updateLabels();
    });

    nextCustBtn.setOnAction(e -> {
      this.curCustIndex = (this.curCustIndex + 1) % this.numCust;
      updateLabels();
    });
    prevCustBtn.setOnAction(e -> {
      this.curCustIndex = ((this.curCustIndex - 1) + this.numCust) % this.numCust;
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
        Statement stmt = this.conn.createStatement();
        String query = String.format("INSERT INTO Customer (firstName, lastName, phone) VALUES (\"%s\", \"%s\", \"%s\");", this.firstNameField.getText(), this.lastNameField.getText(), this.phoneField.getText());
        System.out.println(query);
        stmt.execute(query);


        refresh();
      } catch (Exception ex) {
        System.out.println(ex);
      }
      clears();
    });

    this.addProdBtn.setOnAction(e -> {
      try {
        Statement stmt = this.conn.createStatement();
        stmt.execute(String.format("INSERT INTO Product (prodDesc, unitCost, qtyOnHand) VALUES (\"%s\", \"%s\", \"%s\");", this.prodDescField.getText(), this.unitCostField.getText(), this.qtyField.getText()));
        refresh();
      } catch (Exception ex) {
        System.out.println(ex);
      }
      clears();

    });

  }
}
