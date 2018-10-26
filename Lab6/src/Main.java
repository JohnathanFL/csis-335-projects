import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
  private ObservableList<Student> stuData = FXCollections.observableArrayList(
    new Student("John Doe", "CS", "Ficek"),
      new Student("Mary Miller", "CIS", "Brekke"),
      new Student("Gary Gray", "CIT", "Chen"),
      new Student("Ann Abel", "CIT", "Brekke"),
      new Student("Bob Brown", "CS", "Ficek")
  );

  private static Stage stage;
  private Scene scene;
  private TableView stuTable = new TableView();

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primStage) {
    stage = primStage;

    createUI();
    stage.show();
  }


  private void createUI() {
    TableColumn nameCol = new TableColumn("Name"), majorCol = new TableColumn("Major"), advisorCol = new TableColumn("Advisor");
    {// Populate table
      this.stuTable.setEditable(true);
      nameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("stName"));
      majorCol.setCellValueFactory(new PropertyValueFactory<Student, String>("stMajor"));
      advisorCol.setCellValueFactory(new PropertyValueFactory<Student, String>("stAdvisor"));
      this.stuTable.setItems(this.stuData);

      this.stuTable.getColumns().addAll(nameCol, majorCol, advisorCol);
    }


    final TextField txtName = new TextField(), txtMajor = new TextField(), txtAdvisor = new TextField();
    txtName.setPromptText("name...");
    txtMajor.setPromptText("major...");
    txtAdvisor.setPromptText("advisor...");

    txtName.setMaxWidth(nameCol.getPrefWidth());
    txtAdvisor.setMaxWidth(advisorCol.getPrefWidth());
    txtMajor.setMaxWidth(majorCol.getPrefWidth());

    final Button btnAdd = new Button("Add Student");
    btnAdd.setDefaultButton(true);
    btnAdd.setOnAction(e -> {
      this.stuData.add(new Student(txtName.getText(), txtMajor.getText(), txtAdvisor.getText()));
      //System.out.println(this.stuData);
      txtName.clear();
      txtAdvisor.clear();
      txtMajor.clear();
    });

    final HBox hb = new HBox();
    hb.getChildren().addAll(txtName, txtMajor, txtAdvisor, btnAdd);


    VBox vb = new VBox();
    Text topText = new Text("Students");
    topText.setFont(new Font("Arial", 20));

    vb.setSpacing(5);
    vb.setPadding(new Insets(20,0,0,10));
    vb.getChildren().addAll(topText, stuTable, hb);
    this.scene = new Scene(vb, 300,300);


    this.scene = new Scene(new FlowPane(vb));
    stage.setScene(this.scene);
  }
}
