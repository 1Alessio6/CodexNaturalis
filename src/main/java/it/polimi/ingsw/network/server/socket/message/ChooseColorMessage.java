package it.polimi.ingsw.network.server.socket.message;

import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

/**
 * ChooseColorMessage represents the message containing the color chosen by the player.
 */
public class ChooseColorMessage extends NetworkMessage {
    private PlayerColor color;

    /**
     * Constructs a <code>ChooseColorMessage</code> with the <code>sender</code> and <code>color</code> provided.
     *
     * @param sender the username of the player who chose the color.
     * @param color  the color chosen by the <code>sender</code>.
     */
    public ChooseColorMessage(String sender, PlayerColor color) {
        super(Type.CHOOSE_COLOR, sender);
        this.color = color;
    }

    public PlayerColor getColor() {
        return color;
    }
}
