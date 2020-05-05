/**
* (basic description of the program or class)
*
* @author Kyle Rupp <kdrupp@asu.edu>
* @version April 2020
*/

package station;

import station.server.Server;

public class MainServer {
    
    public static void main(String[] args) {
        System.out.println("Running Server!");
        Server server = new Server();
        server.start();
    }

}
