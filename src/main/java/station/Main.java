package station;

import station.client.Client;

/**
 * This class is a main entry for the client.
 *
 * @author kylerupp
 * @version May 2020
 * @since May 2020
 */
public class Main {
    
    /**
     * Main entry into the program.
     * @param args Command line args.
     */
    public static void main(String[] args) {
        System.out.println("Running Client!");
        Client client = new Client();
        client.start();
    }

} 