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

    /**
     * Constructs a <code>ClientHandlerRMI</code> with the <code>server</code>, <code>clientRMI</code> and the
     * <code>username</code> provided
     *
     * @param server    the representation of the server
     * @param clientRMI the representation of the client
     * @param username  the username of the player
     */
    public ClientHandlerRMI(ServerRMI server, VirtualView clientRMI, String username) {
        this.server = server;
        this.stub = clientRMI;
        this.username = username;
        this.heartBeat = new HeartBeat(this, "server", this, username);
    }

    /**
     * Starts the heartBeat
     */
    public void startHeartBeat() {
        heartBeat.startHeartBeat();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCreator() throws RemoteException {
        stub.updateCreator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAfterConnection(ClientGame clientGame) throws RemoteException {
        stub.updateAfterConnection(clientGame);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdatePlayersInLobby(List<String> usernames) throws RemoteException {
        stub.showUpdatePlayersInLobby(usernames);
    }

    /**
     * Terminates the execution of the heartBeat
     */
    public void terminate() {
        heartBeat.terminate();
    }

    private void removeMeFromTheListOfActiveClients() {
        System.err.println("user " + username + " left the server");
        heartBeat.terminate();
        server.remove(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateExceedingPlayer() throws RemoteException {
        stub.showUpdateExceedingPlayer();
        removeMeFromTheListOfActiveClients();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAfterLobbyCrash() throws RemoteException {
        stub.updateAfterLobbyCrash();
        removeMeFromTheListOfActiveClients();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateFullLobby() throws RemoteException {
        stub.showUpdateFullLobby();
        heartBeat.terminate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdatePlayerStatus(boolean isConnected, String username) throws RemoteException {
        stub.showUpdatePlayerStatus(isConnected, username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateColor(PlayerColor color, String username) throws RemoteException {
        stub.showUpdateColor(color, username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateObjectiveCard(ClientObjectiveCard chosenObjective, String username) throws RemoteException {
        stub.showUpdateObjectiveCard(chosenObjective, username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard, Side placedSide, Position position) throws RemoteException {
        stub.showUpdateAfterPlace(positionToCornerCovered, newAvailablePositions, newResources, points, username, placedCard, placedSide, position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateAfterDraw(ClientCard drawnCard, ClientFace newTopDeck, ClientCard newFaceUpCard, String username, int boardPosition) throws RemoteException {
        stub.showUpdateAfterDraw(drawnCard, newTopDeck, newFaceUpCard, username, boardPosition);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateChat(Message message) throws RemoteException {
        stub.showUpdateChat(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateCurrentPlayer(int currentPlayerIdx, GamePhase phase) throws RemoteException {
        stub.showUpdateCurrentPlayer(currentPlayerIdx, phase);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateGameState() throws RemoteException {
        stub.showUpdateGameState();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showWinners(List<String> winners) throws RemoteException {
        stub.showWinners(winners);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportError(String details) throws RemoteException {
        stub.reportError(details);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resultOfLogin(boolean accepted, String username, String details) throws RemoteException {
        stub.resultOfLogin(accepted, username, details);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleUnresponsiveness(String unresponsiveListener) {
        System.err.println("User " + unresponsiveListener + " is unresponsive");
        heartBeat.terminate();
        //server.remove(unresponsiveListener);
        server.handleUnresponsiveness(unresponsiveListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receivePing(HeartBeatMessage ping) throws RemoteException {
        stub.receivePing(ping);
    }

    /**
     * Registers the <code>ping</code>
     *
     * @param ping the message
     */
    public void registerMessage(HeartBeatMessage ping) {
        heartBeat.registerMessage(ping);
    }
}
