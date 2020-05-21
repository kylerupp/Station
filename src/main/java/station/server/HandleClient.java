package station.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A class to handle connections to clients.
 *
 * @author kylerupp
 * @version May 6, 2020
 * @since May 6, 2020
 */
public class HandleClient {
    
    private DataInputStream fromClient;
    private DataOutputStream toClient;
    private String clientName;
    
    /**
     * Constructor for server side client handler.
     * @param in Input Stream from client.
     */
    public HandleClient(InputStream in, OutputStream out) {
        fromClient = new DataInputStream(in);
        toClient = new DataOutputStream(out);
    }
    
    /**
     * Listens for a message from the client.
     * @return client's message.
     * @throws EOFException if end of stream is reached
     * @throws IOException ioexception.
     */
    public String getMessage() throws EOFException, IOException {

        return fromClient.readUTF();
    }
    
    /**
     * Listens for a name from the client.
     * @return Name fetched from client.
     */
    public String getNameFromClient() {
        String ret = "Error with fetching name";
        try {
            ret = fromClient.readUTF();
            clientName = ret;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ret;
    }
    
    /**
     * This method sends the server a message with the given string. This is used for the basic
     * message passing. The message send should already be formatted with the Date and user.
     * @param str Message to be sent to the server.
     */
    public void sendMessage(String str) {
        try {
            toClient.writeUTF(str);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public int getCommand() throws IOException {
        return fromClient.readInt();
    }
    
    public void sendCommand(int command) throws IOException  {
        toClient.writeInt(command);
    }
    
    public String getName() {
        return clientName;
    }
    
    public boolean getStatus() throws IOException {
        return fromClient.readBoolean();
    }
    
    public int getIndex() throws IOException {
        return fromClient.readInt();
    }
    
    public void sendStatus(int index, boolean ready) throws IOException {
        toClient.writeInt(index);
        toClient.writeBoolean(ready);
    }
    
    public void sendConnect(int index, String name) throws IOException {
        toClient.writeInt(index);
        toClient.writeUTF(name);
    }
    
    public void sendIndex(int index) throws IOException {
        toClient.writeInt(index);
    }

    public void close() {
        try {
            toClient.close();
            fromClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (toClient != null) {
                toClient = null;
            }if (fromClient != null) {
                fromClient = null;
            }
        }
    }
}
