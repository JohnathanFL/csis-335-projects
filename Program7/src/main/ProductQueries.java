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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.crypto.spec.PBEKeySpec;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;


/**
 * Connection manager class.
 * Also provides simple methods for calling prepared queries/inserting/deleting
 */
public class ProductQueries {
  static Connection conn;
  static ResultSet curResSet;

  /**
   * Primary constructor
   * @throws SQLException If unable to connect to database or prepare any statements.
   */
  ProductQueries() throws SQLException {
    final String url = "jdbc:mysql://localhost:3306/leejo_prog7?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false", username = "leejo", password = "OverlyUnderlyPoweredMS";
    if(conn == null) {
      conn = DriverManager.getConnection(url, username, password);

      // If we didn't have a connection, we need to recreate the statements no matter what.
      selectAllProducts = conn.prepareStatement("select * from Product;");
      selectProductByCategory = conn.prepareStatement("select * from Product where lcase(category) like lcase(?);");
      selectProductWhereQtyOnHand = conn.prepareStatement("select * from Product where quantOnHand <= ?;");
      selectProductWhereUnitcost = conn.prepareStatement("select * from Product where unitCost <= ?;");
      insertNewProduct = conn.prepareStatement("insert into Product (description, category, quantOnHand, unitCost, sellingPrice) values (?, ?, ?, ?, ?);");
      deleteProduct = conn.prepareStatement("delete from Product where prodID = ?;");
    }
  }

  /**
   * Authenticates the login credentials
   * @param username Username. Maps to database->Admin.username
   * @param password Password. Will be hashed before comparison via String.hashCode. Maps to database->Admin.passwd
   * @return
   */
  public boolean checkLogin(String username, String password) {
    System.out.println(password.hashCode());

    try {
      ResultSet passwdRes = conn.createStatement().executeQuery("select * from Admin;");

      while (passwdRes.next())
        if (username.equals(passwdRes.getString(1)) && password.hashCode() == passwdRes.getInt(2))
          return true;

        System.out.println("Incorrect login");
      return false;
    } catch (SQLException e) {
      System.out.println(e);
      return false;
    }
  }

  static final public String selectAllName = "All Products", selectCategoryName = "All Products Where Category is...",
  selectWhereQtyLTName = "All Products Where Quantity on Hand is <=...", selectWhereUnitCostLTName = "All Products where Unit Cost is <=...";

  static private PreparedStatement selectAllProducts, selectProductByCategory,
      selectProductWhereQtyOnHand, selectProductWhereUnitcost,
  // Only for admins:
      insertNewProduct, deleteProduct;

  /**
   * Turns the current resultset into an ArrayList
   * @return
   */
  public ArrayList<Product> makeArrayList() {
    ArrayList<Product> list = new ArrayList<>();
    try {
      while (curResSet.next())
        list.add(new Product(curResSet));
    } catch (SQLException e) {
      System.out.println(e);
    }
    return list;
  }

  /**
   * Gets a listing of all products. No exceptions. (literally)
   * @return
   */
  public ArrayList<Product> getAllProducts()  {
    try {
      curResSet = selectAllProducts.executeQuery();
    } catch (SQLException e) {
      System.out.println(e);
    }
    return makeArrayList();
  }

  /**
   * @param category The product.category. (I.E offense, support, etc). Case insensitive.
   * @return All products with the same category
   */
  public ArrayList<Product> getProductByCategory(String category) {
   try {
    selectProductByCategory.setString(1, category);
    curResSet = selectProductByCategory.executeQuery();

  } catch (SQLException e) {
    System.out.println(e);
  }
    return makeArrayList();
  }

  /**
   * @param qty The quantity to search for.
   * @return All products with a quantOnHand of <= qty
   */
  public ArrayList<Product> getProductByQuantity(int qty)  {
    try {
    selectProductWhereQtyOnHand.setInt(1, qty);
    curResSet = selectProductWhereQtyOnHand.executeQuery();
    } catch (SQLException e) {
      System.out.println(e);
    }

    return makeArrayList();
  }

  /**
   * @param cost The cost to search for
   * @return All products with a unitCost of <= cost
   */
  public ArrayList<Product> getProductsByUnitCost(BigDecimal cost) {
    try {
    selectProductWhereUnitcost.setBigDecimal(1, cost);
    curResSet = selectProductWhereUnitcost.executeQuery();

    } catch (SQLException e) {
      System.out.println(e);
    }

    return makeArrayList();
  }

  /**
   * Adds a new product to the database.
   * ADMIN RESTRICTED
   * @param desc The product's description
   * @param cat The product's category
   * @param quant The product's quantityOnHand
   * @param unitCost The product's unitCost
   * @param sellingPrice The product's sellingPrice (i.e including discounts)
   * @return Whether adding the product executed successfully.
   */
  public boolean addProduct(String desc, String cat, int quant, BigDecimal unitCost, BigDecimal sellingPrice) {
    try {
      insertNewProduct.setString(1, desc);
      insertNewProduct.setString(2, cat);
      insertNewProduct.setInt(3, quant);
      insertNewProduct.setBigDecimal(4, unitCost);
      insertNewProduct.setBigDecimal(5, sellingPrice);


      return insertNewProduct.execute();
    } catch (SQLException e) {
      System.out.println(e);
    }
    return false;
  }

  /**
   *
   * @param id
   * @return
   */
  public boolean deleteProduct(int id) {
    try {
    deleteProduct.setInt(1, id);
    return deleteProduct.execute();

    } catch (SQLException e) {
      System.out.println(e);
    }

    return false;
  }

  public void updateComboBox(ComboBox box) {
    box.getItems().clear();
    box.getItems().add(selectAllName);
    box.getItems().add(selectCategoryName);
    box.getItems().add(selectWhereQtyLTName);
    box.getItems().add(selectWhereUnitCostLTName);
  }

  public void updateList(ObservableList<Product> list) {
    list = FXCollections.observableArrayList();

    try {
      curResSet.beforeFirst();
      while (curResSet.next())
        list.add(new Product(curResSet));
    } catch (SQLException ex) {
      System.out.println(ex);
    }
  }
}
