package it.polimi.ingsw.network.server.socket;

import com.google.gson.Gson;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.model.ClientGame;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;
import it.polimi.ingsw.network.client.socket.message.*;
import it.polimi.ingsw.network.client.socket.message.ClientPingMessage;
import it.polimi.ingsw.network.heartbeat.HeartBeatListener;
import it.polimi.ingsw.network.server.socket.Server;
import it.polimi.ingsw.network.server.socket.message.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientHandler implements VirtualView, HeartBeatListener {
    private final Server server;
    private final PrintWriter out;
    private final BufferedReader input;
    private final Gson gson;
    private final ExecutorService executor;
    private String username;
    private Timer timerForClientResponse;
    private static final int DELAY_FOR_CLIENTS_RESPONSE = 10000;

    public ClientHandler(Server server, BufferedReader input, PrintWriter out) {
        this.server = server;
        this.out = out;
        this.input = input;
        gson = new Gson();
        executor = Executors.newSingleThreadExecutor();
        timerForClientResponse = new Timer();
    }

    public void addClientUsername(String username) {
        this.username = username;
    }

    public void run() {
        try {
            String line = input.readLine();
            while (line != null) {
                System.out.println("Received input: " + line);
                ServerMessage message = gson.fromJson(line, ServerMessage.class);
                ServerType type = message.getType();
                String sender = message.getSender();

                switch (type) {
                    case CONNECT:
                        executor.submit(() -> {
                            server.connect(this, sender);
                        });
                        break;
                    case PLACE_STARTER:
                        PlaceStarterMessage placeStarterMessage = gson.fromJson(line, PlaceStarterMessage.class);
                        executor.submit(() -> server.placeStarter(sender, placeStarterMessage.getSide()));
                        break;
                    case CHOOSE_COLOR:
                        ChooseColorMessage chooseColorMessage = gson.fromJson(line, ChooseColorMessage.class);
                        executor.submit(() -> server.chooseColor(sender, chooseColorMessage.getColor()));
                        break;
                    case PLACE_OBJECTIVE:
                        PlaceObjectiveMessage placeObjectiveMessage = gson.fromJson(line, PlaceObjectiveMessage.class);
                        executor.submit(() -> server.placeObjectiveCard(sender, placeObjectiveMessage.getChosenObjective()));
                        break;
                    case PLACE_CARD:
                        PlaceCardMessage placeCardMessage = gson.fromJson(line, PlaceCardMessage.class);
                        executor.submit(() -> server.placeCard(sender, placeCardMessage.getFrontId(), placeCardMessage.getBackId(),
                                placeCardMessage.getSide(), placeCardMessage.getPosition()));
                        break;
                    case DRAW:
                        DrawMessage drawMessage = gson.fromJson(line, DrawMessage.class);

                        executor.submit(() -> server.draw(sender, drawMessage.getIdDraw()));
                        break;

                    case SEND_CHAT_MESSAGE:
                        SendChatMessage sendChatMessage = gson.fromJson(line, SendChatMessage.class);

                        executor.submit(() -> server.sendMessage(sendChatMessage.getMessage()));
                        break;

                    case SET_PLAYER_NUMBER:
                        SetPlayerNumberMessage setPlayerNumberMessage = gson.fromJson(line, SetPlayerNumberMessage.class);

                        executor.submit(() -> server.setPlayersNumber(sender, setPlayerNumberMessage.getNumPlayers()));
                        break;

                    case DISCONNECT:
                        executor.submit(() -> {
                            server.disconnect(sender);
                            terminate();
                        });
                        break;

                    case SEND_PING:
                        receivePing(username);
                        break;
                }
                line = input.readLine();
            }
        } catch (IOException e) {
            System.err.println("channel has been closed");
        }
    }

    @Override
    public void updateCreator() throws RemoteException {
        UpdateCreatorMessage message = new UpdateCreatorMessage();
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void updateAfterLobbyCrash() throws RemoteException {
        UpdateAfterLobbyCrashMessage message = new UpdateAfterLobbyCrashMessage();
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void updateAfterConnection(ClientGame clientGame) throws RemoteException {
        UpdateAfterConnectionMessage message = new UpdateAfterConnectionMessage(clientGame);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void showUpdatePlayersInLobby(List<String> usernames) throws RemoteException {
        UpdatePlayersInLobbyMessage message = new UpdatePlayersInLobbyMessage(usernames);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void showUpdatePlayerStatus(boolean isConnected, String username) throws RemoteException {
        UpdatePlayerStatusMessage message = new UpdatePlayerStatusMessage(isConnected, username);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void showUpdateColor(PlayerColor color, String username) throws RemoteException {
        UpdateColorMessage message = new UpdateColorMessage(username, color);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void showUpdateObjectiveCard(ClientObjectiveCard chosenObjective, String username) throws RemoteException {
        UpdateObjectiveCardMessage message = new UpdateObjectiveCardMessage(chosenObjective, username);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void showUpdateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard, Side placedSide, Position position) throws RemoteException {
        UpdateAfterPlaceMessage message = new UpdateAfterPlaceMessage(positionToCornerCovered, newAvailablePositions, newResources, points, username, placedCard, placedSide, position);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void showUpdateAfterDraw(ClientCard drawnCard, ClientFace newTopDeck, ClientCard newFaceUpCard, String username, int boardPosition) throws RemoteException {
        UpdateAfterDrawMessage message = new UpdateAfterDrawMessage(drawnCard, newTopDeck, newFaceUpCard, username, boardPosition);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void showUpdateChat(Message message) throws RemoteException {
        UpdateChatMessage clientMessage = new UpdateChatMessage(message);
        String jsonMessage = gson.toJson(clientMessage);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void showUpdateCurrentPlayer(int currentPlayerIdx, GamePhase phase) throws RemoteException {
        UpdateCurrentPlayerMessage message = new UpdateCurrentPlayerMessage(currentPlayerIdx, phase);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void showUpdateSuspendedGame() throws RemoteException {
        UpdateSuspendedGameMessage message = new UpdateSuspendedGameMessage();
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void showWinners(List<String> winners) throws RemoteException {
        ShowWinnersMessage message = new ShowWinnersMessage(winners);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void reportError(String details) throws RemoteException {
        ReportErrorMessage message = new ReportErrorMessage(details);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void notifyStillActive() throws RemoteException {

    }

    @Override
    public void receivePing(String senderName) throws RemoteException {
        // respond
        ClientPingMessage pingMessage = new ClientPingMessage(senderName);
        String jsonMessage = gson.toJson(pingMessage);
        out.println(jsonMessage);
        out.flush();
        // update timer for new ping from the client
        timerForClientResponse.cancel();
        timerForClientResponse = new Timer();
        timerForClientResponse.schedule(new TimerTask() {
            @Override
            public void run() {
                terminate();
            }
        }, DELAY_FOR_CLIENTS_RESPONSE);
    }

    private void terminate() {
        try {
            input.close();
        } catch (IOException e) {
        }
        out.close();
        executor.close();
        // leave after being registered in the game
        if (username != null) {
            server.disconnect(username);
        }
    }
}
