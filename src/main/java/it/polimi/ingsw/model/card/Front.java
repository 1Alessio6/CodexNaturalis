package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Playground;

import java.util.Map;
import java.util.List;

public class Front extends Face {
    private final int score;

    /**
     * Constructs a front card with the color, score and corners provided.
     *
     * @param color   of the card.
     * @param cornerList of the card.
     * @param score   obtained after positioning the card.
     * @throws IllegalArgumentException if any argument is null or there are not 4 corners or the score is negative.
     */
    Front(Color color, List<Corner> cornerList, int score) throws IllegalArgumentException {
        super(color, cornerList);

        if (score < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }

        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public int calcPoints(Playground p) {
        return 0;
    }
}
