package it.polimi.ingsw.network.client.view;

import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.controller.ClientActions;
import it.polimi.ingsw.network.client.controller.ClientController;

import java.rmi.RemoteException;
import java.util.List;


public abstract class View {

    private ClientController controller;

    //method used to read client input
    public abstract void run(VirtualView client);

    //method used to update after a controller update, invoked by the client class

    public abstract void showUpdatePlayersInLobby();

    public abstract void showUpdateCreator();

    public abstract void showUpdateAfterLobbyCrash();

    public abstract void showUpdateAfterConnection();

    public abstract void showUpdatePlayerStatus();

    public abstract void showInitialPlayerStatus();

    public abstract void showBoardSetUp();

    public abstract void showStarterPlacement();

    public abstract void showUpdateColor();

    public abstract void showUpdateObjectiveCard();

    public abstract void showUpdateAfterPlace();

    public abstract void showUpdateAfterDraw();

    public abstract void showUpdateChat();

    public abstract void showUpdateCurrentPlayer();

    public abstract void showUpdateSuspendedGame();

    public abstract void showWinners();

}
