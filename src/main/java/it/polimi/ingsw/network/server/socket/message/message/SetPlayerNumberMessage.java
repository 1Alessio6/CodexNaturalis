<<<<<<<< HEAD:src/main/java/it/polimi/ingsw/network/server/socket/message/message/SetPlayerNumberMessage.java
package it.polimi.ingsw.network.server.socket.server.message;
========
package it.polimi.ingsw.network.server.socket.message;
>>>>>>>> refs/remotes/origin/main:src/main/java/it/polimi/ingsw/network/server/socket/message/SetPlayerNumberMessage.java

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
