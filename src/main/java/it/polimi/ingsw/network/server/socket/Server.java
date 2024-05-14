package it.polimi.ingsw.network.server.socket;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.InvalidIdForDrawingException;
import it.polimi.ingsw.model.InvalidGamePhaseException;
import it.polimi.ingsw.model.NonexistentPlayerException;
import it.polimi.ingsw.model.SuspendedGameException;
import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color.InvalidColorException;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.EmptyDeckException;
import it.polimi.ingsw.model.card.InvalidCardIdException;
import it.polimi.ingsw.model.card.InvalidFaceUpCardException;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.chat.message.InvalidMessageException;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.lobby.FullLobbyException;
import it.polimi.ingsw.model.lobby.InvalidPlayersNumberException;
import it.polimi.ingsw.model.lobby.InvalidUsernameException;
import it.polimi.ingsw.model.player.InvalidPlayerActionException;
import it.polimi.ingsw.network.VirtualView;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {
    private final static int PORT = 1234;
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
            ClientHandler handler = new ClientHandler(this, in, out);
            new Thread(handler::run).start();
        }
    }

    //todo handle all the exceptions with report error messages

    public static void main(String[] args) throws IOException {
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        System.out.println("host: " + host + " waiting on port: " + port);
        ServerSocket listenSocket = new ServerSocket(port);
        new Server(listenSocket).runServer();
    }

    public void connect(VirtualView client, String username) throws RemoteException, FullLobbyException, InvalidUsernameException {
        controller.handleConnection(username, client);
    }


    public void disconnect(String username) throws InvalidUsernameException, RemoteException {
        controller.handleDisconnection(username);
    }

    public void sendPing(String username) {

    }

    public void placeStarter(String username, Side side) throws InvalidPlayerActionException, InvalidGamePhaseException {
        controller.placeStarter(username, side);
    }

    public void chooseColor(String username, PlayerColor color) throws NonexistentPlayerException, InvalidColorException, InvalidPlayerActionException, InvalidGamePhaseException, RemoteException {
        controller.chooseColor(username, color);
    }

    public void placeObjectiveCard(String username, int chosenObjective) throws InvalidPlayerActionException, InvalidGamePhaseException {
        controller.placeObjectiveCard(username, chosenObjective);
    }

    public void placeCard(String username, int frontId, int backId, Side side, Position position) throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException, InvalidGamePhaseException, SuspendedGameException, InvalidCardIdException {
        controller.placeCard(username, frontId, backId, side, position);
    }

    public void draw(String username, int idToDraw) throws InvalidPlayerActionException, InvalidIdForDrawingException, EmptyDeckException, InvalidGamePhaseException, InvalidFaceUpCardException {
        controller.draw(username, idToDraw);
    }

    public void sendMessage(Message message) throws InvalidMessageException {
        controller.sendMessage(message);
    }

    public void setPlayersNumber(String username, int playersNumber) throws InvalidPlayersNumberException {
        controller.setPlayersNumber(playersNumber);
    }
}
