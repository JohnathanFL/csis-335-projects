import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.ArrayList;
import java.util.List;

public class LabObsrvList {
  public static void main(String[] args) {
    List<String> regList = new ArrayList<>();
    ObservableList<String> obList = FXCollections.observableList(regList);
    obList.addListener((ListChangeListener)(change) -> {
      System.out.println("Detected a change! ");
    });

    System.out.println("Add to obList: ");
    obList.add("item one");
    System.out.printf("regList: \n%s\nobList: \n%s\n", regList, obList);

    System.out.println("\n\nAdd to regList: ");
    regList.add("item two");
    System.out.printf("regList: \n%s\nobList: \n%s\n", regList, obList);
  }
}
