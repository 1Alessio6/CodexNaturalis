<<<<<<<< HEAD:src/main/java/it/polimi/ingsw/network/server/socket/message/message/PingMessage.java
package it.polimi.ingsw.network.server.socket.server.message;
========
package it.polimi.ingsw.network.server.socket.message;
>>>>>>>> refs/remotes/origin/main:src/main/java/it/polimi/ingsw/network/server/socket/message/PingMessage.java

public class PingMessage extends ServerMessage {
    public PingMessage(String sender) {
        super(sender, ServerType.SEND_PING);
    }
}
