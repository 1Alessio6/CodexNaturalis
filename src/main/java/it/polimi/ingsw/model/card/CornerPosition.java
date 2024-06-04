package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Position;

import java.util.Map;

/**
 * Enumeration representing the possible positions of the corners
 */
public enum CornerPosition {
    /**
     * Top Left, refers to the top left corner of the card.
     */
    TOP_LEFT,
    /**
     * Top right, refers to top right corner of the card.
     */
    TOP_RIGHT,
    /**
     * Lower right, refers to the lower right corner of the card.
     */
    LOWER_RIGHT,
    /**
     * Lower left, refers to the lower left corner of the card.
     */
    LOWER_LEFT;

    public static Map<Position, CornerPosition> fromPositionToCornerPosition;
    public static Map<CornerPosition, Position> fromCornerPositionToPosition;

    static {
        fromPositionToCornerPosition = Map.ofEntries(
                Map.entry(new Position(-1, 1), TOP_LEFT),
                Map.entry(new Position(1, 1), TOP_RIGHT),
                Map.entry(new Position(1, -1), LOWER_RIGHT),
                Map.entry(new Position(-1, -1), LOWER_LEFT)
        );

        fromCornerPositionToPosition = Map.ofEntries(
                Map.entry(TOP_LEFT, new Position(-1, 1)),
                Map.entry(TOP_RIGHT, new Position(1, 1)),
                Map.entry(LOWER_RIGHT, new Position(1, -1)),
                Map.entry(LOWER_LEFT, new Position(-1, -1))
        );
    }

}
