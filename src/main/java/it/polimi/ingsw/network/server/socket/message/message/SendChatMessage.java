<<<<<<<< HEAD:src/main/java/it/polimi/ingsw/network/server/socket/message/message/SendChatMessage.java
package it.polimi.ingsw.network.server.socket.server.message;
========
package it.polimi.ingsw.network.server.socket.message;
>>>>>>>> refs/remotes/origin/main:src/main/java/it/polimi/ingsw/network/server/socket/message/SendChatMessage.java

import it.polimi.ingsw.model.chat.message.Message;

public class SendChatMessage extends ServerMessage {
    Message message;
    public SendChatMessage(String sender, Message message) {
        super(sender, ServerType.SEND_CHAT_MESSAGE);
        this.message = message;
    }
}
