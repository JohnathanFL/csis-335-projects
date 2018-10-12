package main;

import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Order {
  private static final Pattern parsePattern = Pattern.compile("([^\\|]+)\\|([^\\|]+)\\|([^\\|]+)\\|([^\\|]+)\\|([^\\|]+)\\|([^\\|]+)");
  DecimalFormat fmt = new DecimalFormat("$###,###.00");
  private int custID, prodID, quantity;
  private Shipping shipping;
  private Payment payment;
  private boolean hasWarranty;
  private BigDecimal subtotal;
  private ObservableList<Customer> custTable;

  public int getCustID() {
    return custID;
  }

  public void setCustID(int custID) {
    this.custID = custID;
  }

  public int getProdID() {
    return prodID;
  }

  public void setProdID(int prodID) {
    this.prodID = prodID;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public Shipping getShipping() {
    return shipping;
  }

  public void setShipping(Shipping shipping) {
    this.shipping = shipping;
  }

  public Payment getPayment() {
    return payment;
  }

  public void setPayment(Payment payment) {
    this.payment = payment;
  }

  public boolean isHasWarranty() {
    return hasWarranty;
  }

  public void setHasWarranty(boolean hasWarranty) {
    this.hasWarranty = hasWarranty;
  }

  private ObservableList<Product> prodTable;
  public Order(ObservableList<Customer> custTable, ObservableList<Product> prodTable, int custID, int prodID,
               int quantity, Shipping shipping, Payment payment, boolean hasWarranty) {
    this.custID = custID;
    this.prodID = prodID;
    this.quantity = quantity;
    this.shipping = shipping;
    this.payment = payment;
    this.hasWarranty = hasWarranty;
    this.custTable = custTable;
    this.prodTable = prodTable;

    Product thisProduct = prodTable.filtered(p -> p.getProdID() == this.prodID).get(0);
    this.subtotal = thisProduct.getUnitCost().multiply(new BigDecimal(this.quantity));

  }

  public Order(ObservableList<Customer> custTable, ObservableList<Product> products) {
    this.custTable = custTable;
    this.prodTable = products;
  }

  public static ArrayList<Order> parseFile(File inFile, ObservableList<Customer> custTable,
                                           ObservableList<Product> prodTable) {
    ArrayList<Order> res = new ArrayList<>();
    try {

      BufferedReader reader = new BufferedReader(new FileReader(inFile));

      System.out.println(reader);

      String line;
      while (((line = reader.readLine()) != null) && !line.trim().isEmpty()) {
        line = line.trim();
        Matcher matcher = parsePattern.matcher(line);

        matcher.matches(); // Won't run unless I check if it matches... Go figure.

        // Once again, may the mighty Stroustrup forgive us our ugly code
        res.add(new Order(custTable, prodTable, Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)),
                Shipping.fromString(matcher.group(4)), Payment.fromString(matcher.group(5)),
                Boolean.parseBoolean(matcher.group(6))));
      }

    } catch (IOException e) {
      System.out.println("Failed to load " + inFile + " after exception " + e.toString());
    }

    return res;
  }

  public String getFullName() {
    // Should only ever be one in the returned list, so we can just take the first anyway.
    Customer thisCust = this.custTable.filtered(cust -> cust.getCustID() == this.custID).get(0);
    return thisCust.getFirstName() + " " + thisCust.getLastName();
  }

  public String getProdName() {
    // See comment in getFullName
    return this.prodTable.filtered(prod -> prod.getProdID() == this.prodID).get(0).getProdName();
  }

  public String getSubtotalFmt() {
    return fmt.format(this.subtotal.floatValue());
  }

  public enum Shipping {
    StorkExpress("Stork Express"), GremlinTrain("Gremlin Train"), SnailBackMail("Snailback Mail");

    private final String name;

    Shipping(String newName) {
      this.name = newName;
    }

    public static Shipping fromString(String from) {
      if (from.equals("Stork Express"))
        return StorkExpress;
      else if (from.equals("Gremlin Train"))
        return GremlinTrain;
      else
        return SnailBackMail;
    }
  }

  public enum Payment {
    Gold("Gold Coins"), SoulShards("Soul Shards");

    private final String type;

    Payment(String newType) {
      this.type = newType;
    }

    public static Payment fromString(String from) {
      if (from.equals("Gold Coins"))
        return Gold;
      else
        return SoulShards;
    }
  }


}
