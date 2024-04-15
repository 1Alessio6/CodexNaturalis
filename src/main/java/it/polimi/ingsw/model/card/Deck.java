package it.polimi.ingsw.model.card;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Deck<T> {
    private final Stack<T> deck;

    public Deck(Stack<T> deck) {
        this.deck = deck;
    }

    public Deck(List<T> list) {
        Collections.shuffle(list);
        deck = new Stack<>();
        deck.addAll(list);
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
