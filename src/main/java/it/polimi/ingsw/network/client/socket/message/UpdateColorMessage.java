package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.model.card.Color.PlayerColor;

public class UpdateColorMessage extends ClientMessage {
    private final String username;
    private final PlayerColor colorSelected;

    public UpdateColorMessage(String username, PlayerColor colorSelected) {
        super(ClientType.SHOW_UPDATE_COLOR);
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
