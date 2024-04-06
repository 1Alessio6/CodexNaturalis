package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Tile;

import java.util.Map;

public class ObjectiveDiagonalPositionCard extends ObjectivePositionCard{
    public ObjectiveDiagonalPositionCard(Map<Position, Color> condition, int multiplier) throws IllegalArgumentException {
        super(condition, multiplier);
    }
}
