package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.client.model.ClientGame;

public class UpdateAfterConnectionMessage extends ClientMessage{

    private final ClientGame game;
    public UpdateAfterConnectionMessage(ClientGame game) {
        super(ClientType.UPDATE_AFTER_CONNECTION);
        this.game = game;
    }

    public ClientGame getGame(){
        return game;
    }
}
