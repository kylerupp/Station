package station.server;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * A class to handle connections to clients.
 *
 * @author kylerupp
 * @version May 6, 2020
 * @since May 6, 2020
 */
public class HandleClient {
    
    private DataInputStream fromClient;
    private String clientName;
    
    /**
     * Constructor for server side client handler.
     * @param in Input Stream from client.
     */
    public HandleClient(InputStream in) {
        fromClient = new DataInputStream(in);
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
    
    public String getName() {
        return clientName;
    }

}
