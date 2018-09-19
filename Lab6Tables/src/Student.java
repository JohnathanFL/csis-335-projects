import javafx.beans.property.SimpleStringProperty;

public class Student {
  private final SimpleStringProperty stName, stMajor, stAdvisor;

  Student(String name, String major, String advisor) {
    this.stName = new SimpleStringProperty(name);
    this.stMajor = new SimpleStringProperty(major);
    this.stAdvisor = new SimpleStringProperty(advisor);
  }
}
