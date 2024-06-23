package it.polimi.ingsw.network.server.socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.model.ClientGame;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;
import it.polimi.ingsw.network.client.socket.message.*;
import it.polimi.ingsw.network.heartbeat.HeartBeat;
import it.polimi.ingsw.network.heartbeat.HeartBeatHandler;
import it.polimi.ingsw.network.heartbeat.HeartBeatMessage;
import it.polimi.ingsw.network.server.socket.message.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * Sends the commands received by the Server Socket to the Client Socket.
 */
public class ClientHandler implements VirtualView, HeartBeatHandler {
    private final Server server;
    private final PrintWriter out;
    private final BufferedReader input;
    private final Socket clientSocket;
    private final Gson gson;
    private String username;
    private final HeartBeat heartBeat;

    /**
     * Constructs a <code>ClientHandler</code> with the <code>server</code>, <code>input</code>, <code>out</code> and
     * the <code>clientSocket</code> provided
     *
     * @param server       the server
     * @param input        the reader
     * @param out          the printer
     * @param clientSocket the representation of the clientSocket
     */
    public ClientHandler(Server server, BufferedReader input, PrintWriter out, Socket clientSocket) {
        this.server = server;
        this.out = out;
        this.input = input;
        this.clientSocket = clientSocket;
        GsonBuilder builder = new GsonBuilder().enableComplexMapKeySerialization();
        this.gson = builder.create();
        heartBeat = new HeartBeat(this, "handler", this, "handler");
        heartBeat.startHeartBeat();
    }

    /**
     * Runs the <code>ClientHandler</code>
     */
    public void run() {
        System.out.println("ClientHandler has started");
        try {
            String line = input.readLine();
            while (line != null) {
                System.out.println("Received from the client: " + line);
                NetworkMessage message = gson.fromJson(line, NetworkMessage.class);
                Type type = message.getNetworkType();
                String sender = message.getSender();

                switch (type) {
                    case CONNECT:
                        server.connect(this, sender);
                        break;
                    case PLACE_STARTER:
                        PlaceStarterMessage placeStarterMessage = gson.fromJson(line, PlaceStarterMessage.class);
                        server.placeStarter(sender, placeStarterMessage.getSide());
                        break;
                    case CHOOSE_COLOR:
                        ChooseColorMessage chooseColorMessage = gson.fromJson(line, ChooseColorMessage.class);
                        server.chooseColor(sender, chooseColorMessage.getColor());
                        break;
                    case PLACE_OBJECTIVE:
                        PlaceObjectiveMessage placeObjectiveMessage = gson.fromJson(line, PlaceObjectiveMessage.class);
                        server.placeObjectiveCard(sender, placeObjectiveMessage.getChosenObjective());
                        break;
                    case PLACE_CARD:
                        PlaceCardMessage placeCardMessage = gson.fromJson(line, PlaceCardMessage.class);
                        server.placeCard(sender, placeCardMessage.getFrontId(), placeCardMessage.getBackId(),
                                placeCardMessage.getSide(), placeCardMessage.getPosition());
                        break;
                    case DRAW:
                        DrawMessage drawMessage = gson.fromJson(line, DrawMessage.class);
                        server.draw(sender, drawMessage.getIdDraw());
                        break;

                    case SEND_CHAT_MESSAGE:
                        SendChatMessage sendChatMessage = gson.fromJson(line, SendChatMessage.class);
                        server.sendMessage(sendChatMessage.getMessage());
                        break;

                    case SET_PLAYER_NUMBER:
                        SetPlayerNumberMessage setPlayerNumberMessage = gson.fromJson(line, SetPlayerNumberMessage.class);
                        server.setPlayersNumber(sender, setPlayerNumberMessage.getNumPlayers());
                        break;

                    case DISCONNECT:
                        server.disconnect(sender);
                        closeResources();
                        heartBeat.terminate();
                        break;

                    case HEARTBEAT:
                        HeartBeatMessage ping = gson.fromJson(line, HeartBeatMessage.class);
                        heartBeat.registerMessage(ping);
                        //receivePing(ping);
                        break;
                }
                line = input.readLine();
            }
        } catch (IOException e) {
            System.err.println("server stops hearing: channel has been closed");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCreator() {
        UpdateCreatorMessage message = new UpdateCreatorMessage();
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAfterLobbyCrash() {
        UpdateAfterLobbyCrashMessage message = new UpdateAfterLobbyCrashMessage();
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
        closeResources();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAfterConnection(ClientGame clientGame) {
        //System.out.println("UpdateAfterConnection");
        UpdateAfterConnectionMessage message = new UpdateAfterConnectionMessage(clientGame);
        String jsonMessage = gson.toJson(message);
        System.out.println("Message to send " + jsonMessage);
        out.println(jsonMessage);
        out.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdatePlayersInLobby(List<String> usernames) {
        UpdatePlayersInLobbyMessage message = new UpdatePlayersInLobbyMessage(usernames);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateFullLobby() {
        FullLobbyMessage message = new FullLobbyMessage();
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
        closeResources();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateExceedingPlayer() {
        ExceedingPlayerMessage message = new ExceedingPlayerMessage();
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
        closeResources();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdatePlayerStatus(boolean isConnected, String username) {
        UpdatePlayerStatusMessage message = new UpdatePlayerStatusMessage(isConnected, username);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateColor(PlayerColor color, String username) {
        UpdateColorMessage message = new UpdateColorMessage(username, color);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateObjectiveCard(ClientObjectiveCard chosenObjective, String username) {
        UpdateObjectiveCardMessage message = new UpdateObjectiveCardMessage(chosenObjective, username);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard, Side placedSide, Position position) {
        UpdateAfterPlaceMessage message = new UpdateAfterPlaceMessage(positionToCornerCovered, newAvailablePositions, newResources, points, username, placedCard, placedSide, position);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateAfterDraw(ClientCard drawnCard, ClientFace newTopDeck, ClientCard newFaceUpCard, String username, int boardPosition) {
        UpdateAfterDrawMessage message = new UpdateAfterDrawMessage(drawnCard, newTopDeck, newFaceUpCard, username, boardPosition);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateChat(Message message) {
        UpdateChatMessage clientMessage = new UpdateChatMessage(message);
        String jsonMessage = gson.toJson(clientMessage);
        out.println(jsonMessage);
        out.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateCurrentPlayer(int currentPlayerIdx, GamePhase phase) {
        UpdateCurrentPlayerMessage message = new UpdateCurrentPlayerMessage(currentPlayerIdx, phase);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateGameState() {
        UpdateSuspendedGameMessage message = new UpdateSuspendedGameMessage();
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showWinners(List<String> winners) {
        ShowWinnersMessage message = new ShowWinnersMessage(winners);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportError(String details) {
        ReportErrorMessage message = new ReportErrorMessage(details);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resultOfLogin(boolean accepted, String username, String details) throws RemoteException {
        if (accepted) {
            this.username = username;
        }
        ResultOfLogin message = new ResultOfLogin(accepted, username, details);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleUnresponsiveness(String unresponsiveListener) {
        System.err.println("User " + unresponsiveListener + " has crashed");
        terminate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receivePing(HeartBeatMessage ping) {
        String jsonMessage = gson.toJson(ping);
        out.println(jsonMessage);
        out.flush();
    }

    /**
     * Closes input.
     */
    private void closeResources() {
        try {
            input.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("input has already been closed");
        }
    }

    /**
     * Closes input, ends the timer and disconnects the player from the game.
     */
    private void terminate() {
        closeResources();
        heartBeat.terminate();
        // leave after being registered in the game
        if (username != null) {
            server.disconnect(username);
        }
    }
}
