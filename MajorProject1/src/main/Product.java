package main;

import javafx.collections.ObservableList;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Product {
  private static final Pattern parsePattern = Pattern.compile("([^\\|]+)\\|([^\\|]+)\\|([^\\|]+)\\|([^\\|]+)");
  private static final DecimalFormat fmt = new DecimalFormat("$###,###.00");

  private static int lastID = 0;

  int prodID, inStock;
  String prodName;
  BigDecimal unitCost;

  public String getUnitCostFmt() {
    return fmt.format(this.getUnitCost().doubleValue());
  }

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

  @Override
  public String toString() {
    return "[ID: " + this.getProdID() + "] " + this.prodName;
  }

  public static ArrayList<Product> parseFile(File inFile) {
    ArrayList<Product> res = new ArrayList<>();
    try {

      BufferedReader reader = new BufferedReader(new FileReader(inFile));

      System.out.println(reader);

      String line;
      while (((line = reader.readLine()) != null) && !line.trim().isEmpty()) {
        line = line.trim();
        Matcher matcher = parsePattern.matcher(line);

        matcher.matches(); // Won't run unless I check if it matches... Go figure.

        System.out.println(matcher.group(4));
        res.add(new Product(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), matcher.group(3),
            new BigDecimal(matcher.group(4))));
      }

    } catch (IOException e) {
      System.out.println("Failed to load " + inFile + " after exception " + e.toString());
    }

    return res;
  }

  public String serialize() {
    return String.format("%s|%s|%s|%s", this.prodID, this.inStock, this.prodName, this.unitCost);
  }

  public static void serialize(File outFile, ObservableList<Product> list) {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(outFile, false));
      for (Product prod : list)
        writer.write(prod.serialize() + '\n');
    } catch (Exception e) {
    }
  }

  public Product(int prodID, int inStock, String prodName, BigDecimal unitCost) {
    this.prodID = prodID;
    this.inStock = inStock;
    this.prodName = prodName;
    this.unitCost = unitCost;
  }
}
