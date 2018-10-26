import javafx.beans.property.SimpleStringProperty;

public class Student {
  public String getStName() {
    return this.stName.get();
  }

  public String getStMajor() {
    return this.stMajor.get();
  }

  public String getStAdvisor() {
    return this.stAdvisor.get();
  }

  public void setStName(String newName) {
    this.stName.set(newName);
  }
  public void setStMajor(String newName) {
    this.stMajor.set(newName);
  }
  public void setStAdvisor(String newName) {
    this.stAdvisor.set(newName);
  }

  private final SimpleStringProperty stName, stMajor, stAdvisor;

  Student(String name, String major, String advisor) {
    this.stName = new SimpleStringProperty(name);
    this.stMajor = new SimpleStringProperty(major);
    this.stAdvisor = new SimpleStringProperty(advisor);
  }
  @Override
  public java.lang.String toString() {
    return String.format("Student %s in %s advised by %s", this.getStName(), this.getStMajor(), this.getStAdvisor());
  }
}
