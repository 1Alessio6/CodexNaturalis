package it.polimi.ingsw.network.client.view;

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

    /**
     * Method used to show the crash of the server through a message in output.
     */
    void showServerCrash();

    /**
     * Method used to show the update of the current players in the lobby.
     */
    void showUpdatePlayersInLobby();

    /**
     * Method used to show a welcome message to the creator and allow him/her to set the lobby size.
     */
    void showUpdateCreator();

    /**
     * Method used to show the crash of the lobby through a message in output.
     */
    void showUpdateAfterLobbyCrash();

    /**
     * Method used to notify an exceeding player about his/her status.
     */
    void showUpdateExceedingPlayer();

    /**
     * Method used to show a player that he/she has digitized an invalid username
     *
     * @param details the reasons
     */
    void showInvalidLogin(String details);

    /**
     * Method used to show the playing area of the player after the connection. //review
     */
    void showUpdateAfterConnection();

    /**
     * Method used to show the current status of the players.
     */
    void showUpdatePlayerStatus();

    /**
     * Method used to show starter placement, and update availableActions for the player notified.
     * This is a broadcast update
     *
     * @param username of the player that placed the card
     */
    void showStarterPlacement(String username);

    /**
     * Method used to show chosen color, and update availableActions for the player notified.
     * This is a broadcast update
     *
     * @param username of the player that chose the color
     */
    void showUpdateColor(String username);

    /**
     * Method used to show either common or private objective cards.
     */
    void showUpdateObjectiveCard();

    /**
     * Method used to show the playing area after the placement of a card in the playground.
     *
     * @param username of the player that placed the card.
     */
    void showUpdateAfterPlace(String username);

    /**
     * Method used to show the update of the player's hand, face up cards and decks.
     */
    void showUpdateAfterDraw(String username);

    /**
     * Method used to show the updated chat.
     */
    void showUpdateChat();

    /**
     * Method used to indicate the player's turn.
     */
    void showUpdateCurrentPlayer();

    /**
     * Method used to show a message of suspension or activation of the game.
     */
    void showUpdateSuspendedGame();

    /**
     * Method used to show the winners of the game.
     *
     * @param winners of the game.
     */
    void showWinners(List<String> winners);

    /**
     * Method used to report an error
     *
     * @param details error details
     */
    void reportError(String details);

    /**
     * Shows the connection to the server is lost.
     */
    void showConnectionLost();
}
