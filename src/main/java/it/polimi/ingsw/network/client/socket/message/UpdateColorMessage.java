package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

public class UpdateColorMessage extends NetworkMessage {
    private final String username;
    private final PlayerColor colorSelected;

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
