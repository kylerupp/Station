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
    
    public String getMessage() {
        String ret = "Error with message";
        try {
            ret = fromClient.readUTF();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

}
