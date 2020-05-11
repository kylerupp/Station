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
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.ConnectException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Client extends Frame {
    
    private final JTextField messageField = new JTextField();
    private final JTextField nameField = new JTextField();
    private JPanel mainPanel;
    
    private TextArea feed;
    
    private ClientSideServerHandler handler;
    
    /**
     * This method starts the client window. Sets appropriate sizes and variables and shows window.
     */
    public void start() {
        this.setSize(new Dimension(460, 300));
        this.setResizable(false);
        
        mainPanel = connectPanel();
        this.add(mainPanel);
        
        this.setTitle("Client");
        this.setVisible(true);
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                if (handler != null) {
                    handler.close();
                }
            }
        });
    }
    
    private JPanel connectPanel() {
        JPanel titlePanel = new JPanel();
        JLabel title = new JLabel("Station");
        
        title.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(title);
        
        JPanel connectPanel = new JPanel();
        JLabel label = new JLabel("Name");
        
        nameField.setPreferredSize(new Dimension(100, 20));
        connectPanel.add(label);
        connectPanel.add(nameField);
        
        JButton button = new JButton("Connect");
        connectPanel.add(button);
        
        JPanel panel = new JPanel();
        
        panel.setLayout(new BorderLayout());
        panel.add(title, BorderLayout.NORTH);
        panel.add(connectPanel, BorderLayout.CENTER);
        
        button.addActionListener(e -> {
            try {
                handler = new ClientSideServerHandler();
                handler.sendMessage(nameField.getText());
                handler.setName(nameField.getText());
                this.remove(mainPanel);
                mainPanel = sendMessagePanel();
                this.add(mainPanel);
                this.revalidate();
            } catch (ConnectException conex) {
                //todo create dialoug
                System.out.println("Coudnt' connect");
            }
        });
        
        return panel;
    }
    
    private JPanel sendMessagePanel() { 
        JLabel name = new JLabel("Welcome, " + handler.getName() + "!");
        this.add(name, BorderLayout.NORTH);
        
        JButton button = new JButton("Send");
        
        messageField.setPreferredSize(new Dimension(300, 27));
        
        JPanel panel = new JPanel();
        JPanel main = new JPanel(new GridLayout(2, 1));
        
        panel.add(messageField);
        panel.add(button);
        main.add(getMessageLog());
        main.add(panel);
        
        button.addActionListener(e -> {
            sendMessage();
        });
        
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent event) {
                //if enter
                if(event.getKeyCode() == 13) {
                    sendMessage();
                }
            }
            
            @Override
            public void keyReleased(KeyEvent event) {
                
            }
            
            @Override
            public void keyTyped(KeyEvent event) {
                
            }
        });
        
        handler.listenForMessages(feed);
        
        return main;
    }
    
    private JPanel getMessageLog() {
        JPanel chat = new JPanel();
        
        feed = new TextArea();
        feed.setEditable(false);
        feed.setPreferredSize(new Dimension(400, 200));
        chat.add(feed);
        chat.setPreferredSize(new Dimension(400, 200));
        
        return chat;
    }
    
    private void sendMessage() {
        handler.sendMessage(messageField.getText());
        messageField.setText("");
    }

}
