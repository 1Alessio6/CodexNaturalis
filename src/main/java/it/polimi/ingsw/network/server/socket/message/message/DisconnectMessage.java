<<<<<<<< HEAD:src/main/java/it/polimi/ingsw/network/server/socket/message/message/DisconnectMessage.java
package it.polimi.ingsw.network.server.socket.server.message;
========
package it.polimi.ingsw.network.server.socket.message;
>>>>>>>> refs/remotes/origin/main:src/main/java/it/polimi/ingsw/network/server/socket/message/DisconnectMessage.java

public class DisconnectMessage extends ServerMessage {
    public DisconnectMessage(String sender) {
        super(sender, ServerType.DISCONNECT);
    }
}
