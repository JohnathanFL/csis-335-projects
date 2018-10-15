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

  int prodID, inStock;
  String prodName;
  BigDecimal unitCost;

  /**
   * A nicer looking getUnitCost
   * @return unitCost formatted into $500,000.00 style strings.
   */
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

  /**
   * @return A string containing the Product ID and name
   */
  @Override
  public String toString() {
    return "[ID: " + this.getProdID() + "] <" + this.getInStock() + "> " + this.prodName +
            (this.getInStock() > 1 ? "(s), " : ", ") + this.getUnitCostFmt() + "/piece";
  }


  /**
   * Loads all products from a file
   * @param inFile The file to read from
   * @return An arraylist containing all products in inFile
   */
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

  /**
   * @return A deserializable string representation of this
   */
  public String serialize() {
    return String.format("%s|%s|%s|%s", this.prodID, this.inStock, this.prodName, this.unitCost);
  }

  /**
   * Writes an array of Products to a file
   * @param outFile File to write to
   * @param list List to write
   */
  public static void serialize(File outFile, ObservableList<Product> list) {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(outFile, false));
      for (Product prod : list)
        writer.write(prod.serialize() + '\n');

      writer.close();
    } catch (Exception e) {
    }
  }

  /**
   * Main constructor for product
   * @param prodID Product ID. Must be unique
   * @param inStock How many of the product are currently in stock
   * @param prodName Name of the product
   * @param unitCost How much EACH of the product costs
   */
  public Product(int prodID, int inStock, String prodName, BigDecimal unitCost) {
    this.prodID = prodID;
    this.inStock = inStock;
    this.prodName = prodName;
    this.unitCost = unitCost;
  }
}
