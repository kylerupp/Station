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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Client extends Frame {
    
    private final JTextField field = new JTextField();
    private DataOutputStream toServer;
    private DataInputStream fromServer;
    private JPanel mainPanel;
    
    private ClientSideServerHandler handler;
    
    /**
     * This method starts the client window. Sets appropriate sizes and variables and shows window.
     */
    public void start() {
        this.setSize(new Dimension(400, 200));
        
        mainPanel = connectPanel();
        this.add(mainPanel);
        
        this.setTitle("Client");
        this.setVisible(true);
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
    
    private JPanel connectPanel() {
        JLabel title = new JLabel("Station");
        JLabel label = new JLabel("Name");
        
        field.setPreferredSize(new Dimension(100, 20));
        
        JButton button = new JButton("Connect");
        JPanel panel = new JPanel();
        
        panel.add(title);
        panel.add(label);
        panel.add(field);
        panel.add(button);
        
        button.addActionListener(e -> {
            handler = new ClientSideServerHandler();
            handler.sendMessage(field.getText());
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
            handler.sendMessage(field.getText());
        });
        
        return panel;
    }

}
