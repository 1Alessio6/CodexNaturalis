<<<<<<<< HEAD:src/main/java/it/polimi/ingsw/network/server/socket/message/message/ServerMessage.java
package it.polimi.ingsw.network.server.socket.server.message;
========
package it.polimi.ingsw.network.server.socket.message;
>>>>>>>> refs/remotes/origin/main:src/main/java/it/polimi/ingsw/network/server/socket/message/ServerMessage.java

public class ServerMessage {
    ServerType type;
    String sender;

        public ServerMessage(String sender, ServerType type) {
        this.type = type;
        this.sender = sender;
    }

    public ServerType getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }
}
