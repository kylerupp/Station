/**
* This class is a main entry for the server class.
*
* @author Kyle Rupp <kdrupp@asu.edu>
* @version May 2020
* @since May 2020
*/

package station;

import station.server.Server;

public class MainServer {
    
    /**
     * Main entry into the program.
     * @param args Command line args.
     */
    public static void main(String[] args) {
        System.out.println("Running Server!");
        Server server = new Server();
        server.start();
    }

}
