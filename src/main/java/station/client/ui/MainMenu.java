/**
* (basic description of the program or class)
*
* @author Kyle Rupp <kdrupp@asu.edu>
* @version April 2020
*/

package station.client.ui;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import station.client.Client;

public class MainMenu {
    
    JLabel title = new JLabel("Station");
    
    JButton offlineButton = new JButton("Offline");
    JButton onlineButton = new JButton("Online");
    
    JPanel panel = new JPanel();
    
    Client client;
    /**
     * Constructor to make the main menu.
     */
    public MainMenu(Client client) {
        this.client = client;
        panel.setLayout(new GridLayout(3, 1));
        panel.add(title);
        panel.add(offlineButton);
        panel.add(onlineButton);
        
        offlineButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "You should look into getting internet.\n(Offline is not yet implemented)");
            System.out.println("Playing Offline!");
            
        });
        
        onlineButton.addActionListener(e -> {
            System.out.println("Playing Online!");
            client.changeScene(1);
        });
    }
    
    public JPanel getPanel() {
        return panel;
    }

}
