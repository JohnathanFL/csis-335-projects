/**
 * Author: Johnathan Lee
 * Class: CSIS 335
 *
 * Due 11/05/18
 *
 * Program 7:
 * Provides a simple display/query interface for a product database, along with an add/delete admin interface.
 */

package main;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

/**
 * Holds information from one row of the database.
 */
public class Product {
  private final DecimalFormat fmt = new DecimalFormat("$###,###.00");

  private int prodID;
  private String desc, category;
  private  int quantOnHand;
  private BigDecimal unitCost, sellingPrice;

  /**
   * Primary constructor.
   * Takes values from the current row of a ResultSet, throws if the ResultSet throws
   * @param res Set to pull values from. Expects the following column types (in order): (prodID: int, desc: String, category: String, quantOnHand: int, unitCost: BigDecimal, sellingPrice: BigDecimal)
   * @throws SQLException If any resultset.getxxx method fails
   */
  public Product(ResultSet res) throws SQLException {
    this.prodID = res.getInt(1);
    this.desc = res.getString(2);
    this.category = res.getString(3);
    this.quantOnHand = res.getInt(4);
    this.unitCost = res.getBigDecimal(5);
    this.sellingPrice = res.getBigDecimal(6);
  }


  @Override
  public String toString() {
    return "Product{" +
        "prodID=" + prodID +
        ", desc='" + desc + '\'' +
        ", category='" + category + '\'' +
        ", quantOnHand='" + quantOnHand + '\'' +
        ", unitCost='" + unitCost + '\'' +
        ", sellingPrice='" + sellingPrice + '\'' +
        '}';
  }

  public int getProdID() {
    return prodID;
  }

  public void setProdID(int prodID) {
    this.prodID = prodID;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public int getQuantOnHand() {
    return quantOnHand;
  }

  public void setQuantOnHand(int quantOnHand) {
    this.quantOnHand = quantOnHand;
  }

  public BigDecimal getUnitCost() {
    return unitCost;
  }

  public String getUnitCostFmt() {

    return fmt.format(this.unitCost);
  }

  public void setUnitCost(BigDecimal unitCost) {
    this.unitCost = unitCost;
  }

  public BigDecimal getSellingPrice() {
    return sellingPrice;
  }

  public String getSellingPriceFmt() {
    return fmt.format(this.sellingPrice);
  }

  public void setSellingPrice(BigDecimal sellingPrice) {
    this.sellingPrice = sellingPrice;
  }
}
