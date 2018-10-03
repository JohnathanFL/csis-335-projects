/*
Author: Johnathan Lee
Due: 09/28/18

Program 4:

Simple order/order list interface.
 */

package main;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * See docs at the top of the file.
 */
public class Main extends Application {
  /** The stage. This hero hardly needs introduction */
  private Stage stage;
  /** Contains a namefield, radio buttons for shipping destination/book type, a total cost field, and submit/display buttons */
  private Scene inputScene,
  /** Contains a table with columns for Name/Type/Dest/Cost, as well as a back button */
  tableScene;
  /** A simple transparent image with a black border. Used as a placeholder. All other images are base image composed with this. */
  private Image blankImage = new Image(getClass().getResource("/main/blank.png").toString());
  /** All orders */
  private ObservableList<Order> orders = FXCollections.observableArrayList();
  /** Constantly updated as the user changes things. Gets added and re-instantiated on submit */
  private Order wipOrder = new Order();

  /** AND IN THIS CORNER: TYPICALLY HEARD AFTER A GUNSHOT AND TO THE CHEERS OF THE ADORING CROWD: START
   * It starts the program, what else is there to say? */
  @Override
  public void start(Stage primStage) {
    this.stage = primStage;
    createInputUI();
    createDisplayUI();
    this.stage.setScene(this.inputScene);
  this.stage.show();
  }

  /**
   * Sugar for setting anchors for anchor pane all at once
   * @param ap The node to set anchors for
   * @param top Top anchor
   * @param right Right anchor
   * @param bot Bottom anchor
   * @param left Left anchor
   */
  private static void setAnchors(Node ap, Double top, Double right, Double bot, Double left) {
    if(top != null)
      AnchorPane.setTopAnchor(ap, top);
    if(right != null)
      AnchorPane.setRightAnchor(ap, right);
    if(bot != null)
      AnchorPane.setBottomAnchor(ap, bot);
    if(left != null)
      AnchorPane.setLeftAnchor(ap, left);
  }

  /**
   * Sugar for setting toggle group for multiple toggles
   * @param group
   * @param btns
   */
  private static void setToggleGroup(ToggleGroup group, RadioButton ... btns) {
    for (RadioButton btn : btns) {
      btn.setToggleGroup(group);
    }
  }

  /**
   * Creates the first scene for making orders
   */
  private void createInputUI() {
    /* Simple, vericle style:
    Please enter order information...
                    -------------------------
    Customer name: |                         |
                    -------------------------
    * USA    * Mexico   -----
                       |     |
    * Canada * International    -----

    * Paperback         -----
                       |     |
    * Hardcover         -----


    SUBMIT      DISPLAY
     */


    AnchorPane ap = new AnchorPane();
    ap.setPadding(new Insets(5,10,5,10));
    ap.setPrefSize(340, 225);

    Text topText = new Text("Please enter order information..."), prompt = new Text("Customer Name: "), totalCost = new Text("Total Cost: $...");
    totalCost.setTextAlignment(TextAlignment.CENTER);

    TextField custName = new TextField();
    custName.setPromptText("customer name...");


    Button submit = new Button("Submit"), display = new Button("Display");
    {
      submit.setMaxWidth(Double.MAX_VALUE);
      display.setMaxWidth(Double.MAX_VALUE);
      submit.setDefaultButton(true);
      submit.setDisable(true);
    }





    RadioButton usa = new RadioButton("USA"), canada = new RadioButton("Canada"),
        mexico = new RadioButton("Mexico"), nonNa = new RadioButton("International"),
        paperback = new RadioButton("Paperback"), hardback = new RadioButton("Hardback");
    {
      usa.setUserData(Order.Shipping.USA);
      canada.setUserData(Order.Shipping.Canada);
      mexico.setUserData(Order.Shipping.Mexico);
      nonNa.setUserData(Order.Shipping.International);
      paperback.setUserData(Order.BookType.Paperback);
      hardback.setUserData(Order.BookType.Hardcover);
    }



    ImageView destImg = new ImageView(blankImage), bookImg = new ImageView(blankImage);
    {
      destImg.setPreserveRatio(true);
      destImg.setFitWidth(50.0);
      destImg.setFitHeight(50.0);
    }

    ToggleGroup bookTypeGroup = new ToggleGroup(), destGroup = new ToggleGroup();
    {
      setToggleGroup(bookTypeGroup, paperback, hardback);
      setToggleGroup(destGroup, usa, canada, mexico, nonNa);

      bookTypeGroup.selectedToggleProperty().addListener((v, oldVal, newVal) -> {
        // If toggle got unselected, newVal becomes null.
        if (newVal != null) {
          Order.BookType type = (Order.BookType) newVal.getUserData();
          bookImg.setImage(type.getImg());
          wipOrder.setBookType(type);
        } else {
          bookImg.setImage(blankImage);
          wipOrder.setBookType(Order.BookType.Null);
        }

        submit.setDisable(!this.wipOrder.isValid());
        if (this.wipOrder.hasValidCost())
          totalCost.setText("Total Cost: " + this.wipOrder.getPrice());
        else
          totalCost.setText("Total Cost: $...");
      });

      destGroup.selectedToggleProperty().addListener((v, oldVal, newVal) -> {
        if (newVal != null) {
          Order.Shipping dest = (Order.Shipping) newVal.getUserData();
          destImg.setImage(dest.getImg());
          this.wipOrder.setDestination(dest);
        } else {
          destImg.setImage(blankImage);
          this.wipOrder.setDestination(Order.Shipping.Null);
        }

        submit.setDisable(!this.wipOrder.isValid());
        if (this.wipOrder.hasValidCost())
          totalCost.setText("Total Cost: " + this.wipOrder.getPrice());
        else
          totalCost.setText("Total Cost: $...");
      });
      custName.textProperty().addListener((e, old, newVal) -> {
        if (newVal != null)
          this.wipOrder.setCustomerName(newVal);
        else
          this.wipOrder.setCustomerName("");

        submit.setDisable(!this.wipOrder.isValid());
      });
    }
    HBox inputBox = new HBox(prompt, custName);


    GridPane radioGrid = new GridPane();
    {
      radioGrid.setHgap(5.0);
      radioGrid.setVgap(5.0);
      radioGrid.add(usa, 0, 0);
      radioGrid.add(canada, 0, 1);
      radioGrid.add(mexico, 1, 0);
      radioGrid.add(nonNa, 1, 1);
      radioGrid.add(destImg, 2, 0, 2, 2);

      radioGrid.add(paperback, 0, 3);
      radioGrid.add(hardback, 0, 4);
      radioGrid.add(bookImg, 2, 3, 2, 2);
    }


    HBox btnBox = new HBox(submit, display);
    {
      btnBox.setFillHeight(true);
      HBox.setHgrow(submit, Priority.ALWAYS);
      HBox.setHgrow(display, Priority.ALWAYS);
    }

    ap.getChildren().addAll(topText, inputBox, radioGrid, totalCost, btnBox);

    {
      setAnchors(topText, 0.0, 0.0, null, 0.0);
      setAnchors(inputBox, 20.0, 0.0, null, 0.0);
      setAnchors(radioGrid, 60.0, 0.0, null, 0.0);
      setAnchors(totalCost, null, 0.0, 30.0, 0.0);
      setAnchors(btnBox, null, 0.0, 0.0, 0.0);
    }


    display.setOnAction(e -> {
      this.stage.setScene(this.tableScene);
    });
    submit.setOnAction(e -> {
      this.orders.add(this.wipOrder);
      this.wipOrder = new Order();
      custName.clear();
      bookTypeGroup.getSelectedToggle().setSelected(false);
      destGroup.getSelectedToggle().setSelected(false);
      custName.requestFocus();
    });

    inputScene = new Scene(ap);
  }

  /**
   * Creates the UI for displaying all orders in a table
   */
  private void createDisplayUI() {
    TableView<Order> table = new TableView<>(this.orders);
    {
      TableColumn name, type, dest, cost;
      name = new TableColumn("Customer Name");
      type = new TableColumn("Book Type");
      dest = new TableColumn("Destination");
      cost = new TableColumn("Cost");

      name.setCellValueFactory(new PropertyValueFactory<Order, String>("customerName"));
      type.setCellValueFactory(new PropertyValueFactory<Order, Order.BookType>("bookType"));
      dest.setCellValueFactory(new PropertyValueFactory<Order, Order.Shipping>("destination"));
      cost.setCellValueFactory(new PropertyValueFactory<Order, String>("price"));

      table.getColumns().addAll(name, type, dest, cost);
    }

    Button back = new Button("Back");
    back.setDefaultButton(true);
    back.requestFocus();
    back.setOnAction(e -> this.stage.setScene(this.inputScene));
    back.setMaxWidth(Double.MAX_VALUE);
    back.setMaxHeight(Double.MAX_VALUE);

    VBox vb = new VBox(table, back);
    VBox.setVgrow(back, Priority.ALWAYS);
    this.tableScene = new Scene(vb, 500, 350);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
