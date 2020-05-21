/**
* This class handles the main connectivity for the client.
*
* @author Kyle Rupp <kdrupp@asu.edu>
* @version May 2020
* @since May 2020
*/

package station.client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ConnectException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Client extends Frame {
    
    private final JTextField messageField = new JTextField();
    private final JTextField nameField = new JTextField();
    private JPanel mainPanel;
    
    private static ConnectScreen connectScreen = new ConnectScreen(2);
    
    private TextArea feed;
    
    private ClientSideServerHandler handler;
    
    /**
     * This method starts the client window. Sets appropriate sizes and variables and shows window.
     */
    public void start() {
        this.setSize(new Dimension(500, 300));
        this.setResizable(false);
        
        mainPanel = connectPanel();
        this.add(mainPanel);
        
        this.setTitle("Client");
        this.setVisible(true);
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    dispose();
                    if (handler != null) {
                        if (handler.getConnectedPos() != -1) {
                            handler.sendCommand(999);
                            handler.sendIndex(handler.getConnectedPos());
                        }
                        handler.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    
    private JPanel connectPanel() {
        //create main panel;
        JPanel titlePanel = new JPanel();
        
        //title
        JLabel title = new JLabel("Station");
        
        //alignt the title in the middle of the panel and add to main panel
        title.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(title);
        
        //label for the name field
        JLabel nameLabel = new JLabel("Name");

        //panel to hold all the text fields
        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        
        //add the name paramters to the login panel
        loginPanel.add(nameLabel);
        loginPanel.add(nameField);
        
        //field and label for server info
        JLabel serverLabel = new JLabel("Server IP:");
        JTextField serverField = new JTextField();
        
        //add server parameters to login panel
        loginPanel.add(serverLabel);
        loginPanel.add(serverField);
        
        //field and label for port
        JLabel portLabel = new JLabel("Port:");
        JTextField portField = new JTextField();
        
        //add port to login panel
        loginPanel.add(portLabel);
        loginPanel.add(portField);
        
        //sizing of objects
        nameField.setPreferredSize(new Dimension(100, 20));
        serverField.setPreferredSize(new Dimension(100, 20));
        portField.setPreferredSize(new Dimension(100, 20));
        
        //panel to hold login info and connect button
        JPanel connectPanel = new JPanel();
        
        //button for connect
        JButton button = new JButton("Connect");
        
        //add button and login info to panel
        connectPanel.add(loginPanel);
        connectPanel.add(button);
        
        //return panel
        JPanel panel = new JPanel();
        
        //add title and connect panel
        panel.setLayout(new BorderLayout());
        panel.add(title, BorderLayout.NORTH);
        panel.add(connectPanel, BorderLayout.CENTER);
        
        //listener for connect button
        button.addActionListener(e -> {
            try {
                if (serverField.getText().isBlank()) {
                    if (portField.getText().isBlank()) {
                        handler = new ClientSideServerHandler("localHost", 8000);
                    } else {
                        handler = new ClientSideServerHandler("localHost",
                                Integer.parseInt(portField.getText()));
                    }
                } else {
                    handler = new ClientSideServerHandler(serverField.getText(),
                            Integer.parseInt(portField.getText()));
                }
                if (handler.sendCommand(1)) {
                    handler.setName(nameField.getText());
                    handler.sendMessage(nameField.getText());
                    handler.sendCommand(0);
                    handler.commandListener();
                }
                
                this.remove(mainPanel);
                mainPanel = connectScreen.getPanel();
                //mainPanel = sendMessagePanel();
                this.add(mainPanel, BorderLayout.CENTER);
                //this.add(getGamePanel(), BorderLayout.EAST);
                this.revalidate();
            } catch (ConnectException conex) {
                //todo create dialoug
                System.out.println("Coudnt' connect");
            }
        });
        
        //return panel
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
        
        messageField.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent event) {
                //if enter
                if (event.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
            
            public void keyReleased(KeyEvent event){}
            
            public void keyTyped(KeyEvent event){} 
        });
        
        handler.listenForMessages(feed);
        
        return main;
    }
    
    private JPanel getMessageLog() {
        feed = new TextArea();
        feed.setEditable(false);
        feed.setPreferredSize(new Dimension(400, 200));
        //feed.setMinimumSize(new Dimension(400, 200));
        feed.setSize(new Dimension(400, 200));
        
        JPanel chat = new JPanel();
        chat.add(feed);
        chat.setPreferredSize(new Dimension(400, 200));
        
        return chat;
    }
    
    private JPanel getGamePanel() {
        JPanel buyPanel = new JPanel();
        buyPanel.setLayout(new BoxLayout(buyPanel, BoxLayout.Y_AXIS));
        
        JButton buyButton = new JButton("Buy");
        JButton sellButton = new JButton("Sell");
        buyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        sellButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buyPanel.add(buyButton);
        buyPanel.add(sellButton);
        
        buyButton.addActionListener(e -> {
            //sendGameCommand("Bought one unit!");
        });
        
        return buyPanel;
    }
    
    private void sendMessage() {
        handler.sendMessage(messageField.getText());
        messageField.setText("");
    }
    
    public static void updatePlayerConnect(int index, String player) {
        connectScreen.updatePlayerLabel(index, player);
    }
    
    public static void updatePlayerStatus(int index, boolean ready) {
        connectScreen.updatePlayerStatus(index, ready);
    }

}
