package it.polimi.ingsw.network.client.view;

import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.controller.ClientController;


/**
 * Methods do not need any argument, as information are retrieved directly from the
 * ClientGame after being updated
 */
public interface View {

    //method used to read client input
    public ClientController run(VirtualView client);

    //method used to update after a controller update, invoked by the client class
    public void showServerCrash();

    public void showUpdatePlayersInLobby();

    public void showUpdateCreator();

    public void showUpdateAfterLobbyCrash();

    public void showUpdateAfterConnection();

    public void showUpdatePlayerStatus();

    public void showInitialPlayerStatus();

    public void showBoardSetUp();

    public void showStarterPlacement();

    public void showUpdateColor();

    public void showUpdateObjectiveCard();

    public void showUpdateAfterPlace();

    public void showUpdateAfterDraw();

    public void showUpdateChat();

    public void showUpdateCurrentPlayer();

    public void showUpdateSuspendedGame();

    public void showWinners();

    public void beginCommandAcquisition();

}
