package it.polimi.ingsw.network.server.socket;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.chat.message.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final ServerSocket listenSocket;

    private Controller controller;

    public Server(ServerSocket listenSocket) {
        this.listenSocket = listenSocket;
        this.controller = new Controller();
    }

    // method to accept clients' connections represented by the ClientHandler
    private void runServer() throws IOException {
        Socket clientSocket = null;
        while ((clientSocket = listenSocket.accept()) != null) {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Received connection");
            ClientHandler handler = new ClientHandler(this, in, out, clientSocket);
            new Thread(handler::run).start();
        }
    }

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt("1234");
        System.out.println("Server is ready on port " + port);
        ServerSocket listenSocket = new ServerSocket(port);
        new Server(listenSocket).runServer();
    }

    public void connect(ClientHandler client, String username) {
        controller.handleConnection(username, client);
    }

    public void disconnect(String username) {
        controller.handleDisconnection(username);
    }

    public void placeStarter(String username, Side side) {
        controller.placeStarter(username, side);
    }

    public void chooseColor(String username, PlayerColor color) {
        controller.chooseColor(username, color);
    }

    public void placeObjectiveCard(String username, int chosenObjective) {
        controller.placeObjectiveCard(username, chosenObjective);
    }

    public void placeCard(String username, int frontId, int backId, Side side, Position position) {
        controller.placeCard(username, frontId, backId, side, position);
    }

    public void draw(String username, int idToDraw) {
        controller.draw(username, idToDraw);
    }

    public void sendMessage(Message message) {
        controller.sendMessage(message);
    }

    public void setPlayersNumber(String username, int playersNumber) {
        controller.setPlayersNumber(username, playersNumber);
    }
}
