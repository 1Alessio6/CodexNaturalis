package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.model.*;
import it.polimi.ingsw.network.client.model.card.*;
import it.polimi.ingsw.network.client.view.View;
import it.polimi.ingsw.network.client.view.tui.ClientTUI;
import it.polimi.ingsw.network.server.rmi.ServerRMI;


import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ClientRMI extends UnicastRemoteObject implements VirtualView {

    private final ClientController controller;

    private View clientView; //can be tui or gui

    public ClientRMI() throws RemoteException{
        controller = new ClientController(new ServerRMI());
        clientView = new ClientTUI();
    }

    public ClientRMI(VirtualServer server, View clientView) throws RemoteException {
        controller = new ClientController(server);
        //check which view
        //clientView = new view(controller)
    }

    private void run(String host, int port) throws RemoteException, NotBoundException {
        String serverName = "ServerRmi";
        Registry registry = LocateRegistry.getRegistry(host, port); //todo change port and args and decide how to handle the exception

        VirtualServer server = (VirtualServer) registry.lookup(serverName);

        clientView.run(this);
        //runView (view has to select connect)
    }

    private String receiveUsername(){
        System.out.println("Insert username");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.next();
        scanner.close();

        return username;
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
        clientView.showUpdateColor();
    }

    @Override
    public void showUpdateObjectiveCard(ClientCard chosenObjective, String username) {
        controller.updateObjectiveCard(chosenObjective, username);
        //todo: if the objective card isn't of the main player the view should not show the card
        if(controller.getMainPlayerUsername().equals(username)){
            clientView.showUpdateObjectiveCard();
        }
    }

    @Override
    public void showUpdateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard, Side placedSide, Position position) throws RemoteException {
        controller.updateAfterPlace(positionToCornerCovered, newAvailablePositions, newResources, points, username, placedCard, placedSide, position);
        clientView.showUpdateAfterPlace();
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

}
