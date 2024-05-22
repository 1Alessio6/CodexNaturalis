package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

public class UpdateCurrentPlayerMessage extends NetworkMessage {

    private final int currentPlayerIdx;
    private final GamePhase currentPhase;
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
