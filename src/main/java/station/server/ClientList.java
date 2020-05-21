/**
* (basic description of the program or class)
*
* @author Kyle Rupp <kdrupp@asu.edu>
* @version April 2020
*/

package station.server;

import java.util.ArrayList;
import java.util.List;

public class ClientList {
    
    private List clients;
    
    private int size;
    
    /**
     * Creates a list of ClientNodes of a given size.
     * 
     * @param size Size of the list.
     */
    public ClientList(int size) {
        this.size = size;
        clients = new ArrayList<ClientNode>();
        for (int i = 0; i < size; i++) {
            clients.add(new ClientNode(i, null));
        }
    }
    
    /**
     * Adds a client. This method looks for the first open slot and will add a client.
     * 
     * @param client Client I/O to add.
     * @return True if added.
     */
    public boolean add(HandleClient client) {
        System.out.println("DEBUG: checking if clients are null");
        for (int i = 0; i < size; i++) {
            System.out.println(((ClientNode)clients.get(i)).isConnected());
            if (!((ClientNode)clients.get(i)).isConnected()) {
                System.out.println("Client at index " + i + " was null! returning.");
                ClientNode newClient = (ClientNode)clients.get(i);
                newClient.setClient(client);
                newClient.setConnected(true);
                return true;
            }
            System.out.println("Client at index " + i + " was not null.");
        }
        return false;
    }
    
    /**
     * This method adds a client I/O at a given index.
     * 
     * @param index Index to add the client.
     * @param client Client to add.
     */
    public void add(int index, HandleClient client) {
        ClientNode newClient = (ClientNode) clients.get(index);
        newClient.setClient(client);
        newClient.setConnected(true);
    }
    
    /**
     * This method removes a client at an index.
     * 
     * @param index To remove client from.
     */
    public void remove(int index) {
        ClientNode oldClient = (ClientNode) clients.get(index);
        oldClient.getClient().close();
        oldClient.setClient(null);
        oldClient.setConnected(false);
    }
    
    public ClientNode get(int index) {
        return (ClientNode) clients.get(index);
    }
    
    public List getClients() {
        return clients;
    }
    
    public int size() {
        return size;
    }

}
