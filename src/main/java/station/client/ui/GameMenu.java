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
    private JLabel turnCounter;
    
    private JPanel panel;
    
    private JButton action;
    private JButton lobby;
    
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
        lobby = new JButton("Lobby");
        
        
        panel.add(status);
        panel.add(action);
        panel.add(lobby);
        
        turnCounter = new JLabel("Turn 0");
        panel.add(turnCounter);
        
        lobby.addActionListener(e -> {
            client.changeScene(3);
            client.getHandler().sendCommand(5);
        });
        
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
    
    /**
     * Called whenever the game is started. It updates the games status label and only enables the
     * game buttons.
     */
    public void startGame() {
        status.setText("Game has started!");
        action.setEnabled(true);
        lobby.setEnabled(false);
    }
    
    /**
     * Called whenever the game is finished. It will update the status label with the winner and
     * only enable the return to lobby button.
     * 
     * @param winner String of the winner.
     */
    public void endGame(String winner) {
        action.setEnabled(false);
        status.setText(winner);
        lobby.setEnabled(true);
    }
    
    public void updateTurnCounter(int turn) {
        turnCounter.setText("Turn " + turn);
    }
    
}
