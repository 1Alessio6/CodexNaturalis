package it.polimi.ingsw.network.server.rmi;

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
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;
import it.polimi.ingsw.network.heartbeat.HeartBeat;
import it.polimi.ingsw.network.heartbeat.HeartBeatHandler;
import it.polimi.ingsw.network.heartbeat.HeartBeatMessage;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class ClientHandlerRMI implements VirtualView, HeartBeatHandler {
    private VirtualView stub;
    private String username;
    private HeartBeat heartBeat;
    private ServerRMI server;

    public ClientHandlerRMI(ServerRMI server, VirtualView clientRMI, String username) {
        this.server = server;
        this.stub = clientRMI;
        this.username = username;
        this.heartBeat = new HeartBeat(this, "server", this, username);
    }

    public void startHeartBeat() {
        heartBeat.startHeartBeat();
    }

    @Override
    public void updateCreator() throws RemoteException {
        stub.updateCreator();
    }

    @Override
    public void updateAfterConnection(ClientGame clientGame) throws RemoteException {
        stub.updateAfterConnection(clientGame);
    }

    @Override
    public void showUpdatePlayersInLobby(List<String> usernames) throws RemoteException {
        stub.showUpdatePlayersInLobby(usernames);
    }

    public void terminate() {
        heartBeat.terminate();
    }

    private void removeMeFromTheListOfActiveClients() {
        System.err.println("user " + username + " left the server");
        heartBeat.terminate();
        server.remove(username);
    }

    @Override
    public void showUpdateExceedingPlayer() throws RemoteException {
        stub.showUpdateExceedingPlayer();
        removeMeFromTheListOfActiveClients();
    }

    @Override
    public void updateAfterLobbyCrash() throws RemoteException {
        stub.updateAfterLobbyCrash();
        removeMeFromTheListOfActiveClients();
    }

    @Override
    public void showUpdateFullLobby() throws RemoteException {
        stub.showUpdateFullLobby();
        heartBeat.terminate();
    }

    @Override
    public void showUpdatePlayerStatus(boolean isConnected, String username) throws RemoteException {
        stub.showUpdatePlayerStatus(isConnected, username);
    }

    @Override
    public void showUpdateColor(PlayerColor color, String username) throws RemoteException {
        stub.showUpdateColor(color, username);
    }

    @Override
    public void showUpdateObjectiveCard(ClientObjectiveCard chosenObjective, String username) throws RemoteException {
        stub.showUpdateObjectiveCard(chosenObjective, username);
    }

    @Override
    public void showUpdateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard, Side placedSide, Position position) throws RemoteException {
        stub.showUpdateAfterPlace(positionToCornerCovered, newAvailablePositions, newResources, points, username, placedCard, placedSide, position);
    }

    @Override
    public void showUpdateAfterDraw(ClientCard drawnCard, ClientFace newTopDeck, ClientCard newFaceUpCard, String username, int boardPosition) throws RemoteException {
        stub.showUpdateAfterDraw(drawnCard, newTopDeck, newFaceUpCard, username, boardPosition);
    }

    @Override
    public void showUpdateChat(Message message) throws RemoteException {
        stub.showUpdateChat(message);
    }

    @Override
    public void showUpdateCurrentPlayer(int currentPlayerIdx, GamePhase phase) throws RemoteException {
        stub.showUpdateCurrentPlayer(currentPlayerIdx, phase);
    }

    @Override
    public void showUpdateGameState() throws RemoteException {
        stub.showUpdateGameState();
    }

    @Override
    public void showWinners(List<String> winners) throws RemoteException {
        stub.showWinners(winners);
    }

    @Override
    public void reportError(String details) throws RemoteException {
        stub.reportError(details);
    }

    @Override
    public void resultOfLogin(boolean accepted, String username, String details) throws RemoteException {
        stub.resultOfLogin(accepted, username, details);
    }

    @Override
    public void handleUnresponsiveness(String unresponsiveListener) {
        System.err.println("User " + unresponsiveListener + " is unresponsive");
        heartBeat.terminate();
        //server.remove(unresponsiveListener);
        server.handleUnresponsiveness(unresponsiveListener);
    }

    @Override
    public void receivePing(HeartBeatMessage ping) throws RemoteException {
        stub.receivePing(ping);
    }

    public void registerMessage(HeartBeatMessage ping) {
        heartBeat.registerMessage(ping);
    }
}
