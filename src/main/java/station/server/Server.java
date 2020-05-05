/**
* (basic description of the program or class)
*
* @author Kyle Rupp <kdrupp@asu.edu>
* @version April 2020
*/

package station.server;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.ScrollPane;
import java.awt.TextArea;
import javax.swing.JPanel;

public class Server extends Frame {
    
    private TextArea log;
    
    public void start() {
        log = new TextArea();
        ScrollPane area = new ScrollPane();
        area.add(log);
        area.setPreferredSize(new Dimension(450, 200));
        this.setTitle("Server");
        this.setSize(new Dimension(480, 220));
        this.add(area);
        this.setVisible(true);
    }

}
