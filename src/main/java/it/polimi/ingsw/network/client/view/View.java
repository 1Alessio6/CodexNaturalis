package it.polimi.ingsw.network.client.view;

import it.polimi.ingsw.network.VirtualView;


/**
 * Methods do not need any argument, as information are retrieved directly from the
 * ClientGame after being updated
 */
public interface View {

    //method used to read client input
    void run(VirtualView client);

    //method used to update after a controller update, invoked by the client class

    void showUpdatePlayersInLobby();

    void showUpdateCreator();

    void showUpdateAfterLobbyCrash();

    void showUpdateAfterConnection();

    void showUpdatePlayerStatus();

    void showInitialPlayerStatus();

    void showBoardSetUp();

    /**
     * Method used to show starter placement, and update availableActions for the player notified.
     * This is a broadcast update
     * @param username of the player that placed the card
     */
    void showStarterPlacement(String username);

    /**
     * Method used to show chosen color, and update availableActions for the player notified.
     * This is a broadcast update
     * @param username of the player that chose the color
     */
    void showUpdateColor(String username);

    void showUpdateObjectiveCard();

    void showUpdateAfterPlace();

    void showUpdateAfterDraw();

    void showUpdateChat();

    void showUpdateCurrentPlayer();

    void showUpdateSuspendedGame();

    void showWinners();

}
