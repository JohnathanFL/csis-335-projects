import java.util.Collections;
import java.util.LinkedList;

public class ExLinkedList {
  static LinkedList<Student> allStudents = new LinkedList<>();

  public static void main(String[] args) {
    allStudents.add(new Student("Molly", "Brown", 3));
    allStudents.add(new Student("Joe", "Thompson", 4));
    allStudents.add(new Student("Bill", "Gray", 2));

    for(Student stu : allStudents)
      System.out.printf("%-10s %-10s %f\n", stu.getLastName(), stu.getLastName(), stu.getGPA());

    Collections.sort(allStudents);

    System.out.println("Sorted by GPA: ");
    for(int i = 0; i < allStudents.size(); i++) {
      Student tmp = allStudents.get(i);
      System.out.printf("%-10s %-10s %3.1f\n", tmp.getLastName(), tmp.getFirstName(), tmp.getGPA());
    }
  }
}
