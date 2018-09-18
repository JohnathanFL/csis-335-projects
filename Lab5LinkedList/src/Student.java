public class Student implements Comparable {
  private String lastName, firstName;
  private float gpa;

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public float getGPA() {
    return gpa;
  }

  public void setGPA(float gpa) {
    this.gpa = gpa;
  }

  public Student() {
    this.lastName = this.firstName = "";
    this.gpa = 0.0f;
  }

  public Student(String firstName, String lastName, float gpa) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.gpa = gpa;
  }

  @Override
  public int compareTo(Object tmp) {
    if(tmp instanceof Student) {
      Student stu = (Student)tmp;
      return Float.compare(this.getGPA(), stu.getGPA());
    }
    return 0;
  }

}
