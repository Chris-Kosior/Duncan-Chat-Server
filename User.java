//Written by Francisco (so far ;] )
import java.io.*;
import java.util.Scanner;

public class User{
  /*
  public static File members = new File(""); // Whatever gets used for user info
  public static FileWriter userwrite(members, true);
  public static FileReader userread(members, true);
  Scanner reader = new Scanner(members);
  int idCheck = 0;
  String nameCheck = new String("");

  String Username = new String(setName(uName));
  int ID = setID();

  // int channels[] = new int[3]; Leaving this here for when we get around to chat channels

  userwrite.write(Username + "          " + ID);
}
*/
private String name;
private int ID;

public User(String name){
  this.name = name;
  this.ID = 0;
}

public int getID(){
  return ID;
}

public String getName(){
  return name;
}

public void setName(String name){
  this.name = name;
}
/*
public int setID(){
int ID = Math.floor(Math.random() * 10000); //Method to generate IDs, 5 digits long

for(i=0; i<10000; i++){
idCheck = reader.nextInt();
if (idCheck == ID){
setID();
}
}
return ID;
}

public static String setName(uname){
String name = new String(uname);
for(i=0; i<10000;i++){
nameCheck = reader.nextString();
if (nameCheck.equals(name)){
setName(name);
}
}

return name;
*/
}
