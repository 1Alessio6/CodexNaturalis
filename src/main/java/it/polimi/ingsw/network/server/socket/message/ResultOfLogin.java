package it.polimi.ingsw.network.server.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

public class ResultOfLogin extends NetworkMessage {
    private final boolean accepted;

    public ResultOfLogin(boolean accepted, String sender) {
        super(Type.RESULT_OF_LOGIN, sender);
        this.accepted = accepted;
    }

    public boolean getAccepted() {
        return accepted;
    }
}

