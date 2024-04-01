package it.polimi.ingsw.model.card;

public class Card {
    private Front front;
    private Back back;

    public Card(Front front, Back back) {
        this.front = front;
        this.back = back;
    }

    public Back getBack() {
        return back;
    }

    public Front getFront() {
        return front;
    }

}
