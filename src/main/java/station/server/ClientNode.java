/**
* (basic description of the program or class)
*
* @author Kyle Rupp <kdrupp@asu.edu>
* @version April 2020
*/

package station.server;

public class ClientNode {
    
    private HandleClient client;
    private int index;
    private boolean connected;
    private boolean ready;
    
    /**
     * Creates a node to store the information for a client. Index and client i/o should be given.
     * It defaults the connection to null.
     * 
     * @param index Index of the connection. This is used for the parent's list.
     * @param client Client connection.
     */
    public ClientNode(int index, HandleClient client) {
        this.client = client;
        this.index = index;
        connected = false;
        ready = false;
    }
    
    /**
     * Returns the client's name.
     * @return The name.
     */
    public String getName() {
        if (client != null) {
            return client.getName();
        } else {
            return "Unconnected";
        }
    }
    
    public void setClient(HandleClient client) {
        this.client = client;
    }
    
    public HandleClient getClient() {
        return client;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setConnected(boolean connected) {
        this.connected = connected;
    }
    
    public boolean isConnected() {
        return connected;
    }
    
    public void setReady(boolean ready) {
        this.ready = ready;
    }
    
    public boolean isReady() {
        return ready;
    }

}

