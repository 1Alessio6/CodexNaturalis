package it.polimi.ingsw.model.card;

import java.util.List;
import java.util.Stack;

public class Deck<T> {
    // todo. type?
    String type;
    Stack<T> deck;

    Deck(String type, Stack<T> deck) {
        this.type = type;
        this.deck = deck;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T draw() throws EmptyDeckException {
        if (deck.isEmpty()) {
            throw new EmptyDeckException("EMPTY DECK");
        }

        return deck.pop();
    }

    public void add(T obj) {
        deck.push(obj);
    }
}
