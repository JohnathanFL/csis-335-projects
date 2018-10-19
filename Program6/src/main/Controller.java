package main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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

  int currentIndex = 0;

  ArrayList<Customer> customers = new ArrayList<>();
  ArrayList<Product> products = new ArrayList<>();

  public void refresh() {
    Connection conn;
    try {
      conn = DriverManager.getConnection("jdbc:mysql://puff.mnstate.edu:3306/leejo_prog6?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false", "leejo", "OverlyUnderlyPoweredMS");
    } catch (SQLException ex) {
      System.out.println("Failed to connect to DB. Error:");
      System.out.println(ex);
      return;
    }


    ResultSet customers, products;
    try {
      Statement statement = conn.createStatement();
      customers = statement.executeQuery("SELECT * FROM Customer");


      this.customers.clear();
      while(customers.next())
        this.customers.add(new Customer(customers.getString(1), customers.getString(2), customers.getString(3), customers.getString(4)));

      products = statement.executeQuery("SELECT * FROM Product");

      this.products.clear();
      while(products.next())
        this.products.add(new Product(products.getString(1), products.getString(2), products.getString(3), products.getString(4)));

    } catch (SQLException ex) {
      System.out.println("Failed to execute queries");
      System.out.println(ex);
      return;
    }

    System.out.println(this.customers);
    System.out.println(this.products);
  }

  @FXML
  public void initialize() {
    refresh();
    refreshBtn.setOnAction(e -> refresh());


  }
}
