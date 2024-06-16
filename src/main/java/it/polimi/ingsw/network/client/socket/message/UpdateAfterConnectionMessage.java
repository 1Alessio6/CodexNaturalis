package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;
import it.polimi.ingsw.network.client.model.ClientGame;

/**
 * UpdateAfterConnectionMessage represents the message containing the representation of the game after the connection
 * of the number of players that was set at the beginning of the game.
 */
public class UpdateAfterConnectionMessage extends NetworkMessage {

    private final ClientGame game;

    /**
     * Constructs a <code>UpdateAfterConnectionMessage</code> with the <code>game</code> provided.
     *
     * @param game the representation of the game.
     */
    public UpdateAfterConnectionMessage(ClientGame game) {
        super(Type.UPDATE_AFTER_CONNECTION, "server");
        this.game = game;
    }

    public ClientGame getGame(){
        return game;
    }
}
