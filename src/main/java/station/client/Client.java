/**
* This class handles the main connectivity for the client.
*
* @author Kyle Rupp <kdrupp@asu.edu>
* @version May 2020
* @since May 2020
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
    private DataOutputStream toServer;
    
    /**
     * This method starts the client window. Sets appropriate sizes and variables and shows window.
     */
    public void start() {
        this.setSize(new Dimension(400, 200));
        
        JButton button = new JButton();
        
        field.setPreferredSize(new Dimension(100, 20));
        
        JPanel panel = new JPanel();
        
        panel.add(field);
        panel.add(button);
        
        button.addActionListener(e -> {
            sendMessage(field.getText());
        });
        
        
        this.add(panel);
        
        this.setTitle("Client");
        this.setVisible(true);
        
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
    
    /**
     * This method connects the client to the server.
     */
    public void connect() {
        try {
            Socket socket = new Socket("localHost", 8000);
        
            DataInputStream fromServer = new DataInputStream(socket.getInputStream());
            
            toServer = new DataOutputStream(socket.getOutputStream());
            
            fromServer.close();
            toServer.close();
            
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }
    
    private void sendMessage(String msg) {
        try {
            toServer.writeUTF(msg);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
