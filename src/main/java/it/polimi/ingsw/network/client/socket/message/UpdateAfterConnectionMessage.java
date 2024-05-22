package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;
import it.polimi.ingsw.network.client.model.ClientGame;

public class UpdateAfterConnectionMessage extends NetworkMessage {

    private final ClientGame game;
    public UpdateAfterConnectionMessage(ClientGame game) {
        super(Type.UPDATE_AFTER_CONNECTION, "server");
        this.game = game;
    }

    public ClientGame getGame(){
        return game;
    }
}
