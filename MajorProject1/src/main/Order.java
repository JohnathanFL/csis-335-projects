package main;

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

  public static ArrayList<Customer> parseFile(String fileName) {

  }
}
