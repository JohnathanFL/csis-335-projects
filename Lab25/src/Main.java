public class Main {
  public static void main(String[] args) {
    SMSInbox inbox = new SMSInbox();
    inbox.addMsg("Joe", "Hello there");
    inbox.addMsg("Mary", "What's up?");
    inbox.addMsg("Mary", "Lunch at noon?");

    System.out.println(inbox);

  }
}
