package it.polimi.ingsw.model.gamePhase;

/**
 * Class that handles the game's phases.
 * The FSM looks like
 * Setup -> PlaceNormal -> Draw -> PlaceNormal -> Draw
                                                 * -> PlaceAdditional (if all players have finished the additional turn)
                                                 * -> PlaceAdditional
                                                 * -> End
 */

public class PhaseHandler {
    private boolean lastNormalTurn;
    private final int numPlayers;
    private int counterSetupPlayers;
    private final int lastPlayerIdx;

    public PhaseHandler(int numPlayers) {
        this.numPlayers = numPlayers;
        counterSetupPlayers = 0;
        this.lastNormalTurn = false;
        this.lastPlayerIdx = numPlayers - 1;
    }

    public void setLastNormalTurn() {
        lastNormalTurn = true;
    }

    /**
     * Returns the next phase.
     *
     * @param currentPhase the current game's phase.
     * @return the next game's phase.
     */
    public GamePhase getNextPhase(GamePhase currentPhase, int currentPlayerIdx) {
        GamePhase nextPhase = null;
        switch (currentPhase) {
            case Setup:
                counterSetupPlayers += 1;
                if (counterSetupPlayers == numPlayers) {
                    nextPhase = GamePhase.PlaceNormal;
                } else {
                    nextPhase = GamePhase.Setup;
                }
                break;
            case PlaceNormal:
                nextPhase = GamePhase.DrawNormal;
                break;
            case DrawNormal:
                nextPhase = GamePhase.PlaceNormal;
                if (lastNormalTurn && currentPlayerIdx == lastPlayerIdx) {
                    nextPhase = GamePhase.PlaceAdditional;
                }
                break;
            case PlaceAdditional:
                nextPhase = GamePhase.PlaceAdditional;
                if (currentPlayerIdx == lastPlayerIdx) {
                    nextPhase = GamePhase.End;
                }
                break;
            default:
                nextPhase = GamePhase.End;
                break;
        }
        return nextPhase;
    }
}
