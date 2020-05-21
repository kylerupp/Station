package station.client;

import java.awt.TextArea;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import station.server.UnknownCommandException;

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
    
    private int connectedPos = -1;
    
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
    
    /**
     * Calls the server to see if a command can be run.
     * 1 is to give the server the name of a connecting client. If the server will listen to the
     * connection a 2 is returned.
     * 
     * @param command Int of the client side command.
     * @return If the server reads the command.
     */
    public boolean sendCommand(int command) {
        try {
            toServer.writeInt(command);
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    /**
     * Used to listen for commands. Once called a thread is created to listen for commands which
     * will then enact a response for the command once called. If no command can be called it will
     * throw an unknown command error and print to log. Otherwise if IOException occurs the loop
     * will exit. Commands are as follows. Commands coming in from the server should only be even
     * numbers.
     * 
     * <p>0-10 reserved for lobby connection</p>
     * <p>2. new player connected</p>
     * <p>4. player updated ready status</p>
     */
    public void commandListener() {
        new Thread(() -> {
            boolean running = true;
            while (running) {
                try {
                    switch (fromServer.readInt()) {
                        case 0:
                            connectedPos = fromServer.readInt();
                            System.out.println("My position is " + connectedPos);
                            toServer.writeInt(1);
                            break;
                        case 2:
                            int pos = fromServer.readInt();
                            String name = fromServer.readUTF();
                            System.out.println("Updating pos " + pos + " with name " + name);
                            Client.updatePlayerConnect(pos, name);
                            break;
                        case 4:
                            Client.updatePlayerStatus(fromServer.readInt(),
                                    fromServer.readBoolean());
                            break;
                        case 999:
                            break;
                        default:
                            throw new UnknownCommandException("error");
                    }
                } catch (SocketException close) { 
                    System.out.println("Closing.");
                    running = false;
                } catch (IOException ex) {
                    ex.printStackTrace();
                    running = false;
                } catch (UnknownCommandException cex) {
                    cex.printStackTrace();
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
    
    public int getConnectedPos() {
        return connectedPos;
    }
    
    public void sendIndex(int index) throws IOException {
        toServer.writeInt(index);
    }
    
    public void sendStatus(boolean ready) throws IOException {
        toServer.writeBoolean(ready);
    }

}
