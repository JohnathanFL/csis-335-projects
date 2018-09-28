package main;

import javafx.scene.image.Image;

public class Order {
  public enum BookType {
    Paperback(5),
    Hardcover(15);

    // Oh what I'd give for a C-style enum
    private final int cost;
    BookType(int cost) {
      this.cost = cost;
    }

    public int getCost() {
      return this.cost;
    }

    public Image getImg() {
      switch (this.cost) {
        case 5: // Paperback
          return new Image("/main/paperback.png");

        case 15: // Hardcover
          return new Image("/main/hardcover.png");

          default:
            return null; // Can't give what we don't got
      }
    }
  }
  public enum Shipping {

    USA(20), Canada(40), Mexico(45), International(50);

    private int cost;
    private Shipping(int cost) {
      this.cost = cost;
    }

    public int getCost() {
      return this.cost;
    }

    public Image getImg() {
      switch (this.cost) {
        case 20: // USA
          return new Image("/main/usa.png");

        case 40: // Canada
          return new Image("/main/canada.png");

        case 45: // Mexico
          return new Image("/main/mexico.png");

        case 50: // International
          return new Image("/main/nonna.png");

        default:
          return null;
      }
    }
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public BookType getBookType() {
    return bookType;
  }

  public void setBookType(BookType bookType) {
    this.bookType = bookType;
  }

  public Shipping getDestination() {
    return destination;
  }

  public void setDestination(Shipping destination) {
    this.destination = destination;
  }

  public int getPrice() {
    return bookType.getCost() + destination.getCost();
  }

  private String customerName;
  private BookType bookType;
  private Shipping destination;

  Order(String custName, BookType type, Shipping dest) {
    this.customerName = custName;
    this.bookType = type;
    this.destination = dest;
  }
}
