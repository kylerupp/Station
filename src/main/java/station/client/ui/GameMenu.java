/**
* (basic description of the program or class)
*
* @author Kyle Rupp <kdrupp@asu.edu>
* @version April 2020
*/

package station.client.ui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import station.client.Client;

public class GameMenu {

    private JLabel status;
    private JLabel turnCounter;
    
    private JPanel panel;
    
    private JButton action;
    private JButton lobby;
    private JButton endTurn;
    
    private Client client;
    
    private List readyBoxes;
    
    /**
     * Creates the main game menu.
     * 
     * @param client The client to interact with.
     */
    public GameMenu(Client client, int size) {
        this.client = client;
        
        readyBoxes = new ArrayList<JCheckBox>();
        for(int i = 0; i < size; i++) {
            JCheckBox box = new JCheckBox();
            box.setBackground(Color.red);
            box.setEnabled(false);
            readyBoxes.add(box);
        }
        
        panel = new JPanel();
        
        status = new JLabel("Game has started" + client.getName() 
                + "! First to press the button wins!");
        
        action = new JButton("Win!");
        lobby = new JButton("Lobby");
        
        
        panel.add(status);
        panel.add(action);
        panel.add(lobby);
        
        endTurn = new JButton("End Turn");
        panel.add(endTurn);
        
        turnCounter = new JLabel("Turn 0");
        panel.add(turnCounter);
        
        panel.add(getReadyBoxes());
        
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
        
        endTurn.addActionListener(e -> {
            try {
                client.getHandler().sendCommand(15);
                client.getHandler().sendIndex(client.getHandler().getConnectedPos());
                client.getHandler().sendStatus(true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
    }
    
    public JPanel getReadyBoxes() {
        JPanel readyPanel = new JPanel();
        for(int i = 0; i < readyBoxes.size(); i++) {
            readyPanel.add((JCheckBox)readyBoxes.get(i));
        }
        return readyPanel;
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
    
    public void updateReadyBox(int index, int status) {
        JCheckBox box = (JCheckBox)readyBoxes.get(index);
        switch (status) {
            case 0:
                box.setBackground(Color.red);
                box.setSelected(false);
                break;
            case 1:
                box.setBackground(Color.green);
                box.setSelected(true);
                break;
            default:
                box.setBackground(Color.gray);
                box.setSelected(false);
        }
    }
    
}
