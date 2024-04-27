package it.polimi.ingsw.model.card;

public class Card {

    /**
     * Represents the front of the card
     */
    private Front front;
    /**
     * Represents the back of the card
     */
    private Back back;

    // id to represent a card unambiguously.
    private int id;

    private static int incrementalId = 0;

    private static int getIncrementalId() {
        return ++incrementalId;
    }

    /**
     * Constructs a Card with the front and the back provided.
     *
     * @param front of the card.
     * @param back  of the card.
     */
    public Card(Front front, Back back) {
        this.front = front;
        this.back = back;
        this.id = getIncrementalId();
    }

    /**
     * Returns the face of the face with the given side.
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

    public int getId() {
        return id;
    }

}

