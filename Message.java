public class Message() {

  String message;
  String roomID;
  User source;

  public void Message(String message, String roomID, User source){
    this.message = message;
    this.roomID = roomID;
    this.source = source;
  }

  public String getMessage(){return message;}
  public String getRoomID(){return roomID;}
  public User getSource(){return source;}

}
