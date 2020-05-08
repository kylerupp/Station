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
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

public class Server extends Frame {
    
    private TextArea log;
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
                //Start server
                socket = new ServerSocket(8000);
                appendLog("Server started at " + socket.getInetAddress().getHostAddress() 
                        + ":" + socket.getLocalPort());
                appendLog("Waiting for clients to join the session...");
                
                //listen for connections
                new Thread(() -> {
                    try {
                        //player connecction
                        while (true) {
                            Socket player = socket.accept();
                            appendLog("Attempting connection with " 
                                    + player.getInetAddress().getHostAddress());
                        
                            //listen for messages
                            new Thread(() -> {
                                try {
                                    HandleClient client = new HandleClient(player.getInputStream());
                                    appendLog("Client connected with name " 
                                            + client.getNameFromClient());
                                    
                                    new Thread(() -> {
                                        boolean running = true;
                                        while (running) {
                                            try {
                                                appendLog(client.getName() + ": " 
                                                        + client.getMessage());
                                            } catch (EOFException e) {
                                                appendLog("Ending connection with " 
                                                        + client.getName());
                                                running = false;
                                            } catch (IOException ex) {
                                                appendLog("IOException occured with " 
                                                        + client.getName());
                                                appendLog("Ending connection with " 
                                                        + client.getName());
                                                running = false;
                                            }
                                        }
                                    }).start();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }).start();
                        }
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
    
    private void appendLog(String msg) {
        log.append("[" + new Date() + "] " + msg + "\n");
    }

}
