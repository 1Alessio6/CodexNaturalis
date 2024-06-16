package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

import java.util.List;

/**
 * UpdatePlayersInLobbyMessage represents the message containing the new players to add to the lobby.
 */
public class UpdatePlayersInLobbyMessage extends NetworkMessage {

    private final List<String> usernames;

    /**
     * Constructs an <code>UpdatePlayersInLobbyMessage</code> with the <code>usernames</code> provided.
     *
     * @param usernames the usernames of the players to add to the lobby.
     */
    public UpdatePlayersInLobbyMessage(List<String> usernames) {
        super(Type.SHOW_UPDATE_PLAYERS_IN_LOBBY, "server");
        this.usernames = usernames;
    }

    public List<String> getUsernames() {
        return usernames;
    }
}
