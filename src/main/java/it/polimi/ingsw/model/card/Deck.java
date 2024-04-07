package it.polimi.ingsw.model.card;

import java.util.Stack;

public class Deck {
    String type;
    Stack<Card> deck;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Stack<Card> getDeck() {
        return deck;
    }

    public void setDeck(Stack<Card> deck) {
        this.deck = deck;
    }
}
