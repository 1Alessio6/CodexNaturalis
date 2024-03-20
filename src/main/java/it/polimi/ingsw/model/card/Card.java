package it.polimi.ingsw.model.card;

public class Card {
    private Front front;
    private Back back;

    public Back getBack() {
        return back;
    }

    public void setBack(Back back) {
        this.back = back;
    }

    public Front getFront() {
        return front;
    }

    public void setFront(Front front) {
        this.front = front;
    }
}
