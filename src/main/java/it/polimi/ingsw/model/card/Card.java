package it.polimi.ingsw.model.card;

import java.util.Map;

/**
 * Card represents the card
 * It has front and back
 */
public class Card{

    /**
     * Represents the front of the card
     */
    private Front front;
    /**
     * Represents the back of the card
     */
    private Back back;


    /**
     * Constructs a Card with the front and the back provided.
     *
     * @param front of the card.
     * @param back  of the card.
     */
    public Card(Front front, Back back) {
        this.front = front;
        this.back = back;
    }

    /**
     * Returns the face of the card with the given side.
     *
     * @param side of the card.
     * @return the face of the card.
     */
    public Face getFace(Side side) {
        if (side.equals(Side.FRONT))
            return front;
        else
            return back;
    }

    public int getFrontId() {
        return front.getId();
    }

    public int getBackId() {
        return back.getId();
    }

    public Map<Symbol, Integer> getRequiredResources() {
        return front.getRequiredResources();
    }
}

