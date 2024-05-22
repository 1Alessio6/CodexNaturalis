package it.polimi.ingsw.network.server.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

public class SetPlayerNumberMessage extends NetworkMessage {
    private int numPlayers;

    public SetPlayerNumberMessage(String sender, int numPlayers) {
        super(Type.SET_PLAYER_NUMBER, sender);
        this.numPlayers = numPlayers;
    }

    public int getNumPlayers() {
        return numPlayers;
    }
}
