package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

/**
 * UpdateColorMessage represents the message containing the color to <B>update</B> the player's color.
 */
public class UpdateColorMessage extends NetworkMessage {
    private final String username;
    private final PlayerColor colorSelected;

    /**
     * Constructs an <code>UpdateColorMessage</code> with the <code>username</code> and <code>colorSelected</code>
     * provided.
     *
     * @param username      the username of the player who chose the color.
     * @param colorSelected the color chosen by the <code>username</code>.
     */
    public UpdateColorMessage(String username, PlayerColor colorSelected) {
        super(Type.SHOW_UPDATE_COLOR, "server");
        this.username = username;
        this.colorSelected = colorSelected;
    }

    public PlayerColor getColorSelected() {
        return colorSelected;
    }

    public String getUsername() {
        return username;
    }
}
