package it.polimi.ingsw.network.server.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

/**
 * SetPlayerNumberMessage represents the message containing the number of players.
 */
public class SetPlayerNumberMessage extends NetworkMessage {
    private int numPlayers;

    /**
     * Constructs a <code>SetPlayerNumberMessage</code> with the <code>sender</code> and <code>numPlayers</code> provided.
     *
     * @param sender     the username of the player who set the number of players, that is, the name of the first player.
     * @param numPlayers the number of players chosen by the first player.
     */
    public SetPlayerNumberMessage(String sender, int numPlayers) {
        super(Type.SET_PLAYER_NUMBER, sender);
        this.numPlayers = numPlayers;
    }

    public int getNumPlayers() {
        return numPlayers;
    }
}
