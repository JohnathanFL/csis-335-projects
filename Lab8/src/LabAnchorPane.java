import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.util.Optional;

public class LabAnchorPane  extends Application {

  public static void setAnchors(Node n, Double top, Double right, Double bot, Double left) {
    if(top !=null)
      AnchorPane.setTopAnchor(n, top);
    if(right != null)
      AnchorPane.setRightAnchor(n, right);
    if(bot != null)
      AnchorPane.setBottomAnchor(n, bot);
    if(left != null)
      AnchorPane.setLeftAnchor(n, left);
  }

  @Override
  public void start(Stage prim) {
    AnchorPane aPane = new AnchorPane();
    Button btnMsg = new Button("Print Message"), btnClear = new Button("Clear");
    Label lblMsg = new Label();
    BackgroundFill backFill = new BackgroundFill(Color.CADETBLUE, CornerRadii.EMPTY, Insets.EMPTY);
    Background b = new Background(backFill);
    lblMsg.setBackground(b);
    HBox hb = new HBox(btnMsg, btnClear);
    aPane.getChildren().addAll(lblMsg, hb);
    aPane.setMinSize(400, 100);
    setAnchors(hb, null, 10.0, null, null);
    setAnchors(lblMsg, null, null, null, 10.0);

    btnClear.setOnAction(e ->  lblMsg.setText(""));
    btnMsg.setOnAction(e -> lblMsg.setText("Good Morning!"));


    Scene scene = new Scene(aPane);
    prim.setTitle("Example: Anchorpane");
    prim.setScene(scene);
    prim.show();

  }

  public static void main(String[] args) { launch(args); }
}
