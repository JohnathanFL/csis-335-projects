import java.util.ArrayList;

class Message {
  String sender,text;


  @Override
  public String toString() {
    return "Message{" +
        "sender='" + sender + '\'' +
        ", text='" + text + '\'' +
        '}';
  }

  public Message(String sender, String text) {
    this.sender = sender;
    this.text = text;
  }
}

public class SMSInbox {
  private ArrayList<Message> msgList;

  public void addMsg(String sender, String msg) {
    msgList.add(new Message(sender, msg));
  }


  @Override
  public String toString() {
    String str = "SMSInbox: {\n";
    for(Message msg : this.msgList)
      str += "\t" + msg + ",\n";

    str += "}";

    return str;
  }
}
