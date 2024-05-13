<<<<<<<< HEAD:src/main/java/it/polimi/ingsw/network/server/socket/message/message/ConnectMessage.java
package it.polimi.ingsw.network.server.socket.server.message;
========
package it.polimi.ingsw.network.server.socket.message;
>>>>>>>> refs/remotes/origin/main:src/main/java/it/polimi/ingsw/network/server/socket/message/ConnectMessage.java

public class ConnectMessage extends ServerMessage {
    public ConnectMessage(String sender) {
        super(sender, ServerType.CONNECT);
    }
}
