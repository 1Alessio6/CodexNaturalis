package it.polimi.ingsw.network.server.socket;

import it.polimi.ingsw.controller.Controller;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final static int PORT = 1234;
    private final ServerSocket listenSocket;
    private final List<ClientHandler> clients;
    private Controller controller;

    public Server(ServerSocket listenSocket) {
        this.listenSocket = listenSocket;
        clients = new ArrayList<ClientHandler>();
        this.controller = new Controller();
    }

    // method to accept clients' connections represented by the ClientHandler
    private void runServer() throws IOException {
        Socket clientSocket = null;
        while ((clientSocket = listenSocket.accept()) != null) {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Received connection");
            // create the stub related to the client.
            ClientHandler handler = new ClientHandler(this, in, out);
            new Thread(() -> {
                try {
                    handler.run();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    public static void main(String[] args) throws IOException {
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        System.out.println("host: " + host + " waiting on port: " + port);
        ServerSocket listenSocket = new ServerSocket(port);
        new Server(listenSocket).runServer();
    }
}
