/******
* InventoryServer
* Author: Christian Duncan
* Modified by: <ADD YOUR NAMES HERE>
*
* This server tracks inventory sent to it from various
* clients.  A client connects to the server, establishes a connection
* which is handled on a separate thread, and sends it commands
* processed by the server.
* The commands (the protocol) supported by the server are:

* Internally, the server stores the inventory in a HashMap.
* WARNING: THIS CODE IS NOT DESIGNED TO BE THREAD-SAFE!
*          IT NEEDS TO BE FIXED TO HANDLE RACE CONDITIONS
*          AND OTHER THREADING ISSUES.
******/
import java.net.*;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

// A Basic Inventory Server handling commands as described above.
public class InventoryServer {
  public static final int DEFAULT_PORT = 1518;
  public static final String END_LIST = "---";

  public int port;      // The port to listen to for this server
  public boolean done;  // Is server still running?

  //make array list, stores the list of all messages using Message class
  private ArrayList<Message> messageList;

  // A new thread class to handle connections with a client
  class Connection extends Thread {
    Socket socket;
    PrintWriter out;
    BufferedReader in;
    boolean done;
    String name;   // Name of the Connection (for debugging purposes)
    User user;

    /**
    * The constructor
    * @param socket The socket attached to this connection
    * @param name The "name" of this connection - for debugging purposes
    **/
    public Connection(Socket socket, String name) {
      // First get the I/O streams for communication.
      //   in -- Used to read input from client
      //   out -- Used to send output to client
      //if an exception gets thrown, move it back to run() function
      this.out = new PrintWriter(socket.getOutputStream(), true);
      this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      this.socket = socket; done = false; this.name = name;
      this.user =  new User(name);
    }

    /**
    * Start running the thread for this connection
    **/
    public void run() {
      try {

        while (!done) {
          if(in.ready()){
            String line = in.readLine();
            processLine(line);
          }
          //if ready is true, then send out messages in list, if false it's blocked. maybe turn this into a function outside of run
        }
      } catch (IOException e) {
        printMessage("I/O Error while communicating with Client.  Terminating connection.");
        printMessage("    Message: " + e.getMessage());
      }

      // Close the socket
      try {
        printMessage("CLIENT is closing down.");
        if (in != null) in.close();
        if (out != null) out.close();
        if (socket != null) socket.close();
      } catch (IOException e) {
        printMessage("Error trying to close socket. " + e.getMessage());
      }
    }

    /**
    * Process one line that has been sent through this connection
    * @param line The line to process
    **/
    private void processLine(String line) {
      // Process the line
      if (line == null || line.equals("EXIT")) {
        done = true;   // Nothing left in input stream or EXIT command sent
        out.println("GOOD-BYE.");//make this a server.exit, to let server know who is leaving, so it can send out message
      } else if (line.startsWith("ENTER ")) {
        // Reset the inventory list
        String theName = line.substring(6); //gives you then name you would use after 6 characters
        user.setName(theName);
        // System.err.println("DEBUG: Resetting inventory.");
        out.println("SET NAME TO " + theName);
        printMessage("SET NAME TO " + theName);
      } else {
        out.println("UNRECOGNIZED COMMAND: " + line);
      }
    }

    /**
    * Print out the message (with a little name id in front)
    * @param message The message to print out
    **/
    private void printMessage(String message) {
      System.out.println("["+ name + "]: " + message);
    }
  }

  // The set of client connections. Not really needed here but useful sometimes.
  public HashSet<Connection> connection;
  //  public HashMap<String, String> inventory;

  /**
  * Basic Constructor
  * @param The port to listen to
  **/
  public InventoryServer(int port) {
    this.port = port;
    this.done = false;
    this.connection = new HashSet<Connection>();
    this.messageList = new ArrayList<Message>();
    //  this.inventory = new HashMap<String, String>();
  }

  /**
  * Create a new thread associated with the client connection.
  * And store the thread (for reference sake).
  * @param Socket The client socket to associate with this connection thread.
  **/
  public void addConnection(Socket clientSocket) {
    String name = clientSocket.getInetAddress().toString();
    System.out.println("Inventory Server: Connecting to client: " + name);
    Connection c = new Connection(clientSocket, name);
    connection.add(c);
    c.start();    // Start the thread.
  }

  /**
  * Reset the inventory.
  *   Just creates a new empty hash map.
  **/
  public synchronized void reset() { //synchronize the reset function so it is not Interrupted.
    this.inventory = new HashMap<String, String>();
  }

  /**
  * Add item to the inventory
  * @param item The item to add.
  **/
  public synchronized void addItem(String item) { //synchronize the add function

  }

  /**
  * Print out the items in the inventory to the given outputstream
  * @param out The stream to use for outputting items
  **/
  public void reportItems(PrintWriter out) {

  }

  /**
  * Run the main server... just listen for and create connections
  **/
  public void run() {
    System.out.println("Inventory Server:  WELCOME!  Starting up...");
    try {
      // Create a server socket bound to the given port
      ServerSocket serverSocket = new ServerSocket(port);

      while (!done) {
        // Wait for a client request, establish new thread, and repeat
        Socket clientSocket = serverSocket.accept();
        addConnection(clientSocket);
      }
    } catch (Exception e) {
      System.err.println("ABORTING: An error occurred while creating server socket. " +
      e.getMessage());
      System.exit(1);
    }
  }

  /**
  * The main entry point.  It just processes the command line argument
  * and starts an instance of the InventoryServear running.
  **/
  public static void main(String[] args) {
    int port = DEFAULT_PORT;

    // Set the port if specified
    if (args.length > 0) {
      try {
        port = Integer.parseInt(args[0]);
      } catch (NumberFormatException e) {
        System.err.println("Usage: java InventoryServer [PORT]");
        System.err.println("       PORT must be an integer.");
        System.exit(1);
      }
    }

    // Create and start the server
    InventoryServer s = new InventoryServer(port);
    s.run();
  }
}
