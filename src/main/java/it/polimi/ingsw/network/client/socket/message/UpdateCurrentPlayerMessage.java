package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

/**
 * UpdateCurrentPlayerMessage represents the message containing the new current player and his current phase.
 */
public class UpdateCurrentPlayerMessage extends NetworkMessage {

    private final int currentPlayerIdx;
    private final GamePhase currentPhase;

    /**
     * Constructs an <code>UpdateCurrentPlayerMessage</code> with the <code>currentPlayerIdx</code> and the
     * <code>currentPhase</code> provided.
     *
     * @param currentPlayerIdx the index of the current player.
     * @param currentPhase     the phase in which the current player is.
     */
    public UpdateCurrentPlayerMessage(int currentPlayerIdx, GamePhase currentPhase) {
        super(Type.SHOW_UPDATE_CURRENT_PLAYER, "server");
        this.currentPlayerIdx = currentPlayerIdx;
        this.currentPhase = currentPhase;
    }

    public int getCurrentPlayerIdx() {
        return currentPlayerIdx;
    }

    public GamePhase getCurrentPhase() {
        return currentPhase;
    }
}
