/**
* (basic description of the program or class)
*
* @author Kyle Rupp <kdrupp@asu.edu>
* @version April 2020
*/

package station.client.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import station.client.ClientSideServerHandler;

public class ConnectScreen {
    
    private ClientSideServerHandler handler;
    
    private JPanel panel;
    
    private List playerLabels;
    private List playerReadyStatus;
    
    private JLabel infoLabel;
    
    private int size;
    private boolean ready = false;
    
    /**
     * Constructor for the connect screen. This will initialize all of the necessary connection info
     * that will be updated as the server runs.
     * 
     * @param size of the maximum lobby connection.
     */
    public ConnectScreen(int size, ClientSideServerHandler client, String name) {
        this.handler = client;
        
        panel = new JPanel(new BorderLayout());
        
        infoLabel = new JLabel("Welcome, " + name);
        
        this.size = size;
        JPanel infoPanel = new JPanel(new GridLayout(4, 4));
        
        playerLabels = new ArrayList<JLabel>();
        playerReadyStatus = new ArrayList<JCheckBox>();
        
        for (int i = 0; i < size; i++) {
            JCheckBox check = new JCheckBox();
            check.setEnabled(false);
            JLabel label = new JLabel("Unconnected");
            playerLabels.add(label);
            playerReadyStatus.add(check);
            
            infoPanel.add(label);
            infoPanel.add(check);
        }
        
        JButton readyButton = new JButton("Ready");
        readyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        readyButton.addActionListener(e -> {
            ready = !ready;
            try {
                handler.sendCommand(3);
                handler.sendStatus(ready);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
        panel.add(readyButton, BorderLayout.NORTH);
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(infoLabel, BorderLayout.SOUTH);
    }
    
    /**
     * Updates the label of a player that is connected.
     * 
     * @param index Of the player connecting.
     * @param str Name of the player.
     * 
     * @return True if the label can be updated. 
     */
    public boolean updatePlayerLabel(int index, String str) {
        if (index > size || index < 0) {
            return false;
        }
        JLabel label = (JLabel) playerLabels.get(index);
        label.setText(str);
        
        return true;
    }
    
    /**
     * This method updates the player status on the screen. A check will be formed if the player is
     * ready.
     * 
     * @param index Index of the player readiness.
     * @param check If the selected player is ready.
     * 
     * @return true if the player can be updated.
     */
    public boolean updatePlayerStatus(int index, boolean check) {
        if (index > size || index < 0) {
            return false;
        }
        JCheckBox status = (JCheckBox) playerReadyStatus.get(index);
        status.setSelected(check);
        
        return true;
    }
    
    public void updateStatusLabel(String text) {
        infoLabel.setText(text);
    }
    
    public boolean isReady() {
        return ready;
    }
    
    public void setReady(boolean ready) {
        this.ready = ready;
    }
    
    public JPanel getPanel() {
        return panel;
    }

}
