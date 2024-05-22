package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

import java.util.List;

public class UpdatePlayersInLobbyMessage extends NetworkMessage {

    private final List<String> usernames;

    public UpdatePlayersInLobbyMessage(List<String> usernames) {
        super(Type.SHOW_UPDATE_PLAYERS_IN_LOBBY, "server");
        this.usernames = usernames;
    }

    public List<String> getUsernames() {
        return usernames;
    }
}
