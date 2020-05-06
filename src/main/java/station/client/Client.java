/**
* (basic description of the program or class)
*
* @author Kyle Rupp <kdrupp@asu.edu>
* @version April 2020
*/

package station.client;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Client extends Frame {
    
    private final JTextField field = new JTextField();
    
    public void start() {
        this.setSize(new Dimension(400, 200));
        
        JButton button = new JButton();
        
        field.setPreferredSize(new Dimension(100, 20));
        
        JPanel panel = new JPanel();
        
        panel.add(field);
        panel.add(button);
        
        this.add(panel);
        
        this.setTitle("Client");
        this.setVisible(true);
        
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
    
    public void connect() {
        try {
            Socket socket = new Socket("localHost", 8000);
        
            DataInputStream fromServer = new DataInputStream(socket.getInputStream());
            
            DataOutputStream toServer = new DataOutputStream(socket.getOutputStream());
            
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }

}
