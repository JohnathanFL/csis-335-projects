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

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Read as OR-d Bool Array
 *
 * Stores a number of bools and tracks the OR of the entire set within this.value
 */
class OredBoolArray {
  OredBoolArray(boolean...bools) {
    ar = bools;
    value = new SimpleBooleanProperty(false);
    update();
  }

  /**
   * Updates this.value according to the OR of the set
   */
  void update() {
    boolean res = ar[0];
    for(boolean b : ar)
      res = res || b;

    value.set(res);

    System.out.println(value);
  }

  /**
   * Sets an internal value to val within the array
   * @param index Index of the value to set (0..size-1)
   * @param val Value to place in said index
   */
  public void set(int index, boolean val) {
    ar[index] = val;
    update();
  }

  /**
   * Use this if you want to query the value.
   */
  public SimpleBooleanProperty value;
  boolean[] ar;

}

public class Controller {


  public TableView tblView;
  public ComboBox selectCombo;
  public TableColumn idCol;
  public TableColumn descCol;
  public TableColumn catCol;
  public TableColumn quantCol;
  public TableColumn costCol;
  public TableColumn priceCol;
  public TextField newProdDesc;
  public TextField newProdCat;
  public TextField newProdQuant;
  public TextField newProdUnitCost;
  public TextField newProdSellingPrice;
  public ComboBox<Product> delProdID;
  public Button refreshFieldBtn;
  public Button delProdBtn;
  public TextField fieldField;
  public VBox fieldVBox;
  public TabPane primeTabPane;
  public Tab adminTab, displayTab;
  public Button addProdBtn;


  ObservableList<Product> products;
  ProductQueries queries;

  /**
   * Creates a login window.
   * Does authentication through ProductQueries.checkLogin
   * @return True if user entered correct credentials, false otherwise.
   */
  public boolean loginBox() {
      Dialog<Pair<String, String>> dia = new Dialog<>();
      dia.setTitle("Enter admin credentials");
      dia.setHeaderText("Enter login information");
      ButtonType login = new ButtonType("Login", ButtonBar.ButtonData.FINISH), cancel = new ButtonType("Cancel", ButtonBar.ButtonData.NO);
      TextField userField = new TextField();
      userField.setPromptText("username...");
      PasswordField passField = new PasswordField();
      passField.setPromptText("password...");

      dia.getDialogPane().getButtonTypes().setAll(login, cancel);
      dia.getDialogPane().setContent(new HBox(userField, passField));

      dia.setResultConverter(btn -> {
        if(btn == login)
          return new Pair<>(userField.getText(), passField.getText());
        else
          return  new Pair<>("", "");
      });

      Optional<Pair<String, String>> res = dia.showAndWait();
      if(!res.isPresent() || res.get().getKey() == null || res.get().getValue() == null)
        return false;

      Pair<String, String> combo = res.get();

      try {
        return (new ProductQueries()).checkLogin(combo.getKey(), combo.getValue());
      } catch (SQLException ex) {
        System.out.println(ex);
      }

      return false;
  }

  /**
   * Refresh the products list
   * @param which Which refresh style to use
   */
  public void refresh(String which) {
    products.clear();

    try {
      if (which == ProductQueries.selectAllName) {
        fieldField.clear();
        fieldVBox.setVisible(false);
        products.setAll(queries.getAllProducts());
      } else if (which == ProductQueries.selectCategoryName) {
        fieldField.clear();
        fieldVBox.setVisible(true);
        refreshFieldBtn.setOnAction(ev -> {
          products.setAll(queries.getProductByCategory(fieldField.getText()));
        });
      } else if (which == ProductQueries.selectWhereQtyLTName) {
        fieldField.clear();
        fieldVBox.setVisible(true);
        refreshFieldBtn.setOnAction(ev -> {
          products.setAll(queries.getProductByQuantity(Integer.parseInt(fieldField.getText())));
        });
      } else if (which == ProductQueries.selectWhereUnitCostLTName) {
        fieldField.clear();
        fieldVBox.setVisible(true);
        refreshFieldBtn.setOnAction(ev -> {
          products.setAll(queries.getProductsByUnitCost(new BigDecimal(fieldField.getText())));
        });
      } else {
        System.out.println("Invalid combo choice");
      }
    } catch (Exception ex) {
      System.out.println(ex);
    }

    delProdID.getItems().clear();
    for(Product prod : products) {
      delProdID.getItems().add(prod);
    }

  }


  public void initialize() {
    try {
      queries = new ProductQueries();

      queries.updateComboBox(selectCombo);
      products = FXCollections.observableArrayList();
      selectCombo.setValue(ProductQueries.selectAllName);
      refresh(ProductQueries.selectAllName);
    } catch (SQLException e) {
      System.out.println(e);
    }

    ///=================================================================================================================
    /// Table stuff
    ///=================================================================================================================
    idCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("prodID"));
    descCol.setCellValueFactory(new PropertyValueFactory<Product, String>("desc"));
    catCol.setCellValueFactory(new PropertyValueFactory<Product, String>("category"));
    quantCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("quantOnHand"));
    costCol.setCellValueFactory(new PropertyValueFactory<Product, String>("unitCostFmt"));
    priceCol.setCellValueFactory(new PropertyValueFactory<Product, String>("sellingPriceFmt"));

    tblView.setItems(products);
    refreshFieldBtn.setDisable(true);


    ///=================================================================================================================
    /// Query stuff
    ///=================================================================================================================
    selectCombo.valueProperty().addListener((e, oldVal, newVal) -> {
      refresh((String)newVal);
    });

    fieldField.textProperty().addListener((e, oldVal, newVal) -> {
      refreshFieldBtn.setDisable(newVal.isEmpty());

      Object cur = selectCombo.getValue();
      if(cur == ProductQueries.selectAllName || cur == ProductQueries.selectCategoryName)
        return;
      else if(cur == ProductQueries.selectWhereQtyLTName) {
        if(!newVal.matches("\\d*"))
          fieldField.setText(oldVal);
      } else if(cur == ProductQueries.selectWhereUnitCostLTName) {
        if(!newVal.matches("\\d*\\.?\\d{0,2}"))
          fieldField.setText(oldVal);
      }
    });

    ///=================================================================================================================
    /// Login manager
    ///=================================================================================================================
    primeTabPane.getSelectionModel().selectedItemProperty().addListener((val, oldTab, newTab) -> {
      if(newTab == adminTab)
        if(!loginBox())
          primeTabPane.getSelectionModel().select(displayTab);
    });


    ///=================================================================================================================
    /// Admin Tab stuff
    ///=================================================================================================================


    ///=================================================================================================================
    /// Add stuff
    ///=================================================================================================================

    // In order: newProdDesc, newProdCat, newProdQuant, newProdUnitCost, newProdSellingPrice
    OredBoolArray ar = new OredBoolArray(false, false, false, false, false);

    addProdBtn.disableProperty().bind(ar.value);

    newProdDesc.textProperty().addListener((e, oldVal, newVal) -> {
      ar.set(0, newVal.isEmpty());
    });

    newProdCat.textProperty().addListener((e, oldVal, newVal) -> {
      ar.set(1, newVal.isEmpty());
    });

    newProdQuant.textProperty().addListener((e, oldVal, newVal) -> {
      if(!newVal.matches("\\d*"))
        newProdQuant.setText(oldVal);

      ar.set(2, newVal.isEmpty());
    });

    newProdSellingPrice.textProperty().addListener((e, oldVal, newVal) -> {
      if(!newVal.matches("\\d*\\.?\\d{0,2}"))
        newProdSellingPrice.setText(oldVal);

      ar.set(3, newVal.isEmpty());
    });

    newProdUnitCost.textProperty().addListener((e, oldVal, newVal) -> {
      if(!newVal.matches("\\d*\\.?\\d{0,2}"))
        newProdUnitCost.setText(oldVal);

      ar.set(4, newVal.isEmpty());
    });

    addProdBtn.setOnAction(e -> {
      queries.addProduct(newProdDesc.getText(), newProdCat.getText(), Integer.parseInt(newProdQuant.getText()), new BigDecimal(newProdUnitCost.getText()), new BigDecimal(newProdSellingPrice.getText()));
      refresh((String)selectCombo.getValue());

      newProdQuant.clear();
      newProdUnitCost.clear();
      newProdSellingPrice.clear();
      newProdDesc.clear();
      newProdCat.clear();
      newProdDesc.requestFocus();
    });

    ///=================================================================================================================
    /// Delete Stuff
    ///=================================================================================================================

    delProdBtn.setOnAction(e -> {
      if(delProdID.getValue() != null) {
        queries.deleteProduct(delProdID.getValue().getProdID());
        selectCombo.setValue(ProductQueries.selectAllName);
        delProdID.setValue(null);
        delProdID.requestFocus();
        refresh((String) selectCombo.getValue());
      }
    });

  }
}
