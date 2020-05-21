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
    
    public ClientNode(int index, HandleClient client) {
        this.client = client;
        this.index = index;
        connected = false;
    }
    
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

}

