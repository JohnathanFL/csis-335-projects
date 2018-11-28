import java.util.ArrayList;

public class SMSInbox {
  private ArrayList<Message> msgList;

  public void addMsg(String sender, String msg) {
    msgList.add(new Message(sender, msg));
  }

  public SMSInbox() {
    this.msgList = new ArrayList<>();
  }

  @Override
  public String toString() {
    String str = "SMSInbox: {\n";
    int i = 1;
    for(Message msg : this.msgList)
      str += "\t" + i++ + ": " + msg + ",\n";

    str += "}";

    return str;
  }
}
