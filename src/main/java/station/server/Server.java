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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Server extends Frame {
    
    private TextArea log;
    private ServerSocket socket;
    
    private ClientList clients = new ClientList(2);
    
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
                                    HandleClient client = new HandleClient(player.getInputStream(), 
                                            player.getOutputStream());
                                    
                                    if(!clients.add(client)){
                                        throw new IOException();
                                    }
                                    if (client.getCommand() != 1) {
                                        return;
                                    }
                                    String name = client.getNameFromClient();
                                    appendLog(name + " connected.");
                                    //sendMessage(name + " connected.");
                                    
                                    //messageListener(client);
                                    commandListener(client);
                                    
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
    
    private void commandListener(HandleClient client) {
        new Thread(() -> {
            boolean running = true;
            while (running) {
                try {
                    switch (client.getCommand()) {
                        case 0:
                            for(int i = 0; i < clients.size(); i++) {
                                if(clients.get(i).getClient() != null) {
                                    if (clients.get(i).getClient().equals(client)) {
                                        System.out.println("Sending position " + i + " to " + client.getName());
                                        client.sendIndex(i);
                                    }
                                }
                            }
                        case 1:
                            System.out.println("Recieved name!");
                            for (int i = 0; i < clients.size(); i++) {
                                for (int j = 0; j < clients.size(); j++) {
                                    System.out.println("Sending " + clients.get(j).getName() 
                                            + " to client " + i);
                                    clients.get(i).getClient().sendCommand(2);
                                    clients.get(i).getClient().sendConnect(j, clients.get(j).getName());
                                }
                            }
                            break;
                        case 3:
                            boolean send = client.getStatus();
                            int index = 0;
                            for (int i = 0; i < clients.size(); i++) {
                                if (clients.get(i).equals(client)) {
                                    index = i;
                                }
                            }
                            for (int i = 0; i < clients.size(); i++) {
                                clients.get(i).getClient().sendCommand(4);
                                clients.get(i).getClient().sendStatus(index, send);
                            }
                            break;
                        case 999:
                            //int disconnect = client.getIndex();
                            clients.remove(client.getIndex());
                            break;
                        default:
                            throw new UnknownCommandException("ocmmand");
                    }
                        
                } catch (IOException ex) {
                    ex.printStackTrace();
                    running = false;
                } catch (UnknownCommandException commandEx) {
                    commandEx.printStackTrace();
                }
            }
        }).start();
    }
    
    /*private void messageListener(HandleClient client) {
        new Thread(() -> {
            boolean running = true;
            while (running) {
                try {
                    String str = client.getName() + ": " + client.getMessage();
                    appendLog(str);
                    sendMessage(str);
                } catch (EOFException e) {
                    String exit = client.getName() + " disconnected.";
                    appendLog(exit);
                    sendMessage(exit);
                    running = false;
                } catch (IOException ex) {
                    String exit = client.getName() + " disconnected.";
                    appendLog("IOException occured with " + client.getName());
                    appendLog(exit);
                    sendMessage(exit);
                    running = false;
                }
            }
        }).start();
    }*/
    
    /*private void sendMessage(String str) {
        for (int i = 0; i < clients.size(); i++) {
            clients.get(i).sendMessage(str + "\n");
        }
    }*/
    
    private void appendLog(String msg) {
        log.append("[" + new Date() + "] " + msg + "\n");
    }

}
