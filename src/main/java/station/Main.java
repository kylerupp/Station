package station;

import station.client.Client;

/**
 * (basic description of the program or class)
 * 
 * Completion time: (estimation of hours spent on this program)
 *
 * @author kylerupp
 * @version May 4, 2020
 * @since May 4, 2020
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("Running Client!");
        Client client = new Client();
        client.start();
        client.connect();
    }

} 