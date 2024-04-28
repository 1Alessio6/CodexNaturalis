package it.polimi.ingsw.model.gamePhase;

import it.polimi.ingsw.model.player.Player;

import java.util.List;

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

    private List<Player> players;

    private final int lastPlayerIdx;

    public PhaseHandler(List<Player> players) {
        this.players = players;
        this.lastNormalTurn = false;
        this.lastPlayerIdx = players.size() - 1;
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
                nextPhase = GamePhase.PlaceNormal;
                for (Player p : players) {
                    if (!p.isSetupFinished()) {
                        nextPhase = GamePhase.Setup;
                        break;
                    }
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
