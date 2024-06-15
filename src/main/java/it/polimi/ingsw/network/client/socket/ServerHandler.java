package it.polimi.ingsw.network.client.socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.socket.message.*;
import it.polimi.ingsw.network.heartbeat.HeartBeatMessage;
import it.polimi.ingsw.network.server.socket.message.ConnectMessage;
import it.polimi.ingsw.network.server.socket.message.PlaceStarterMessage;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.network.server.socket.message.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;

public class ServerHandler implements VirtualServer {
    private final BufferedReader in;
    private final PrintWriter out;
    private final ClientSocket clientSocket;
    private final Gson gson;

    public ServerHandler(ClientSocket clientSocket, BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
        this.clientSocket = clientSocket;
        GsonBuilder builder = new GsonBuilder().enableComplexMapKeySerialization();
        this.gson = builder.create();
    }

    public void hear() {
        System.out.println("I'm hearing");
        try {
            String line = in.readLine();
            while (line != null) {
                System.out.println("received from server: " + line);
                NetworkMessage message = gson.fromJson(line, NetworkMessage.class);
                Type type = message.getNetworkType();

                switch (type) {
                    case RESULT_OF_LOGIN:
                        ResultOfLogin resultOfLogin = gson.fromJson(line, ResultOfLogin.class);
                        clientSocket.resultOfLogin(resultOfLogin.getAccepted(), resultOfLogin.getSender());
                        break;
                    case UPDATE_CREATOR:
                        clientSocket.updateCreator();
                        break;
                    case UPDATE_AFTER_LOBBY_CRASH:
                        clientSocket.updateAfterLobbyCrash();
                        break;
                    case UPDATE_AFTER_CONNECTION:
                        UpdateAfterConnectionMessage updateAfterConnectionMessage = gson.fromJson(line, UpdateAfterConnectionMessage.class);
                        clientSocket.updateAfterConnection(updateAfterConnectionMessage.getGame());
                        break;
                    case SHOW_UPDATE_PLAYERS_IN_LOBBY:
                        UpdatePlayersInLobbyMessage updatePlayersInLobbyMessage = gson.fromJson(line, UpdatePlayersInLobbyMessage.class);
                        clientSocket.showUpdatePlayersInLobby(updatePlayersInLobbyMessage.getUsernames());
                        break;
                    case SHOW_UPDATE_PLAYER_STATUS:
                        UpdatePlayerStatusMessage updatePlayerStatusMessage = gson.fromJson(line, UpdatePlayerStatusMessage.class);
                        clientSocket.showUpdatePlayerStatus(updatePlayerStatusMessage.isConnected(), updatePlayerStatusMessage.getUsername());
                        break;
                    case SHOW_UPDATE_COLOR:
                        UpdateColorMessage updateColorMessage = gson.fromJson(line, UpdateColorMessage.class);
                        clientSocket.showUpdateColor(updateColorMessage.getColorSelected(), updateColorMessage.getUsername());
                        break;
                    case SHOW_UPDATE_OBJECTIVE_CARD:
                        UpdateObjectiveCardMessage objectiveCardMessage = gson.fromJson(line, UpdateObjectiveCardMessage.class);
                        clientSocket.showUpdateObjectiveCard(objectiveCardMessage.getChosenObjective(), objectiveCardMessage.getUsername());
                        break;
                    case SHOW_UPDATE_AFTER_PLACE:
                        UpdateAfterPlaceMessage updateAfterPlaceMessage = gson.fromJson(line, UpdateAfterPlaceMessage.class);
                        clientSocket.showUpdateAfterPlace(
                                updateAfterPlaceMessage.getPositionToCornerCovered(),
                                updateAfterPlaceMessage.getNewAvailablePositions(),
                                updateAfterPlaceMessage.getNewResources(),
                                updateAfterPlaceMessage.getPoints(),
                                updateAfterPlaceMessage.getUsername(),
                                updateAfterPlaceMessage.getPlacedCard(),
                                updateAfterPlaceMessage.getPlacedSide(),
                                updateAfterPlaceMessage.getPosition()
                        );
                        break;
                    case SHOW_UPDATE_AFTER_DRAW:
                        UpdateAfterDrawMessage updateAfterDrawMessage = gson.fromJson(line, UpdateAfterDrawMessage.class);
                        clientSocket.showUpdateAfterDraw(
                                updateAfterDrawMessage.getDrawnCard(),
                                updateAfterDrawMessage.getNewTopDeck(),
                                updateAfterDrawMessage.getNewFaceUpCard(),
                                updateAfterDrawMessage.getUsername(),
                                updateAfterDrawMessage.getBoardPosition());
                        break;
                    case SHOW_UPDATE_CHAT:
                        UpdateChatMessage updateChatMessage = gson.fromJson(line, UpdateChatMessage.class);
                        clientSocket.showUpdateChat(updateChatMessage.getMessage());
                        break;
                    case SHOW_UPDATE_CURRENT_PLAYER:
                        UpdateCurrentPlayerMessage updateCurrentPlayerMessage = gson.fromJson(line, UpdateCurrentPlayerMessage.class);
                        clientSocket.showUpdateCurrentPlayer(updateCurrentPlayerMessage.getCurrentPlayerIdx(), updateCurrentPlayerMessage.getCurrentPhase());
                        break;
                    case SHOW_UPDATE_SUSPENDED_GAME:
                        clientSocket.showUpdateGameState();
                        break;
                    case SHOW_WINNERS:
                        ShowWinnersMessage showWinnersMessage = gson.fromJson(line, ShowWinnersMessage.class);
                        clientSocket.showWinners(showWinnersMessage.getWinners());
                        break;
                    case ERROR:
                        ReportErrorMessage errorMessage = gson.fromJson(line, ReportErrorMessage.class);
                        clientSocket.reportError(errorMessage.getDetails());
                        break;
                    case HEARTBEAT:
                        //System.out.println("casting the message...");
                        HeartBeatMessage ping = gson.fromJson(line, HeartBeatMessage.class);
                        clientSocket.receivePing(ping);
                        break;
                    default:
                        System.err.println("Unrecognised input from the server");
                        assert(false);
                        break;
                }
                line = in.readLine();
            }
            System.out.println("Closed connection from server");
        } catch (IOException e) {
            System.err.println("Stop hearing: server is down");
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

    //@Override
    //public void handleUnresponsiveness(String unresponsiveListener) {
    //}

    @Override
    public void receivePing(HeartBeatMessage ping) throws RemoteException {
        //System.out.println("Sending ping...");
        String json = gson.toJson(ping);
        out.println(json);
        out.flush();
    }
}
