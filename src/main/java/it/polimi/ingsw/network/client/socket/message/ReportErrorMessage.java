package it.polimi.ingsw.network.client.socket.message;

public class ReportErrorMessage extends ClientMessage{

    private final String details;

    public ReportErrorMessage(ClientType type, String details) {
        super(type);
        this.details = details;
    }

    public String getDetails() {
        return details;
    }
}
