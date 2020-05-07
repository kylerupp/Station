/**
* This class handles the main connectivity for the client.
*
* @author Kyle Rupp <kdrupp@asu.edu>
* @version May 2020
* @since May 2020
*/

package station.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ConnectException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Client extends Frame {
    
    private final JTextField messageField = new JTextField();
    private final JTextField nameField = new JTextField();
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
                if(handler != null) {
                    handler.close();
                }
            }
        });
    }
    
    private JPanel connectPanel() {
        JPanel titlePanel = new JPanel();
        JLabel title = new JLabel("Station");
        
        titlePanel.add(title);
        
        JPanel connectPanel = new JPanel();
        JLabel label = new JLabel("Name");
        JButton button = new JButton("Connect");
        
        nameField.setPreferredSize(new Dimension(100, 20));
        connectPanel.add(label);
        connectPanel.add(nameField);
        connectPanel.add(button);
        
        JPanel panel = new JPanel();
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(connectPanel, BorderLayout.CENTER);
        
        button.addActionListener(e -> {
            try {
                handler = new ClientSideServerHandler();
                handler.sendMessage(nameField.getText());
                this.remove(mainPanel);
                mainPanel = sendMessagePanel();
                this.add(mainPanel);
                this.revalidate();
            } catch(ConnectException conex) {
                //todo create dialoug
                System.out.println("Coudnt' connect");
            }
        });
        
        return panel;
    }
    
    private JPanel sendMessagePanel() {
        JButton button = new JButton();
        
        messageField.setPreferredSize(new Dimension(100, 20));
        
        JPanel panel = new JPanel();
        
        panel.add(messageField);
        panel.add(button);
        
        button.addActionListener(e -> {
            handler.sendMessage(messageField.getText());
        });
        
        return panel;
    }

}
