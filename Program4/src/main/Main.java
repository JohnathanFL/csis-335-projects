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
import javafx.stage.Stage;

public class Main extends Application {
  private Stage stage;
  private Scene inputScene, tableScene;

  private Image blankImage = new Image(getClass().getResource("/main/blank.png").toString());
  // In order: TextField, ShippingType, BookType
  private boolean[] canSubmit = {false, false, false};

  private ObservableList<Order> orders = FXCollections.observableArrayList();

  @Override
  public void start(Stage primStage) {
    this.stage = primStage;
    createInputUI();
    createDisplayUI();
    this.stage.setScene(this.inputScene);
  this.stage.show();
  }

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

  private static void setToggleGroup(ToggleGroup group, RadioButton ... btns) {
    for (RadioButton btn : btns) {
      btn.setToggleGroup(group);
    }
  }

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

    Text topText = new Text("Please enter order information..."), prompt = new Text("Customer Name: ");

    TextField custName = new TextField();
    custName.setPromptText("customer name...");


    Button submit = new Button("Submit"), display = new Button("Display");
    submit.setMaxWidth(Double.MAX_VALUE);
    display.setMaxWidth(Double.MAX_VALUE);
    submit.setDefaultButton(true);
    submit.setDisable(true);





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
    destImg.setPreserveRatio(true);
    destImg.setFitWidth(50.0);
    destImg.setFitHeight(50.0);

    ToggleGroup bookTypeGroup = new ToggleGroup(), destGroup = new ToggleGroup();
    setToggleGroup(bookTypeGroup, paperback, hardback);
    setToggleGroup(destGroup, usa, canada, mexico, nonNa);

    bookTypeGroup.selectedToggleProperty().addListener((v, oldVal, newVal) -> {
      // If toggle got unselected, newVal becomes null.
      if(newVal != null) {
        bookImg.setImage(((Order.BookType) newVal.getUserData()).getImg());
        this.canSubmit[2] = true;
      }
      else {
        bookImg.setImage(blankImage);
        this.canSubmit[1] = false;
      }

      submit.setDisable(!(this.canSubmit[0] && this.canSubmit[1] && this.canSubmit[2]));
    });

    destGroup.selectedToggleProperty().addListener((v, oldVal, newVal) -> {
      if(newVal != null) {
        destImg.setImage(((Order.Shipping) newVal.getUserData()).getImg());
        this.canSubmit[1] = true;
      }
      else {
        destImg.setImage(blankImage);
        this.canSubmit[1] = false;
      }

      submit.setDisable(!(this.canSubmit[0] && this.canSubmit[1] && this.canSubmit[2]));
    });
    custName.textProperty().addListener((e, old, newVal) -> {
      if(newVal != null && !newVal.isEmpty())
        this.canSubmit[0] = true;
      else
        this.canSubmit[0] = true;

      submit.setDisable(!(this.canSubmit[0] && this.canSubmit[1] && this.canSubmit[2]));
    });

    HBox inputBox = new HBox(prompt, custName);


    GridPane radioGrid = new GridPane();


    radioGrid.setHgap(5.0);
    radioGrid.setVgap(5.0);
    radioGrid.add(usa, 0,0);
    radioGrid.add(canada, 0, 1);
    radioGrid.add(mexico, 1,0);
    radioGrid.add(nonNa, 1,1);
    radioGrid.add(destImg, 2, 0, 2, 2);

    radioGrid.add(paperback, 0, 3);
    radioGrid.add(hardback, 0, 4);
    radioGrid.add(bookImg, 2, 3, 2,2);

    HBox btnBox = new HBox(submit, display);
    btnBox.setFillHeight(true);
    HBox.setHgrow(submit, Priority.ALWAYS);
    HBox.setHgrow(display, Priority.ALWAYS);


    ap.getChildren().addAll(topText, inputBox, radioGrid, btnBox);

    setAnchors(topText, 0.0,0.0, null, 0.0);
    setAnchors(inputBox, 20.0, 0.0, null, 0.0);
    setAnchors(radioGrid, 60.0, 0.0, null, 0.0);
    setAnchors(btnBox, null, 0.0, 0.0, 0.0);


    display.setOnAction(e -> {
      this.stage.setScene(this.tableScene);
    });
    submit.setOnAction(e -> {
      Order.BookType type = (Order.BookType)bookTypeGroup.getSelectedToggle().getUserData();
      Order.Shipping dest = (Order.Shipping)destGroup.getSelectedToggle().getUserData();
      this.orders.add(new Order(custName.getText(), type, dest));
      custName.clear();
      bookTypeGroup.getSelectedToggle().setSelected(false);
      destGroup.getSelectedToggle().setSelected(false);
      custName.requestFocus();
    });

    inputScene = new Scene(ap);
  }
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
      cost.setCellValueFactory(new PropertyValueFactory<Order, Integer>("price"));

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
