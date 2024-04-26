package it.polimi.ingsw.model.gamePhase;

import it.polimi.ingsw.model.player.Player;

import java.util.List;

/**
 * Class that handles the game's phases.
 * The FSM looks like
 *      Setup -> PlaceNormal -> Draw -> PlaceNormal -> Draw
 *                                   -> PlaceAdditional (if all players have finished the additional turn)
 *                                                         -> PlaceAdditional
 *                                                         -> End
 */

public class PhaseHandler {
    private boolean lastNormalTurn;

    private int numPlayersPlayedLastTurn;

    private int numAdditionalTurns;

    private List<Player> players;

    public PhaseHandler(List<Player> players) {
        this.players = players;
        this.lastNormalTurn = false;
        this.numPlayersPlayedLastTurn = 0;
        this.numAdditionalTurns = 0;
    }

    public void setLastNormalTurn() {
        lastNormalTurn = true;
    }

    /**
     * Returns the next phase.
     * @param currentPhase the current game's phase.
     * @return the next game's phase.
     */
    public GamePhase getNextPhase(GamePhase currentPhase) {
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
                if (lastNormalTurn) {
                    numPlayersPlayedLastTurn += 1;
                    // all players have played the additional turn
                    if (numPlayersPlayedLastTurn == players.size()) {
                        nextPhase = GamePhase.PlaceAdditional;
                    }
                }
                break;
            case PlaceAdditional:
                nextPhase = GamePhase.PlaceAdditional;
                numAdditionalTurns += 1;
                if (numAdditionalTurns == players.size()) {
                    nextPhase = GamePhase.End;
                }
                break;
            default:
                nextPhase = GamePhase.End;
                break;
        }
        return nextPhase;
    }

    /**
     * Update the game phase after skipping a turn.
     * @param phase of the game.
     * @return the next phase.
     */
    public GamePhase skipTurn(GamePhase phase) {
        if (phase.equals(GamePhase.PlaceNormal)) {
            // skips placing and simulate a draw
            return getNextPhase(GamePhase.DrawNormal);
        }
        return getNextPhase(GamePhase.PlaceAdditional);
    }
}
