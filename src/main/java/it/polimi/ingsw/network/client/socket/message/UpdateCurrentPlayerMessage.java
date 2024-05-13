package it.polimi.ingsw.network.client.socket.message;

import it.polimi.ingsw.model.gamePhase.GamePhase;

public class UpdateCurrentPlayerMessage extends ClientMessage{

    private final int currentPlayerIdx;
    private final GamePhase currentPhase;
    public UpdateCurrentPlayerMessage(int currentPlayerIdx, GamePhase currentPhase) {
        super(ClientType.SHOW_UPDATE_CURRENT_PLAYER);
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
