package station.client;

import java.awt.TextArea;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
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
    
    private String name;
    
    /**
     * Constructor for the client side server handler. Takes in a host as a string and a port as an
     * int.
     * 
     * @param host String for the host.
     * @param port Port for the server.
     * @throws ConnectException if the client cannot connect.
     */
    public ClientSideServerHandler(String host, int port) throws ConnectException {
        try {
            Socket socket = new Socket(host, port);
        
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
    
    /**
     * This message listens for messages from the server. This is handled in a new thread such that
     * the program can keep running and go about its business with other methods. Messages sent
     * to this thread will be appended to the given text area. Messages should already be
     * formatted.
     * 
     * @param feed TextArea to display messages.
     */
    public void listenForMessages(TextArea feed) {
        new Thread(() -> {
            boolean running = true;
            while (running) {
                try {
                    feed.append(fromServer.readUTF());
                } catch (SocketException socketEx) { 
                    System.out.println("Disconnected from client");
                    running = false;
                } catch (IOException ex) {
                    ex.printStackTrace();
                    running = false;
                }
            }
        }).start();
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

}
