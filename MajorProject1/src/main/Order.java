package main;

import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Order {
  public enum Shipping {
    StorkExpress("Stork Express"), GremlinTrain("Gremlin Train"), SnailBackMail("Snailback Mail");

    private final String name;
    Shipping(String newName) {
      this.name = newName;
    }
  }
  public enum Payment {
    Gold("Gold Coins"), SoulShards("Soul Shards");

    private final String type;
    Payment(String newType) {
      this.type = newType;
    }
  }

  private int custID, prodID, quantity;
  private Shipping shipping;
  private Payment payment;
  private boolean hasWarranty;


  private ObservableList<Customer> custTable;
  private ObservableList<Product> prodTable;

  public static ArrayList<Order> parseFile(String fileName) {
    return new ArrayList<>();
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
}
