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

public class ClientRMI extends Client {
    private HeartBeat heartBeat;
    private VirtualView stub;

    public ClientRMI() {
    }

    @Override
    public VirtualServer bindServer(String ip, Integer port) throws UnReachableServerException {
        String serverName = ServerRMI.getServerName();
        try {
            Registry registry = LocateRegistry.getRegistry(ip, port);
            VirtualServer server = (VirtualServer) registry.lookup(serverName);
            heartBeat = new HeartBeat(this, name, server, "server");
            return server;
        } catch (RemoteException | NotBoundException e) {
            throw new UnReachableServerException(e.getMessage());
        }
    }

    @Override
    public VirtualView getInstanceForTheServer() throws RemoteException {
        if (stub == null) {
            stub = (VirtualView) UnicastRemoteObject.exportObject(this, 0);
        }
        return stub;
    }

    @Override
    public void runView() throws RemoteException {
        // register stub
        clientView.runView();
    }

    @Override
    public void resultOfLogin(boolean accepted, String username) throws RemoteException {
        if (accepted) {
            controller.setMainPlayerUsername(username);
            heartBeat.setHandlerName(username);
            heartBeat.startHeartBeat();
        } else {
            clientView.showInvalidLogin();
        }
    }

    @Override
    public void updateCreator() throws RemoteException {
        clientView.showUpdateCreator();
    }

    @Override
    public void updateAfterLobbyCrash() throws RemoteException {
        clientView.showUpdateAfterLobbyCrash();
    }

    @Override
    public void updateAfterConnection(ClientGame clientGame) {
        controller.updateAfterConnection(clientGame);
        clientView.showUpdateAfterConnection();
    }

    @Override
    public void showUpdatePlayersInLobby(List<String> usernames) throws RemoteException {
        controller.updatePlayersInLobby(usernames);
        clientView.showUpdatePlayersInLobby();
    }

    @Override
    public void showUpdatePlayerStatus(boolean isConnected, String username) throws RemoteException {
        controller.updatePlayerStatus(isConnected, username);
        clientView.showUpdatePlayerStatus();
    }

    @Override
    public void showUpdateColor(PlayerColor color, String username) throws RemoteException {
        controller.updateColor(color, username);
        clientView.showUpdateColor(username);
    }

    @Override
    public void showUpdateObjectiveCard(ClientObjectiveCard chosenObjective, String username) {
        controller.updateObjectiveCard(chosenObjective, username);
        //todo: if the objective card isn't of the main player the view should not show the card
        if (controller.getMainPlayerUsername().equals(username)) {
            clientView.showUpdateObjectiveCard();
        }
    }

    @Override
    public void showUpdateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard, Side placedSide, Position position) throws RemoteException {
        controller.updateAfterPlace(positionToCornerCovered, newAvailablePositions, newResources, points, username, placedCard, placedSide, position);

        if (controller.getGamePhase().equals(GamePhase.Setup)) {
            clientView.showStarterPlacement(username);
        } else {
            clientView.showUpdateAfterPlace(username);
        }
    }

    @Override
    public void showUpdateAfterDraw(ClientCard drawnCard, ClientFace newTopDeck, ClientCard newFaceUpCard, String username, int boardPosition) throws RemoteException {
        controller.updateAfterDraw(drawnCard, newTopDeck, newFaceUpCard, username, boardPosition);
        clientView.showUpdateAfterDraw(username);
    }

    @Override
    public void showUpdateChat(Message message) throws RemoteException {
        controller.updateChat(message);
        clientView.showUpdateChat();
    }

    //@Override
    //public void showUpdateTriggeredEndGame(String username) {

    //}

    @Override
    public void showUpdateCurrentPlayer(int currentPlayerIdx, GamePhase phase) throws RemoteException {
        controller.updateCurrentPlayer(currentPlayerIdx, phase);
        clientView.showUpdateCurrentPlayer();
    }

    @Override
    public void showUpdateGameState() throws RemoteException {
        controller.updateSuspendedGame();
        clientView.showUpdateSuspendedGame();
    }

    @Override
    public void showWinners(List<String> winners) throws RemoteException {
        clientView.showWinners(winners);
    }

    @Override
    public void reportError(String details) throws RemoteException {
        System.err.println(details);
    }

    @Override
    public void handleUnresponsiveness(String unresponsiveListener) {
        clientView.showServerCrash();
        System.exit(1);
    }

    @Override
    public void receivePing(HeartBeatMessage ping) throws RemoteException {
        heartBeat.registerMessage(ping);
    }

}
