import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LabRadioButtons extends Application {
  private Scene scene; HBox hb; VBox vb;
  private RadioButton rbBio, rbChem, rbPhy;
  private final ImageView pic = new ImageView();
  final ToggleGroup grpSci = new ToggleGroup();

  @Override public void start(Stage prim) {
    rbBio = new RadioButton("Biology");
    rbBio.setUserData("biology");
    rbBio.setToggleGroup(grpSci);

    rbChem = new RadioButton("Chemistry");
    rbChem.setUserData("chemistry");
    rbChem.setToggleGroup(grpSci);

    rbPhy = new RadioButton("Physics");
    rbPhy.setUserData("physics");
    rbPhy.setToggleGroup(grpSci);

    grpSci.selectedToggleProperty().addListener((val, oldVal, newVal) -> {
      if(grpSci.getSelectedToggle() != null) {
        String tmp = grpSci.getSelectedToggle().getUserData().toString();
        System.out.println("USer selected: " + tmp);
        tmp = "./" + tmp + ".jpg";
        System.out.println("Image file: " + tmp);
        Image image = new Image(tmp);
        pic.setImage(image);
      }
    });

    vb = new VBox(10, rbBio, rbChem, rbPhy);
    hb = new HBox(50, vb, pic);

    prim.setTitle("Example Radio Buttons");
    prim.setWidth(700);
    prim.setHeight(450);
    prim.setScene(scene = new Scene(hb));
    prim.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
