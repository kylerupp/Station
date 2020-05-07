/**
* This class is to handle the main IO for the server.
*
* @author Kyle Rupp <kdrupp@asu.edu>
* @version May 2020
* @since May 2020
*/

package station.server;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.ScrollPane;
import java.awt.TextArea;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

public class Server extends Frame {
    
    private TextArea log;
    private Socket playerOne;
    private ServerSocket socket;
    
    /**
     * This method starts the server client. Method shows window and handles server sockets.
     */
    public void start() {
        log = new TextArea();
        log.setEditable(false);
        ScrollPane area = new ScrollPane();
        area.add(log);
        area.setPreferredSize(new Dimension(450, 200));
        this.setTitle("Server");
        this.setSize(new Dimension(480, 220));
        this.add(area);
        this.toFront();
        this.setVisible(true);
        
        new Thread(() -> {
            try {
                socket = new ServerSocket(8000);
                log.append("[" + new Date() + "] Server started at " 
                        + socket.getInetAddress().getHostAddress()
                        + ":" + socket.getLocalPort() + "\n");
                log.append("[" + new Date() + "] Waiting for players to join the session...\n");
                
                playerOne = socket.accept();
                log.append("[" + new Date() + "] Attempting connection with  " 
                        + playerOne.getInetAddress().getHostAddress() + "\n");
                
                log.append("[" + new Date() + "] starting thread with player one.\n");
                new Thread(() -> {
                    try {
                        HandleClient clientOne = new HandleClient(playerOne.getInputStream());
                        log.append("Client one's name is " + clientOne.getName() + "\n");
                        new Thread(() -> {
                            while(true) {
                                log.append(clientOne.getMessage() + "\n");
                            }
                        }).start();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }).start();
                
            } catch (SocketException se) {
                System.out.println("Closing sockets");
            } catch (IOException e) {
                e.printStackTrace();
                log.append("Error with server handling...");
            }
        }).start();
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                dispose();
            }
        });
    }

}
