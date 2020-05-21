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
    
    public ClientList(int size) {
        this.size = size;
        clients = new ArrayList<ClientNode>();
        for(int i = 0; i < size; i++) {
            clients.add(new ClientNode(i, null));
        }
    }
    
    public boolean add(HandleClient client) {
        System.out.println("DEBUG: checking if clients are null");
        for(int i = 0; i < size; i++) {
            System.out.println(((ClientNode)clients.get(i)).isConnected());
            if(!((ClientNode)clients.get(i)).isConnected()) {
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
    
    public void add(int index, HandleClient client) {
        ClientNode newClient = (ClientNode) clients.get(index);
        newClient.setClient(client);
        newClient.setConnected(true);
    }
    
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
