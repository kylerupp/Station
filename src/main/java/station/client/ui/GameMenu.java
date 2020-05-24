/**
* (basic description of the program or class)
*
* @author Kyle Rupp <kdrupp@asu.edu>
* @version April 2020
*/

package station.client.ui;

import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import station.client.Client;

public class GameMenu {

    private JLabel status;
    private JPanel panel;
    
    private JButton action;
    
    private Client client;
    
    /**
     * Creates the main game menu.
     * 
     * @param client The client to interact with.
     */
    public GameMenu(Client client) {
        this.client = client;
        
        panel = new JPanel();
        
        status = new JLabel("Game has started" + client.getName() 
                + "! First to press the button wins!");
        
        action = new JButton("Win!");
        
        panel.add(status);
        panel.add(action);
        
        action.addActionListener(e -> {
            try {
                client.getHandler().sendCommand(13);
                client.getHandler().sendIndex(client.getHandler().getConnectedPos());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
    }
    
    public JPanel getPanel() {
        return panel;
    }
    
    public void endGame(String winner) {
        action.setEnabled(false);
        status.setText(winner);
        JButton lobby = new JButton("Lobby");
        panel.add(lobby);
        lobby.addActionListener(e -> {
            client.changeScene(3);
        });
    }
    
}
