package it.polimi.ingsw.network.server.socket.server.message;

public class SetPlayerNumberMessage extends ServerMessage {
    private int numPlayers;

    public SetPlayerNumberMessage(String sender, int numPlayers) {
        super(sender, ServerType.SET_PLAYER_NUMBER);
        this.numPlayers = numPlayers;
    }

    public int getNumPlayers() {
        return numPlayers;
    }
}
