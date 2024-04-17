package it.polimi.ingsw.model.card;

import java.util.List;
import java.util.Stack;

public class Deck<T> {
    // todo. type?

    /**
     * Represents the type of the deck.
     */
    private final DeckType type;

    /**
     * Represents a deck of all types of cards.
     */
    private final Stack<T> deck;

    /**
     * Constructs a deck of any kind of cards using the type and the deck provided
     *
     * @param type of the deck.
     * @param deck of objects.
     */
    public Deck(DeckType type, Stack<T> deck) {
        this.type = type;
        this.deck = deck;
    }

    /**
     * Returns the type of the deck.
     *
     * @return deck's type
     */
    public DeckType getType() {
        return type;
    }

    /**
     * Draw an object from the deck.
     *
     * @return the new deck without the drawn card
     * @throws EmptyDeckException if the deck is empty
     */
    public T draw() throws EmptyDeckException {
        if (deck.isEmpty()) {
            throw new EmptyDeckException("EMPTY DECK");
        }

        return deck.pop();
    }

    /**
     * Tests if the stack is empty
     * @return true if the deck is empty, false otherwise
     */
    public boolean isEmpty() {
        return deck.isEmpty();
    }

    /**
     * Adds a card in the deck
     * @param obj referring to any type of card
     */
    public void add(T obj) {
        deck.push(obj);
    }
}
