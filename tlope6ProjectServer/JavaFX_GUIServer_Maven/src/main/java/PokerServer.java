//port int
//serverScocket: ServerSocket
//clients : List<ClientHandler>
//startServer() : void
//stopServer() : void
//broadCast(string): void

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class PokerServer {
    private int port;
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new ArrayList<>();
    private Consumer<String> callback; //for udpating the listview
    private boolean running = false;



    public PokerServer(int port, Consumer<String> callback) {
        this.port = port;
        this.callback = callback;
    }


    //starting the server

    public void startServer() {
        running = true;

        Thread serverThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                callback.accept("Server started on port: " + port);

                while (running) {
                    Socket clientSocket = serverSocket.accept();
                    ClientHandler handler = new ClientHandler(clientSocket, callback
                    );
                    clients.add(handler);

                    callback.accept("New client connected.");
                    handler.start();
                }
            } catch (IOException e) {
                 callback.accept("Server stopped or failed to start");
            }
        });


        serverThread.setDaemon(true);
        serverThread.start();
    }


    //stop the server
    public void stopServer() {
        running = false;

        try {
            for (ClientHandler c: clients) {
                c.close();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            callback.accept("Error closing server.");
        }
        callback.accept("Server stopped.");
    }

    //broadcasting the message to all clients
    public void broadcast(String message) {
        for (ClientHandler handler : clients) {
            handler.sendMessage(message);
        }
    }

    //gettiong the list of clients
    public List<ClientHandler> getClients() {
        return clients;
    }


}