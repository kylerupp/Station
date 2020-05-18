/**
* (basic description of the program or class)
*
* @author Kyle Rupp <kdrupp@asu.edu>
* @version April 2020
*/

package station.client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConnectScreen {
    
    private JPanel panel;
    
    private List playerLabels;
    private List playerReadyStatus;
    
    private int size;
    
    public ConnectScreen(int size) {
        panel = new JPanel();
        
        this.size = size;
        JPanel infoPanel = new JPanel(new GridLayout(4, 4));
        
        playerLabels = new ArrayList<JLabel>();
        playerReadyStatus = new ArrayList<JCheckBox>();
        
        for(int i = 0; i < size; i++) {
            JCheckBox check = new JCheckBox();
            check.setEnabled(false);
            JLabel label = new JLabel("Unconnected");
            playerLabels.add(label);
            playerReadyStatus.add(check);
            
            panel.add(label);
            panel.add(check);
        }
        
        JButton readyButton = new JButton("Ready");
        readyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(readyButton, BorderLayout.NORTH);
        panel.add(infoPanel, BorderLayout.CENTER);
    }
    
    public boolean updatePlayerLabel(int index, String str) {
        if (index > size || index < 0) {
            return false;
        }
        JLabel label = (JLabel) playerLabels.get(index);
        label.setText(str);
        
        return true;
    }
    
    public boolean updatePlayerStatus(int index, boolean check) {
        if(index > size || index < 0) {
            return false;
        }
        JCheckBox status = (JCheckBox) playerReadyStatus.get(index);
        status.setSelected(check);
        
        return true;
    }
    
    public JPanel getPanel() {
        return panel;
    }

}
