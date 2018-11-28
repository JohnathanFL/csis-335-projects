class Message {
  String sender, textMsg;


  @Override
  public String toString() {
    return "Message{" +
        "sender='" + sender + '\'' +
        ", textMsg='" + textMsg + '\'' +
        '}';
  }

  public Message(String sender, String text) {
    this.sender = sender;
    this.textMsg = text;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getTextMsg() {
    return textMsg;
  }

  public void setTextMsg(String textMsg) {
    this.textMsg = textMsg;
  }
}
