package it.polimi.ingsw.network.client.socket.message;

public class ReportErrorMessage extends ClientMessage{

    private final String details;

    public ReportErrorMessage(String details) {
        super(ClientType.ERROR);
        this.details = details;
    }

    public String getDetails() {
        return details;
    }
}
