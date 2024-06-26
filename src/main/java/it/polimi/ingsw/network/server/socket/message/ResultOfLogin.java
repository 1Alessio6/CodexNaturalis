package it.polimi.ingsw.network.server.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

/**
 * ResultOfLogin represents the message containing the result of the login
 */
public class ResultOfLogin extends NetworkMessage {
    private final boolean accepted;
    private final String selectedUsername;
    private final String details;

    /**
     * Constructs <code>ResultOfLogin</code> with the <code>username</code> and the <code>details</code> provided
     *
     * @param accepted boolean indicating whether the player is accepted or not.
     * @param username the player's username.
     * @param details  the error details.
     */
    public ResultOfLogin(boolean accepted, String username, String details) {
        super(Type.RESULT_OF_LOGIN, "server");
        this.accepted = accepted;
        this.selectedUsername = username;
        this.details = details;
    }

    public boolean getAccepted() {
        return accepted;
    }

    public String getDetails() {
        return details;
    }

    public String getSelectedUsername() {
        return selectedUsername;
    }
}

