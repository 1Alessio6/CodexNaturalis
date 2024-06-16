package it.polimi.ingsw.network.client.rmi;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.UnReachableServerException;
import it.polimi.ingsw.network.client.model.*;
import it.polimi.ingsw.network.client.model.card.*;
import it.polimi.ingsw.network.heartbeat.HeartBeat;
import it.polimi.ingsw.network.heartbeat.HeartBeatMessage;
import it.polimi.ingsw.network.server.rmi.ServerRMI;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

/**
 * The ClientRMI updates the view content and the information present in the ClientController when RMI communication is
 * used.
 */
public class ClientRMI extends Client {
    private HeartBeat heartBeat;

    /**
     * Constructs the ClientRMI using the <code>host</code> and the <code>port</code> provided.
     *
     * @param host the name of the host to connect to.
     * @param port the port number.
     * @throws UnReachableServerException if the server isn't reachable.
     */
    public ClientRMI(String host, Integer port) throws UnReachableServerException {
        super(host, port);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void connect(String ip, Integer port) throws UnReachableServerException {
        String serverName = ServerRMI.getServerName();
        try {
            Registry registry = LocateRegistry.getRegistry(ip, port);
            server = (VirtualServer) registry.lookup(serverName);
        } catch (RemoteException | NotBoundException e) {
            throw new UnReachableServerException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runView() throws RemoteException {
        // register stub
        VirtualView stub =
                (VirtualView) UnicastRemoteObject.exportObject(this, 0);
        heartBeat = new HeartBeat(this, name, server, "server");
        clientView.runView(stub);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(String name) {
        this.name = name;
        heartBeat.setHandlerName(name);
        heartBeat.startHeartBeat();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCreator() throws RemoteException {
        clientView.showUpdateCreator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAfterLobbyCrash() throws RemoteException {
        clientView.showUpdateAfterLobbyCrash();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAfterConnection(ClientGame clientGame) {
        controller.updateAfterConnection(clientGame);
        clientView.showUpdateAfterConnection();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdatePlayersInLobby(List<String> usernames) throws RemoteException {
        controller.updatePlayersInLobby(usernames);
        clientView.showUpdatePlayersInLobby();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdatePlayerStatus(boolean isConnected, String username) throws RemoteException {
        controller.updatePlayerStatus(isConnected, username);
        clientView.showUpdatePlayerStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateColor(PlayerColor color, String username) throws RemoteException {
        controller.updateColor(color, username);
        clientView.showUpdateColor(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateObjectiveCard(ClientObjectiveCard chosenObjective, String username) {
        controller.updateObjectiveCard(chosenObjective, username);
        //todo: if the objective card isn't of the main player the view should not show the card
        if(controller.getMainPlayerUsername().equals(username)){
            clientView.showUpdateObjectiveCard();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard, Side placedSide, Position position) throws RemoteException {
        controller.updateAfterPlace(positionToCornerCovered, newAvailablePositions, newResources, points, username, placedCard, placedSide, position);

        if (controller.getGamePhase().equals(GamePhase.Setup)){
            clientView.showStarterPlacement(username);
        } else {
            clientView.showUpdateAfterPlace(username);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateAfterDraw(ClientCard drawnCard, ClientFace newTopDeck, ClientCard newFaceUpCard, String username, int boardPosition) throws RemoteException {
        controller.updateAfterDraw(drawnCard,newTopDeck,newFaceUpCard,username,boardPosition);
        clientView.showUpdateAfterDraw();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateChat(Message message) throws RemoteException {
        controller.updateChat(message);
        clientView.showUpdateChat();
    }

    //@Override
    //public void showUpdateTriggeredEndGame(String username) {

    //}

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateCurrentPlayer(int currentPlayerIdx, GamePhase phase) throws RemoteException {
        controller.updateCurrentPlayer(currentPlayerIdx,phase);
        clientView.showUpdateCurrentPlayer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateGameState() throws RemoteException {
        controller.updateSuspendedGame();
        clientView.showUpdateSuspendedGame();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showWinners(List<String> winners) throws RemoteException {
        clientView.showWinners(winners);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportError(String details) throws RemoteException {
        System.err.println(details);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleUnresponsiveness(String unresponsiveListener) {
        clientView.showServerCrash();
        System.exit(1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receivePing(HeartBeatMessage ping) throws RemoteException {
        heartBeat.registerMessage(ping);
    }

}
