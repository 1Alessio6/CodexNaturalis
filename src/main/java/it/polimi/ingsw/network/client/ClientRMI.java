package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.model.lobby.FullLobbyException;
import it.polimi.ingsw.model.lobby.InvalidUsernameException;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.model.*;
import it.polimi.ingsw.network.client.model.card.*;
import it.polimi.ingsw.network.client.model.player.*;
import it.polimi.ingsw.network.client.view.View;


import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ClientRMI extends UnicastRemoteObject implements VirtualView {

    private ClientController controller;

    private View clientView; //can be tui or gui

    protected ClientRMI(VirtualServer server) throws RemoteException {
        controller = new ClientController(server);
        //check which view
        //clientView = new view(controller)
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        String serverName = "ServerRmi";
        Registry registry = LocateRegistry.getRegistry(args[0], 1234); //todo change port and args and decide how to handle the exception

        VirtualServer server = (VirtualServer) registry.lookup(serverName);

        new ClientRMI(server).run();
    }


    //this method assume the client tries to connect to the server till the server is available
    private void run() {
        connect();
        //run.view
    }

    //todo check if ClientPlayground it's correctly updated, it should be updated by the methods from observer pattern
    //todo the server needs to send the map of corner

    private void connect(){
        while(true) {
            try {
                String username = receiveUsername();
                this.server.connect(this, username);
                break;
            }catch(InvalidUsernameException e){
                System.err.println("Username selected is already taken. Please try again");
            } catch (RemoteException e) {
                System.err.println("Connection Error");
            } catch (FullLobbyException e){
                System.err.println("Error");
            }
        }
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

    }

    @Override
    public void showUpdatePlayersInLobby(List<String> usernames) throws RemoteException {

    }

  //  @Override
  //  public void showUpdateJoinedPlayers(List<String> usernames) throws RemoteException {

  //  }

    @Override
    public void showUpdatePlayerStatus(boolean isConnected, String username) throws RemoteException {
        /*
        game.getMainPlayer().setNetworkStatus(isConnected);
        System.out.println(game.getMainPlayer().isConnected());

         */
    }

    //@Override
    //public void showStarterPlacement(String username, int faceId) {

    //}

    @Override
    public void showUpdateColor(PlayerColor color, String username) throws RemoteException {

    }

    //delete
    @Override
    public void showUpdateObjectiveCard(ClientCard chosenObjective, String username) {

    }

    @Override
    public void showUpdateAfterPlace(Map<Position, CornerPosition> positionToCornerCovered, List<Position> newAvailablePositions, Map<Symbol, Integer> newResources, int points, String username, ClientCard placedCard, Side placedSide, Position position) throws RemoteException {

    }

    @Override
    public void showUpdateAfterDraw(ClientCard drawnCard, ClientCard newTopDeck, ClientCard newFaceUpCard, String username, int boardPosition) throws RemoteException {

    }

    @Override
    public void showUpdateChat(Message message) throws RemoteException {
        System.out.println(message.getContent());
    }

    //@Override
    //public void showUpdateTriggeredEndGame(String username) {

    //}

    @Override
    public void showUpdateCurrentPlayer(int currentPlayerIdx, GamePhase phase) throws RemoteException {

    }

    @Override
    public void showUpdateSuspendedGame() throws RemoteException {

    }

    @Override
    public void showWinners(List<String> winners) throws RemoteException {

    }

    @Override
    public void reportError(String details) throws RemoteException {
        System.err.println(details);
    }

}
