/**
 * Major Project 1
 * CSIS 335 - GUIs]
 * <p>
 * Author: Johnathan Lee
 * Due: 10/15/18
 * <p>
 * A simple cash register interface. Allows adding customer details and ordering products, while also tracking how
 * many of each product is on hand.
 */

package main;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.io.*;
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
  private ObservableList<Product> prodTable;

  /**
   * Main constructor for Order. Creates a new order with all of its attributes all at once.
   * @param custTable
   * @param prodTable
   * @param custID
   * @param prodID
   * @param quantity
   * @param shipping
   * @param payment
   * @param hasWarranty
   */
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

    this.updateSubtotal();

  }

  /**
   * Recalculates this.subtotal.
   */
  public void updateSubtotal() {
    FilteredList<Product> list = prodTable.filtered(p -> p.getProdID() == this.prodID);
    System.out.println(this.serialize());

    Product thisProduct = list.get(0);

    this.subtotal =
            thisProduct.getUnitCost().multiply(new BigDecimal(this.quantity)) // Units * cost
                    .add(this.shipping.getPrice()) // + shipping costs
                    // + total price *payment %surcharge
                    .multiply(BigDecimal.valueOf(1).add(BigDecimal.valueOf(this.payment.getAdded())))
                    // + a 5.99 warranty for each product (logically not included in shipping calcs)
                    .add(BigDecimal.valueOf(hasWarranty ? 5.99 : 0.00).multiply(new BigDecimal(this.quantity)));
  }

  /**
   * Creates a string representation of this that can be serialized/deserialized.
   * @return
   */
  public String serialize() {
    return String.format("%s|%s|%s|%s|%s|%s", this.custID, this.prodID, this.quantity, this.shipping, this.payment, this.hasWarranty);
  }

  /**
   * Inits an order with decent default values and the databases
   * @param custTable
   * @param products
   */
  public Order(ObservableList<Customer> custTable, ObservableList<Product> products) {
    this.quantity = 1;
    this.custTable = custTable;
    this.prodTable = products;
  }

  /**
   * Gets all Orders in a file.
   * @param inFile The file to read from
   * @param custTable The table containing the customers referenced in inFile.
   * @param prodTable The table containing the products referenced in inFile.
   * @return An arraylist containing all orders from inFile
   */
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

  /**
   * Serializes an entire list of Orders, using the internal serialize function
   * @param outFile File to write to
   * @param list Orders to write
   */
  public static void serialize(File outFile, ObservableList<Order> list) {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(outFile, false));
      for (Order order : list)
        writer.write(order.serialize() + '\n');

      writer.close();
    } catch (Exception e) {
    }
  }

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

  /**
   * Sugar for getting the full name of a customer
   */
  public String getFullName() {
    // Should only ever be one in the returned list, so we can just take the first anyway.
    Customer thisCust = this.custTable.filtered(cust -> cust.getCustID() == this.custID).get(0);
    return thisCust.getFirstName() + " " + thisCust.getLastName();
  }

  /**
   * Gets the name of a product based on the prodID and associated product table.
   * @return
   */
  public String getProdName() {
    // See comment in getFullName
    return this.prodTable.filtered(prod -> prod.getProdID() == this.prodID).get(0).getProdName();
  }

  /**
   * Gets a nicely formatted version of the subtotal.
   * @return
   */
  public String getSubtotalFmt() {
    this.updateSubtotal();
    return fmt.format(this.subtotal.floatValue());
  }

  /**
   * How the order is being shipped.
   * Based on standard shipping speeds.
   */
  public enum Shipping {
    NextDay(BigDecimal.valueOf(10.0)), TwoDay(BigDecimal.valueOf(5.99)), Standard(BigDecimal.valueOf(2.99));

    // Would be far easier in C++ with sane initializers.
    private static final Shipping[] shipMap = {Shipping.NextDay, Shipping.TwoDay, Shipping.Standard};
    private static final String[] strMap = {"Next Day", "Two Day", "Standard"};

    private final BigDecimal price;

    Shipping(BigDecimal price) {
      this.price = price;
    }

    /**
     * Deserializes a string into a Shipping
     * @param from String to read from
     * @return What the string represents, or Standard if it was invalid
     */
    public static Shipping fromString(String from) {
      for (int i = 0; i < strMap.length; i++)
        if (strMap[i].equals(from))
          return shipMap[i];

      return Standard;
    }

    /**
     * Serializes a shipping into a string
     * @return The human readable name of this, or "Invalid Enum" if it's... well, invalid.
     */
    @Override
    public String toString() {
      for (int i = 0; i < shipMap.length; i++)
        if (shipMap[i].equals(this))
          return strMap[i];

      return "Invalid Enum";
    }

    public BigDecimal getPrice() {
      return price;
    }


  }

  /**
   * How the customer is paying for this order.
   * Paying by credit card adds a 5% processing fee.
   */
  public enum Payment {
    Cash(0), Credit(0.05);

    private static final Payment[] payMap = {Payment.Cash, Payment.Credit};
    private static final String[] strMap = {"Cash", "Credit"};

    // How much this payment type adds or subtracts from the purchase
    private final double added;

    Payment(double added) {
      this.added = added;
    }

    public static Payment fromString(String from) {
      for (int i = 0; i < strMap.length; i++)
        if (strMap[i].equals(from))
          return payMap[i];

      return Cash;
    }

    public double getAdded() {
      return added;
    }

    @Override
    public String toString() {
      for (int i = 0; i < payMap.length; i++)
        if (payMap[i].equals(this))
          return strMap[i];

      return "Invalid Enum";
    }
  }


}
