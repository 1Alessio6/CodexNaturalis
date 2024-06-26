package it.polimi.ingsw.model.lobby;

import java.util.List;

public interface LobbyListener {
    /**
     * Method used to show a welcome message to the creator and allow him/her to set the lobby size.
     */
    void updateCreator();

    /**
     * Method used to show the crash of the lobby through a message in output.
     */
    void updateAfterLobbyCrash();

    /**
     * Method used to update the lobby with the <code>usernames</code> of the players.
     *
     * @param usernames of the players.
     */
    void showUpdatePlayersInLobby(List<String> usernames);

    void showUpdateExceedingPlayer();

    /**
     * Method used to report error <code>details</code>.
     *
     * @param details the details to be reported.
     */
    void reportError(String details);

    /**
     * Method used to connect the player or an output with the <code>details</code>, depending on whether the
     * player is <code>accepted</code> or not.
     *
     * @param accepted boolean indicating whether the player is accepted or not.
     * @param details  the error details.
     */
    void resultOfLogin(boolean accepted, String details);
}
