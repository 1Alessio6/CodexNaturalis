package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.board.Tile;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ObjectiveLPositionCard extends ObjectivePositionCard{
    public ObjectiveLPositionCard(Map<Position, Color> condition, int multiplier) throws IllegalArgumentException {
        super(condition, multiplier);
    }
}

