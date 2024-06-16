package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

/**
 * ReportErrorMessage represents the message containing the error details.
 */
public class ReportErrorMessage extends NetworkMessage {

    private final String details;

    /**
     * Constructs a <code>ReportErrorMessage</code> with the <code>details</code> provided.
     *
     * @param details the details of the error.
     */
    public ReportErrorMessage(String details) {
        super(Type.ERROR, "server");
        this.details = details;
    }

    public String getDetails() {
        return details;
    }
}
