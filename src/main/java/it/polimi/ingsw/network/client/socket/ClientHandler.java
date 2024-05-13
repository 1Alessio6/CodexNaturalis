package it.polimi.ingsw.network.client.socket;

import com.google.gson.Gson;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.CornerPosition;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.model.ClientGame;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import it.polimi.ingsw.network.client.socket.message.*;
import it.polimi.ingsw.network.server.socket.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class ClientHandler implements VirtualView {

    private final Server server;
    private final PrintWriter out;
    private final BufferedReader input;

    private final Gson gson;

    public ClientHandler(Server server, BufferedReader input, PrintWriter out) {
        this.server = server;
        this.out = out;
        this.input = input;
        gson = new Gson();
    }

    public void run() throws IOException {

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
        UpdatePlayerStatusMessage message = new UpdatePlayerStatusMessage(isConnected,username);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void showUpdateColor(PlayerColor color, String username) throws RemoteException {
        UpdateColorMessage message = new UpdateColorMessage(username,color);
        String jsonMessage = gson.toJson(message);
        out.println(jsonMessage);
        out.flush();
    }

    @Override
    public void showUpdateObjectiveCard(ClientCard chosenObjective, String username) throws RemoteException {
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


}
