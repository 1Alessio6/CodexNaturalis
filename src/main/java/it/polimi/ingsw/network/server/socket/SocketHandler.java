package it.polimi.ingsw.network.server.socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;
import it.polimi.ingsw.network.client.model.ClientGame;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;
import it.polimi.ingsw.network.client.socket.message.*;
import it.polimi.ingsw.network.heartbeat.HeartBeat;
import it.polimi.ingsw.network.heartbeat.HeartBeatHandler;
import it.polimi.ingsw.network.heartbeat.HeartBeatMessage;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.socket.message.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Sends the commands received by the Server Socket to the Client Socket.
 */
public class SocketHandler implements ClientHandler, HeartBeatHandler {
    private final Server server;
    private final PrintWriter out;
    private final BufferedReader input;
    private final Socket clientSocket;
    private final Gson gson;
    private String username;
    private HeartBeat heartBeat;
    private final ExecutorService notificationHandler;
    private final AtomicBoolean isActive;

    public SocketHandler(Server server, BufferedReader input, PrintWriter out, Socket clientSocket) {
        this.server = server;
        this.out = out;
        this.input = input;
        this.clientSocket = clientSocket;
        GsonBuilder builder = new GsonBuilder().enableComplexMapKeySerialization();
        this.gson = builder.create();
        notificationHandler = Executors.newSingleThreadExecutor();
        isActive = new AtomicBoolean(true);
    }

    @Override
    public void terminate() {
        isActive.set(false);
        closeResources();
    }

    @Override
    public void registerPingFromClient(HeartBeatMessage ping) {
        heartBeat.registerMessage(ping);
    }

    @Override
    public void startHeartBeat() {
        heartBeat.startHeartBeat();
    }

    public void run() {
        System.out.println("ClientHandler has started");
        try {
            String line = input.readLine();
            while (line != null) {
                System.out.println("Received from the client: " + line);
                NetworkMessage message;
                try {
                    message = gson.fromJson(line, NetworkMessage.class);
                } catch (Exception e) {
                    System.err.println("Received malformed input: stop listening the channel");
                    break;
                }
                Type type = message.getNetworkType();
                String sender = message.getSender();

                switch (type) {
                    case CONNECT:
                        username = sender;
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
                        break;

                    case HEARTBEAT:
                        HeartBeatMessage ping = gson.fromJson(line, HeartBeatMessage.class);
                        server.receivePing(ping);
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
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                UpdateCreatorMessage message = new UpdateCreatorMessage();
                String jsonMessage = gson.toJson(message);
                out.println(jsonMessage);
                out.flush();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAfterLobbyCrash() {
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                UpdateAfterLobbyCrashMessage message = new UpdateAfterLobbyCrashMessage();
                String jsonMessage = gson.toJson(message);
                out.println(jsonMessage);
                out.flush();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAfterConnection(ClientGame clientGame) {
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                UpdateAfterConnectionMessage message = new UpdateAfterConnectionMessage(clientGame);
                String jsonMessage = gson.toJson(message);
                System.out.println("Message to send " + jsonMessage);
                out.println(jsonMessage);
                out.flush();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdatePlayersInLobby(List<String> usernames) {
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                UpdatePlayersInLobbyMessage message = new UpdatePlayersInLobbyMessage(usernames);
                String jsonMessage = gson.toJson(message);
                out.println(jsonMessage);
                out.flush();
            }
        });
    }

    @Override
    public void showUpdateExceedingPlayer() {
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                ExceedingPlayerMessage message = new ExceedingPlayerMessage();
                String jsonMessage = gson.toJson(message);
                out.println(jsonMessage);
                out.flush();
            }
        });
    }

    @Override
    public void showUpdatePlayerStatus(boolean isConnected, String username) {
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                UpdatePlayerStatusMessage message = new UpdatePlayerStatusMessage(isConnected, username);
                String jsonMessage = gson.toJson(message);
                out.println(jsonMessage);
                out.flush();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateColor(PlayerColor color, String username) {
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                UpdateColorMessage message = new UpdateColorMessage(username, color);
                String jsonMessage = gson.toJson(message);
                out.println(jsonMessage);
                out.flush();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateObjectiveCard(ClientObjectiveCard chosenObjective, String username) {
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                UpdateObjectiveCardMessage message = new UpdateObjectiveCardMessage(chosenObjective, username);
                String jsonMessage = gson.toJson(message);
                out.println(jsonMessage);
                out.flush();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard, Side placedSide, Position position) {
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                UpdateAfterPlaceMessage message = new UpdateAfterPlaceMessage(positionToCornerCovered, newAvailablePositions, newResources, points, username, placedCard, placedSide, position);
                String jsonMessage = gson.toJson(message);
                out.println(jsonMessage);
                out.flush();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateAfterDraw(ClientCard drawnCard, ClientFace newTopDeck, ClientCard newFaceUpCard, String username, int boardPosition) {
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                UpdateAfterDrawMessage message = new UpdateAfterDrawMessage(drawnCard, newTopDeck, newFaceUpCard, username, boardPosition);
                String jsonMessage = gson.toJson(message);
                out.println(jsonMessage);
                out.flush();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateChat(Message message) {
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                UpdateChatMessage clientMessage = new UpdateChatMessage(message);
                String jsonMessage = gson.toJson(clientMessage);
                out.println(jsonMessage);
                out.flush();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateCurrentPlayer(int currentPlayerIdx, GamePhase phase) {
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                UpdateCurrentPlayerMessage message = new UpdateCurrentPlayerMessage(currentPlayerIdx, phase);
                String jsonMessage = gson.toJson(message);
                out.println(jsonMessage);
                out.flush();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateGameState() {
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                UpdateSuspendedGameMessage message = new UpdateSuspendedGameMessage();
                String jsonMessage = gson.toJson(message);
                out.println(jsonMessage);
                out.flush();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showWinners(List<String> winners) {
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                ShowWinnersMessage message = new ShowWinnersMessage(winners);
                String jsonMessage = gson.toJson(message);
                out.println(jsonMessage);
                out.flush();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportError(String details) {
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                ReportErrorMessage message = new ReportErrorMessage(details);
                String jsonMessage = gson.toJson(message);
                out.println(jsonMessage);
                out.flush();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resultOfLogin(boolean accepted, String details) {
        if (accepted && heartBeat == null) {
            assert username != null;
            heartBeat = new HeartBeat(this, username+"_handler", this, username);
        }
        notificationHandler.submit(() -> {
            if (isActive.get()) {
                ResultOfLogin message = new ResultOfLogin(accepted, username, details);
                String jsonMessage = gson.toJson(message);
                out.println(jsonMessage);
                out.flush();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receivePing(HeartBeatMessage ping) {
        if (isActive.get()) {
            String jsonMessage = gson.toJson(ping);
            out.println(jsonMessage);
            out.flush();
        }
    }

    /**
     * Closes all resources for this handler.
     */
    private void closeResources() {
        try {
            isActive.set(false);
            notificationHandler.shutdownNow();
            heartBeat.terminate();
            clientSocket.close();
            input.close();
            out.close();
        } catch (IOException e) {
            System.out.println("input has already been closed");
        }
    }

    @Override
    public void handleUnresponsiveness(String unresponsiveListener) {
        server.handleUnresponsiveness(unresponsiveListener);
    }
}
