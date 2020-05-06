package station.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * (basic description of the program or class)
 * 
 * Completion time: (estimation of hours spent on this program)
 *
 * @author kylerupp
 * @version May 6, 2020
 * @since May 6, 2020
 */
public class HandleClient {
    
    private DataInputStream fromClient;
    
    public HandleClient(InputStream in) {
        fromClient = new DataInputStream(in);
    }
    
    /**
     * Listens for a message from the client.
     * @return client's message.
     */
    public String getMessage() {
        String ret = "Error with message";
        try {
            ret = fromClient.readUTF();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ret;
    }
    
    /**
     * Listens for a name from the client.
     * @return Name fetched from client.
     */
    public String getName() {
        String ret = "Error with fetching name";
        try {
            ret = fromClient.readUTF();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

}
