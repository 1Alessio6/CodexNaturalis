package it.polimi.ingsw.model.card;

public class Card {
    private Front front;
    private Back back;

    public Card(Front front, Back back) {
        this.front = front;
        this.back = back;
    }

    public Face getFace(Side side)  {
        if (side.equals(Side.FRONT))
            return front;
        else
            return back;
    }

}

