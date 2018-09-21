/**
 * @author Johnathan Lee
 *
 * Employee: A simple class for storing info about an employee.
 */
public class Employee {
  private static int lastIDUsed = 100; //>! Using lastIDUsed++ to get each new ID.
  private int id;
  private String firstName;
  private String lastName;
  private int salary;
  private float bonusRate; // 0.02 if salary < 50000, else 0.05

  Employee(String tmpFName, String tmpLName, int tmpSalary) {
    this.firstName = tmpFName;
    this.lastName = tmpLName;
    this.setSalary(tmpSalary);


    this.id = lastIDUsed++;
  }

  public String toString() {
    return String.format("Employee %d(%s, %s): Paid $%d.00/yr at %.2f%% bonus", this.getID(), this.getLastName(), this.getFirstName(), this.getSalary(), this.getBonusRate());
  }

  public int getID() {
    return this.id;
  }

  public String getLastName() {
    return this.lastName;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public int getSalary() {
    return this.salary;
  }

  public void setSalary(int salary) {
    this.salary = salary;
    if(this.salary >= 50000)
      this.bonusRate = 0.05f;
    else
      this.bonusRate = 0.02f;
  }

  public float getBonusRate() {
    return this.bonusRate;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }


}
