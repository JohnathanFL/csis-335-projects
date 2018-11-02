package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.crypto.spec.PBEKeySpec;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;


public class ProductQueries {
  static Connection conn;
  static ResultSet curResSet;

  ProductQueries() throws SQLException {
    final String url = "jdbc:mysql://localhost:3306/leejo_prog7?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false", username = "leejo", password = "OverlyUnderlyPoweredMS";
    if(conn == null) {
      conn = DriverManager.getConnection(url, username, password);

      // If we didn't have a connection, we need to recreate the statements no matter what.
      selectAllProducts = conn.prepareStatement("select * from Product;");
      selectProductByCategory = conn.prepareStatement("select * from Product where lcase(category) like lcase(?);");
      selectProductWhereQtyOnHand = conn.prepareStatement("select * from Product where quantOnHand <= ?;");
      selectProductWhereUnitcost = conn.prepareStatement("select * from Product where unitCost <= ?;");
      insertNewProduct = conn.prepareStatement("insert into Product (description, category, quantOnHand, unitCost, sellingPrice) values (?. ?, ?, ?, ?);");
      deleteProduct = conn.prepareStatement("delete from Product where prodID = ?;");
    }
  }

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

  public ArrayList<Product> getAllProducts()  {
    try {
      curResSet = selectAllProducts.executeQuery();
    } catch (SQLException e) {
      System.out.println(e);
    }
    return makeArrayList();
  }
  public ArrayList<Product> getProductByCategory(String category) {
   try {
    selectProductByCategory.setString(1, category);
    curResSet = selectProductByCategory.executeQuery();

  } catch (SQLException e) {
    System.out.println(e);
  }
    return makeArrayList();
  }
  public ArrayList<Product> getProductByQuantity(int qty)  {
    try {
    selectProductWhereQtyOnHand.setInt(1, qty);
    curResSet = selectProductWhereQtyOnHand.executeQuery();
    } catch (SQLException e) {
      System.out.println(e);
    }

    return makeArrayList();
  }
  public ArrayList<Product> getProductsByUnitCost(BigDecimal cost) {
    try {
    selectProductWhereUnitcost.setBigDecimal(1, cost);
    curResSet = selectProductWhereUnitcost.executeQuery();

    } catch (SQLException e) {
      System.out.println(e);
    }

    return makeArrayList();
  }
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
