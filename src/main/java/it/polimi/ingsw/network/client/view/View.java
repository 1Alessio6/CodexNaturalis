package it.polimi.ingsw.network.client.view;

import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.controller.ClientController;

import java.util.List;


/**
 * Methods do not need any argument, as information are retrieved directly from the
 * ClientGame after being updated
 */
public interface View {

    /**
     * Runs the view.
     */
    void runView();

    //method used to update after a controller update, invoked by the client class
    void showServerCrash();

    void showUpdatePlayersInLobby();

    void showUpdateCreator();

    void showUpdateAfterLobbyCrash();

    void showUpdateFullLobby();

    void showUpdateExceedingPlayer();

    void showInvalidLogin(String details);

    void showUpdateAfterConnection();

    void showUpdatePlayerStatus();

    void showInitialPlayerStatus();


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

    void showUpdateAfterPlace(String username);

    void showUpdateAfterDraw(String username);

    void showUpdateChat();

    void showUpdateCurrentPlayer();

    void showUpdateSuspendedGame();

    void showWinners(List<String> winners);

    void reportError(String details);

}
