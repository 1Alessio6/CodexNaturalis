package it.polimi.ingsw.network.server.socket.message;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

/**
 * DrawMessage represents the message that allows a player to draw a card.
 */
public class DrawMessage extends NetworkMessage {
    private int idToDraw;

    /**
     * Constructs a <code>DrawMessage</code> with the <code>sender</code> and <code>idToDraw</code> provided.
     *
     * @param sender   the username of the player who performs the drawing.
     * @param idToDraw the index of the card to be drawn.
     */
    public DrawMessage(String sender, int idToDraw) {
        super(Type.DRAW, sender);
        this.idToDraw = idToDraw;
    }

    public int getIdDraw() {
        return idToDraw;
    }
}
