public class Employee {
  private static int lastIDUsed = 100;
  private int id;
  private String firstName;
  private String lastName;
  private float salary;
  private float bonusRate;

  Employee(String tmpFName, String tmpLName, float tmpSalary) {
    this.firstName = tmpFName;
    this.lastName = tmpLName;
    this.salary = tmpSalary;

  }

  public String toString() {
    return "Employee [" + this.getID() + "]" + this.getLastName() + ", " + this.getFirstName()
            + "\n\tPaid: $" + this.getSalary() + "/year:"
            + "\n\tBonus Rate:" + this.getBonusRate() + "\n";
  }

  public int getID() {
    return this.id;
  }

  public void setID(int id) {
    this.id = id;
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

  public float getSalary() {
    return this.salary;
  }

  public void setSalary(float salary) {
    this.salary = salary;
  }

  public float getBonusRate() {
    return this.bonusRate;
  }

  public void setBonusRate(float bonusRate) {
    this.bonusRate = bonusRate;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }


}
