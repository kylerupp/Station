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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Client extends Frame {
    
    private final JTextField field = new JTextField();
    private DataOutputStream toServer;
    private DataInputStream fromServer;
    private JPanel mainPanel;
    
    /**
     * This method starts the client window. Sets appropriate sizes and variables and shows window.
     */
    public void start() {
        this.setSize(new Dimension(400, 200));
        
        mainPanel = sendNamePanel();
        this.add(mainPanel);
        
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
        
            fromServer = new DataInputStream(socket.getInputStream());
            
            toServer = new DataOutputStream(socket.getOutputStream());
            
            
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }
    
    private JPanel sendNamePanel() {
        JLabel label = new JLabel("Name");
        
        field.setPreferredSize(new Dimension(100, 20));
        
        JButton button = new JButton("Send");
        JPanel panel = new JPanel();
        
        panel.add(label);
        panel.add(field);
        panel.add(button);
        
        button.addActionListener(e -> {
            sendMessage(field.getText());
            //this.remove(mainPanel);
            mainPanel = sendMessagePanel();
            this.revalidate();
        });
        
        return panel;
    }
    
    private JPanel sendMessagePanel() {
        JButton button = new JButton();
        
        field.setPreferredSize(new Dimension(100, 20));
        
        JPanel panel = new JPanel();
        
        panel.add(field);
        panel.add(button);
        
        button.addActionListener(e -> {
            sendMessage(field.getText());
        });
        
        return panel;
    }
    
    private void sendMessage(String msg) {
        try {
            toServer.writeUTF(msg);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void close() {
        try {
            fromServer.close();
            toServer.close();
        } catch (IOException ex) {
            System.out.println("Error closing socket on clients end.");
        }
    }

}
