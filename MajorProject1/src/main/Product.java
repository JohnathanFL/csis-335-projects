package main;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Product {
  private static int lastID = 0;

  int prodID, inStock;
  String prodName;
  BigDecimal unitCost;

  public int getProdID() {
    return prodID;
  }

  public void setProdID(int prodID) {
    this.prodID = prodID;
  }

  public int getInStock() {
    return inStock;
  }

  public void setInStock(int inStock) {
    this.inStock = inStock;
  }

  public String getProdName() {
    return prodName;
  }

  public void setProdName(String prodName) {
    this.prodName = prodName;
  }

  public BigDecimal getUnitCost() {
    return unitCost;
  }

  public void setUnitCost(BigDecimal unitCost) {
    this.unitCost = unitCost;
  }

  public static ArrayList<Product> parseFile(String fileName) {
    return new ArrayList<>();
  }
}
