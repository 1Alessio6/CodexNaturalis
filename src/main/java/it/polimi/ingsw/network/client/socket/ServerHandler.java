package it.polimi.ingsw.network.client.socket;

import com.google.gson.Gson;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.socket.message.*;
import it.polimi.ingsw.network.server.socket.message.ConnectMessage;
import it.polimi.ingsw.network.server.socket.message.PingMessage;
import it.polimi.ingsw.network.server.socket.message.PlaceStarterMessage;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.network.server.socket.message.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerHandler implements VirtualServer {
    private BufferedReader in;
    private PrintWriter out;
    private ClientSocket clientSocket;
    private Gson gson;
    private ExecutorService executorService;

    public ServerHandler(ClientSocket clientSocket, BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
        this.clientSocket = clientSocket;
        gson = new Gson();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void hear() {
        try {
            String line = in.readLine();
            while (line != null) {
                System.out.println("To send: " + line);
                ClientMessage mesage = gson.fromJson(line, ClientMessage.class);
                ClientType type = mesage.getType();

                switch (type) {
                    case ClientType.UPDATE_CREATOR:
                        executorService.submit(() -> clientSocket.updateCreator());
                        break;
                    case ClientType.UPDATE_AFTER_LOBBY_CRASH:
                        executorService.submit(() -> clientSocket.updateAfterLobbyCrash());
                        break;
                    case ClientType.UPDATE_AFTER_CONNECTION:
                        UpdateAfterConnectionMessage updateAfterConnectionMessage = gson.fromJson(line, UpdateAfterConnectionMessage.class);
                        executorService.submit(() ->
                                clientSocket.updateAfterConnection(updateAfterConnectionMessage.getGame())
                        );
                        break;
                    case ClientType.SHOW_UPDATE_PLAYERS_IN_LOBBY:
                        UpdatePlayersInLobbyMessage updatePlayersInLobbyMessage = gson.fromJson(line, UpdatePlayersInLobbyMessage.class);
                        executorService.submit(() ->
                                clientSocket.showUpdatePlayersInLobby(updatePlayersInLobbyMessage.getUsernames())
                        );
                        break;
                    case ClientType.SHOW_UPDATE_PLAYER_STATUS:
                        UpdatePlayerStatusMessage updatePlayerStatusMessage = gson.fromJson(line, UpdatePlayerStatusMessage.class);
                        executorService.submit(() ->
                                clientSocket.showUpdatePlayerStatus(updatePlayerStatusMessage.isConnected(), updatePlayerStatusMessage.getUsername())
                        );
                        break;
                    case ClientType.SHOW_UPDATE_COLOR:
                        UpdateColorMessage updateColorMessage = gson.fromJson(line, UpdateColorMessage.class);
                        executorService.submit(() ->
                                clientSocket.showUpdateColor(updateColorMessage.getColorSelected(), updateColorMessage.getUsername())
                        );
                        break;
                    case ClientType.SHOW_UPDATE_OBJECTIVE_CARD:
                        UpdateObjectiveCardMessage objectiveCardMessage = gson.fromJson(line, UpdateObjectiveCardMessage.class);
                        executorService.submit(() ->
                                clientSocket.showUpdateObjectiveCard(objectiveCardMessage.getChosenObjective(), objectiveCardMessage.getUsername())
                        );
                        break;
                    case ClientType.SHOW_UPDATE_AFTER_PLACE:
                        UpdateAfterPlaceMessage updateAfterPlaceMessage = gson.fromJson(line, UpdateAfterPlaceMessage.class);
                        executorService.submit(() ->
                                clientSocket.showUpdateAfterPlace(
                                        updateAfterPlaceMessage.getPositionToCornerCovered(),
                                        updateAfterPlaceMessage.getNewAvailablePositions(),
                                        updateAfterPlaceMessage.getNewResources(),
                                        updateAfterPlaceMessage.getPoints(),
                                        updateAfterPlaceMessage.getUsername(),
                                        updateAfterPlaceMessage.getPlacedCard(),
                                        updateAfterPlaceMessage.getPlacedSide(),
                                        updateAfterPlaceMessage.getPosition()
                                )
                        );
                        break;
                    case ClientType.SHOW_UPDATE_AFTER_DRAW:
                        UpdateAfterDrawMessage updateAfterDrawMessage = gson.fromJson(line, UpdateAfterDrawMessage.class);
                        executorService.submit(() ->
                                clientSocket.showUpdateAfterDraw(
                                        updateAfterDrawMessage.getDrawnCard(),
                                        updateAfterDrawMessage.getNewTopDeck(),
                                        updateAfterDrawMessage.getNewFaceUpCard(),
                                        updateAfterDrawMessage.getUsername(),
                                        updateAfterDrawMessage.getBoardPosition())
                        );
                        break;
                    case ClientType.SHOW_UPDATE_CHAT:
                        UpdateChatMessage updateChatMessage = gson.fromJson(line, UpdateChatMessage.class);
                        executorService.submit(() ->
                                clientSocket.showUpdateChat(updateChatMessage.getMessage())
                        );
                        break;
                    case ClientType.SHOW_UPDATE_CURRENT_PLAYER:
                        UpdateCurrentPlayerMessage updateCurrentPlayerMessage = gson.fromJson(line, UpdateCurrentPlayerMessage.class);
                        executorService.submit(() ->
                                clientSocket.showUpdateCurrentPlayer(updateCurrentPlayerMessage.getCurrentPlayerIdx(), updateCurrentPlayerMessage.getCurrentPhase())
                        );
                        break;
                    case ClientType.SHOW_UPDATE_SUSPENDED_GAME:
                        executorService.submit(() ->
                                clientSocket.showUpdateSuspendedGame()
                        );
                        break;
                    case ClientType.SHOW_WINNERS:
                        ShowWinnersMessage showWinnersMessage = gson.fromJson(line, ShowWinnersMessage.class);
                        executorService.submit(() ->
                                clientSocket.showWinners(showWinnersMessage.getWinners())
                        );
                        break;
                    case ClientType.ERROR:
                        ReportErrorMessage errorMessage = gson.fromJson(line, ReportErrorMessage.class);
                        executorService.submit(() ->
                                clientSocket.reportError(errorMessage.getDetails())
                        );
                        break;
                    case ClientType.SEND_PING:
                        ClientPingMessage pingMessage = gson.fromJson(line, ClientPingMessage.class);
                        clientSocket.notifyStillActive(pingMessage.getUsername());
                        break;
                }
                line = in.readLine();
            }
        } catch (IOException e) {
        }
    }

    @Override
    public void connect(VirtualView client, String username) {
        ConnectMessage message = new ConnectMessage(username);
        String json = gson.toJson(message);
        out.println(json);
        out.flush();
    }

    @Override
    public void placeStarter(String username, Side side) {
        PlaceStarterMessage message = new PlaceStarterMessage(username, side);
        String json = gson.toJson(message);
        out.println(json);
        out.flush();
    }

    @Override
    public void chooseColor(String username, PlayerColor color) {
        ChooseColorMessage message = new ChooseColorMessage(username, color);
        String json = gson.toJson(message);
        out.println(json);
        out.flush();
    }

    @Override
    public void placeObjectiveCard(String username, int chosenObjective) {
        PlaceObjectiveMessage message = new PlaceObjectiveMessage(username, chosenObjective);
        String json = gson.toJson(message);
        out.println(json);
        out.flush();
    }

    @Override
    public void placeCard(String username, int frontId, int backId, Side side, Position position) {
        PlaceCardMessage message = new PlaceCardMessage(username, frontId, backId, side, position);
        String json = gson.toJson(message);
        out.println(json);
        out.flush();
    }

    @Override
    public void draw(String username, int idToDraw) {
        DrawMessage message = new DrawMessage(username, idToDraw);
        String json = gson.toJson(message);
        out.println(json);
        out.flush();
    }

    @Override
    public void sendMessage(Message chatMessage) {
        SendChatMessage message = new SendChatMessage(chatMessage.getSender(), chatMessage);
        String json = gson.toJson(message);
        out.println(json);
        out.flush();
    }

    @Override
    public void setPlayersNumber(String username, int playersNumber) {
        SetPlayerNumberMessage message = new SetPlayerNumberMessage(username, playersNumber);
        String json = gson.toJson(message);
        out.println(json);
        out.flush();
    }

    @Override
    public void disconnect(String username) {
        DisconnectMessage message = new DisconnectMessage(username);
        String json = gson.toJson(message);
        out.println(json);
        out.flush();
        try {
            in.close();
        } catch (IOException e) {
        }
        out.close();
    }

    @Override
    public void receivePing(String username) {
        PingMessage message = new PingMessage(username);
        String json = gson.toJson(message);
        out.println(json);
        out.flush();
    }
}
