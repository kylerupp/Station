package station.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * A client side class to handle connection to a server.
 *
 * @author kylerupp
 * @version May 6, 2020
 * @since May 6, 2020
 */
public class ClientSideServerHandler {
    
    DataInputStream fromServer;
    DataOutputStream toServer;
    
    /**
     * Defualt constructor for the client side handler. This constructor uses a
     * default ip of "localHost" with a port of 8000.
     */
    public ClientSideServerHandler() throws ConnectException {
        try {
            Socket socket = new Socket("localHost", 8000);
        
            fromServer = new DataInputStream(socket.getInputStream());
            
            toServer = new DataOutputStream(socket.getOutputStream());
            
            
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }
    
    /**
     * Sends a message to the server. Takes in the string to send.
     * @param msg String of the message to send to the server.
     */
    public void sendMessage(String msg) {
        try {
            toServer.writeUTF(msg);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Closes the connection to the server.
     */
    public void close() {
        try {
            fromServer.close();
            toServer.close();
        } catch (IOException ex) {
            System.out.println("Error closing socket on clients end.");
        }
    }

}
