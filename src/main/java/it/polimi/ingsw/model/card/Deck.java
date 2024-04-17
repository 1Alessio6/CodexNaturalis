package it.polimi.ingsw.model.card;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Deck<T> {

    /**
     * Represents a deck of all types of cards.
     */
    private final Stack<T> deck;

    /**
     * Constructs a deck of any kind of cards using the deck provided.
     *
     * @param deck of cards.
     */
    public Deck(Stack<T> deck) {
        this.deck = deck;
    }


    /**
     * Constructs a deck of any kind of cards using the list of cards provided.
     *
     * @param list of cards.
     */
    public Deck(List<T> list) {
        Collections.shuffle(list);
        deck = new Stack<>();
        deck.addAll(list);
    }

    /**
     * Draw an object from the deck.
     *
     * @return the new deck without the drawn card.
     * @throws EmptyDeckException if the deck is empty.
     */
    public T draw() throws EmptyDeckException {
        if (deck.isEmpty()) {
            throw new EmptyDeckException("EMPTY DECK");
        }

        return deck.pop();
    }

    /**
     * Tests if the stack is empty.
     *
     * @return true if the deck is empty, false otherwise.
     */
    public boolean isEmpty() {
        return deck.isEmpty();
    }

    /**
     * Adds a card in the deck.
     *
     * @param obj referring to any type of card.
     */
    public void add(T obj) {
        deck.push(obj);
    }
}
