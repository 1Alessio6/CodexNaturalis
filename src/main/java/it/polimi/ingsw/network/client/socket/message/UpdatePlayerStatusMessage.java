package it.polimi.ingsw.network.client.socket.message;

public class UpdatePlayerStatusMessage extends ClientMessage{

    private final boolean isConnected;
    private final String username;

    public UpdatePlayerStatusMessage(boolean isConnected, String username) {
        super(ClientType.SHOW_UPDATE_PLAYER_STATUS);
        this.isConnected = isConnected;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
