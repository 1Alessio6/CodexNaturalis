package it.polimi.ingsw.network.client.socket.message;

import java.util.List;

public class UpdatePlayersInLobbyMessage extends ClientMessage{

    private final List<String> usernames;

    public UpdatePlayersInLobbyMessage(List<String> usernames) {
        super(ClientType.SHOW_UPDATE_PLAYERS_IN_LOBBY);
        this.usernames = usernames;
    }

    public List<String> getUsernames() {
        return usernames;
    }
}
