package it.polimi.ingsw.model.card;

import java.util.List;
import java.util.Stack;

public class Deck<T> {
    // todo. type?
    private final DeckType type;
    private final Stack<T> deck;

    public Deck(DeckType type, Stack<T> deck) {
        this.type = type;
        this.deck = deck;
    }

    public DeckType getType() {
        return type;
    }

    public T draw() throws EmptyDeckException {
        if (deck.isEmpty()) {
            throw new EmptyDeckException("EMPTY DECK");
        }

        return deck.pop();
    }

    public boolean isEmpty() {
        return deck.isEmpty();
    }

    public void add(T obj) {
        deck.push(obj);
    }
}
