package main;

import javafx.scene.image.Image;

import java.text.DecimalFormat;

/**
 * Simple class to hold order information.
 */
public class Order {

  /**
   * Type of book to be ordered.
   */
  public enum BookType {
    Null(0), //  Special type for default constructor
    Paperback(5),
    Hardcover(15);

    // Oh what I'd give for a C-style enum instead of this "enum with constructor" stuff
    private final int cost;
    BookType(int cost) {
      this.cost = cost;
    }

    /**
     * @return The cost of this type of book
     */
    public int getCost() {
      return this.cost;
    }

    /**
     * @return The image associated with this type of book
     * TODO: Maybe cache the image for better performance? May be outside the scope of an assignment though.
     */
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

  /**
   * The destination of the order
   */
  public enum Shipping {
    Null(0), // Special type for default constructor
    USA(20), Canada(40), Mexico(45),
    /** Outside North-America */
    International(50);

    private int cost;
    private Shipping(int cost) {
      this.cost = cost;
    }

    /** How much does this destination cost to ship to? */
    public int getCost() {
      return this.cost;
    }

    /** What image would most people associate with this place? */
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

  /** Gets a formatted price, based on destination and booktype. */
  public String getPrice() {
    final DecimalFormat fmt = new DecimalFormat("$###,###.00");

    return fmt.format(bookType.getCost() + destination.getCost());
  }

  private String customerName; /** Who bought it? */
  private BookType bookType; /** What did they buy? */
  private Shipping destination; /** Where did they order it to? */

  /** Does this order have all information in it? */
  public boolean isValid() {
    return !this.customerName.isEmpty() && this.hasValidCost();
  }

  /** Does this order have all cost-type information in it? */
  public boolean hasValidCost() {
    return (this.bookType != BookType.Null) && (this.destination != Shipping.Null);
  }

  /** Initializes the order to an invalid default */
  Order() {
    this.customerName = "";
    this.bookType = BookType.Null;
    this.destination = Shipping.Null;
  }

  /**
   * Standard constructor
   * @param custName Who bought it?
   * @param type What did they buy?
   * @param dest Where did they send it?
   */
  Order(String custName, BookType type, Shipping dest) {
    this.customerName = custName;
    this.bookType = type;
    this.destination = dest;
  }
}
