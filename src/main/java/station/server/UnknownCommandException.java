/**
* (basic description of the program or class)
*
* @author Kyle Rupp <kdrupp@asu.edu>
* @version April 2020
*/

package station.server;

public class UnknownCommandException extends Exception {
    
    public UnknownCommandException(String msg) {
        super(msg);
    } 

}
