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
import it.polimi.ingsw.network.client.controller.ClientController;
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

public class ClientRMI extends Client implements VirtualView{
    private HeartBeat heartBeat;
    private String name;

    public ClientRMI(String host, Integer port) throws UnReachableServerException {
        super(host, port);
    }

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

    @Override
    public void runView() throws RemoteException {
        // register stub
        VirtualView stub =
                (VirtualView) UnicastRemoteObject.exportObject(this, 0);
        ClientController controller = clientView.run(stub);
        name = controller.getMainPlayerUsername();
        clientView.beginCommandAcquisition();
        heartBeat = new HeartBeat(this, name, server, "server");
        heartBeat.startHeartBeat();
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
        if(controller.getMainPlayerUsername().equals(username)){
            clientView.showUpdateObjectiveCard();
        }
    }

    @Override
    public void showUpdateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard, Side placedSide, Position position) throws RemoteException {
        controller.updateAfterPlace(positionToCornerCovered, newAvailablePositions, newResources, points, username, placedCard, placedSide, position);

        if (controller.getGamePhase().equals(GamePhase.Setup)){
            clientView.showStarterPlacement(username);
        } else {
            clientView.showUpdateAfterPlace();
        }
    }

    @Override
    public void showUpdateAfterDraw(ClientCard drawnCard, ClientFace newTopDeck, ClientCard newFaceUpCard, String username, int boardPosition) throws RemoteException {
        controller.updateAfterDraw(drawnCard,newTopDeck,newFaceUpCard,username,boardPosition);
        clientView.showUpdateAfterDraw();
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
        controller.updateCurrentPlayer(currentPlayerIdx,phase);
        clientView.showUpdateCurrentPlayer();
    }

    @Override
    public void showUpdateSuspendedGame() throws RemoteException {
        controller.updateSuspendedGame();
        clientView.showUpdateSuspendedGame();
    }

    @Override
    public void showWinners(List<String> winners) throws RemoteException {
        clientView.showWinners();
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
