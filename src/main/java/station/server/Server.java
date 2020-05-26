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
    
    private ClientList clients = new ClientList(8);
    
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
                
                appendLog("Starting game loop...");
                
                startGameLoop();
                
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
                                    
                                    if (!clients.add(client)) {
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
                    int command = client.getCommand();
                    switch (command) {
                        case 0:
                            newConnection(client);
                            break;
                        case 1:
                            newOtherClientConnection(client);
                            break;
                        case 3:
                            updatePlayerStatus(client);
                            break;
                        case 5:
                            sendAllPlayerStatus(client);  
                            break;
                        case 13:
                            declareWinner(client);
                            break;
                        case 999:
                            disconnectUser(client.getIndex());
                            break;
                        default:
                            throw new UnknownCommandException("Server recived " + command);
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
    
    private void disconnectUser(int index) {
        try {
            appendLog(clients.get(index).getClient().getName() + " disconnected.");
            clients.remove(index);
            System.out.println("updating names");
            for (int i = 0; i < clients.size(); i++) {
                if (clients.get(i).isConnected()) {
                    for (int j = 0; j < clients.size(); j++) {
                        System.out.println("Sending " + clients.get(j).getName() 
                                + " to client " + i);
                        clients.get(i).getClient().sendCommand(2);
                        clients.get(i).getClient().sendConnect(j, clients.get(j).getName());
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
    
    private void startGameLoop() {
        new Thread(() -> {
        
            boolean running = true;
            while (running) {
                int connectedCount = getConnectedCount();
                if (isEveryoneReady() && connectedCount > 0) {
                    int countdown = 8;
                    for (int j = countdown; j >= 0; j--) {
                        if (isEveryoneReady()) {
                            try {
                                sendCountdown(countdown);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                running = false;
                            } catch (InterruptedException iex) {
                                iex.printStackTrace();
                                running = false;
                            }
     
                            countdown--;
                            if (countdown <= 0) {
                                
                            try {
                                sendGameStartCommand();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                running = false;
                            }
                            return;
                            }
                        } else {
                            appendLog("Stopping countdown.");
                            try {
                                sendCountdown(-2);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                running = false;
                            } catch (InterruptedException intEx) {
                                intEx.printStackTrace();
                                running = false;
                            }
                            j = 0;
                        }
                    }
                }
            }
        
        }).start();
    }
    
    private boolean isEveryoneReady() {
        int connectedCount = 0;
        int readyCount = 0;
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).isConnected()) {
                connectedCount++;
                if (clients.get(i).getClient().getPlayerStatus()) {
                    readyCount++;
                }
            }
        }
        return readyCount >= connectedCount && connectedCount > 0;
    }
    
    private int getConnectedCount() {
        int connectedCount = 0;
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).isConnected()) {
                connectedCount++;
            }
        }
        return connectedCount;
    }
    
    private void sendCountdown(int countdown) throws IOException, InterruptedException {
        if (countdown > 0) {
            appendLog("Starting game in " + countdown);
        }
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).isConnected()) {    
                clients.get(i).getClient().sendCommand(6);
                clients.get(i).getClient().sendIndex(countdown);
            } 
        }
        Thread.sleep(1000);
    }
    
    private void newConnection(HandleClient client) throws IOException {
        System.out.println("New client connected");
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getClient() != null) {
                if (clients.get(i).getClient().equals(client)) {
                    System.out.println("Sending position " + i + " to " + client.getName());
                    client.sendCommand(0);
                    client.sendIndex(i);
                }
            }
        }
    }
    
    private void newOtherClientConnection(HandleClient client) throws IOException {
        System.out.println("Recieved name!");
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).isConnected()) {
                for (int j = 0; j < clients.size(); j++) {
                    System.out.println("Sending " + clients.get(j).getName() + " to client " + i);
                    clients.get(i).getClient().sendCommand(2);
                    clients.get(i).getClient().sendConnect(j, clients.get(j).getName());
                }
            }
        }
    }
    
    private void updatePlayerStatus(HandleClient client) throws IOException {
        System.out.println("Updating players status");
        boolean send = client.getStatus();
        int index = 0;
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).isConnected()) {
                if (clients.get(i).getClient().equals(client)) {
                    index = i;
                }
            }
        }
        clients.get(index).setReady(send);
        if (send) {
            appendLog(clients.get(index).getClient().getName() + " is ready.");
        } else {
            appendLog(clients.get(index).getClient().getName() + " is unready.");
        }
        int tracker = 0;
        try {
            for (int i = 0; i < clients.size(); i++) {
                tracker = i;
                if (clients.get(i).isConnected()) {
                    System.out.println(clients.get(i).getClient().getName() + " is connected.");
                    clients.get(i).getClient().sendCommand(4);
                    clients.get(i).getClient().sendStatus(index, send);
                }
            }
        } catch (SocketException userDisconnect) {
            userDisconnect.printStackTrace();
            disconnectUser(tracker);
        }
    }
    
    private void sendAllPlayerStatus(HandleClient client) throws IOException {
        for (int i = 0; i < clients.size(); i++) {
            client.sendCommand(4);
            client.sendStatus(i, clients.get(i).isReady());
        }   
    }
    
    private void declareWinner(HandleClient client) throws IOException {
        int winner = client.getIndex();
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).isConnected()) {
                clients.get(i).getClient().sendCommand(62);
                clients.get(i).getClient().sendMessage(clients.get(winner).getName() + " has won!");
            }
        }
        startGameLoop();
    }
    
    private void sendGameStartCommand() throws IOException {
        appendLog("Game starting!");
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).isConnected()) {
                clients.get(i).getClient().sendCommand(60);
            }
        }
    }

}
