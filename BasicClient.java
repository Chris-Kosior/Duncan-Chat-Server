/******
 * BasicClient
 * Author: Christian Duncan
 *
 * This program strips a client down to the simplest elements.
 * It simply connects to a server, sends a message and reads a response.
 ******/
import java.net.*;
import java.io.*;

public class BasicClient {
    private String hostname;

    public static void main(String[] args) throws Exception {
	BasicClient c = new BasicClient(args[0]);
	c.run();
    }

    public BasicClient(String hostname) { this.hostname = hostname; }

    public void run() throws Exception {
        // Create a socket connection to server at hostname and port
	Socket socket = new Socket(hostname, BasicServer.PORT);
	System.out.println("Connected. Communicating from port " +
                           socket.getLocalPort() + " to port " +
                           socket.getPort() + ".");

        // Get the two IO-streams for communicating back-and-forth
        // Note: for ease-of-use we wrap the streams in Writers/Readers
	PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	out.println("Hey there.  Are you around?");  // Send message to client
	String message = in.readLine();              // Get response
	System.out.println("RECEIVED: " + message);  // Display it

        // Close the streams (never cross 'em)
	in.close();
	out.close();
	socket.close();
    }
}
