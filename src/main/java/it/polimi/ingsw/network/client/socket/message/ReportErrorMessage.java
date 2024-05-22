package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

public class ReportErrorMessage extends NetworkMessage {

    private final String details;

    public ReportErrorMessage(String details) {
        super(Type.ERROR, "server");
        this.details = details;
    }

    public String getDetails() {
        return details;
    }
}
