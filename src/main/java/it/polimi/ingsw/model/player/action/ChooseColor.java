package it.polimi.ingsw.model.player.action;

import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.player.Player;

public class ChooseColor extends PlayerAction {
    /**
     * {@inheritDoc}
     */
    @Override
    public void assignColor(Player player, PlayerColor color) {
        player.setColor(color);
        nextAction(player, new ChooseObjective());
    }

    @Override
    public PlayerState getPlayerState() {
        return PlayerState.ChooseColor;
    }
}
