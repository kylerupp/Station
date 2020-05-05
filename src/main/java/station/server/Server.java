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
        JPanel scene = new JPanel();
        scene.add(area);
        scene.setPreferredSize(new Dimension(450, 200));
        this.setTitle("Server");
        this.setSize(new Dimension(450, 200));
        this.add(scene);
        this.setVisible(true);
    }

}
