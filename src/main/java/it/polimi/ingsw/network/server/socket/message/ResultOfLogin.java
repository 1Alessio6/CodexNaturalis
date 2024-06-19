package it.polimi.ingsw.network.server.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

public class ResultOfLogin extends NetworkMessage {
    private final boolean accepted;
    private final String selectedUsername;
    private final String details;

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

