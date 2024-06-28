package it.polimi.ingsw.model.deck;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.jsondeserializer.InterfaceAdaptor;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.strategies.CalculatePoints;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * Deck represents a <code>T</code> deck
 *
 * @param <T> the kind of the card
 */
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
     * Retrieves the StreamReader of the resource
     *
     * @param resourcePath virtual path inside resource folder
     * @return reader
     * @throws NullPointerException if the resource doesn't exist, or is empty
     */
    private static Reader getReaderFromResource(String resourcePath) throws NullPointerException {
        /* json as streams, so even after jar build it can retrieve the correct file */
        InputStream resourceAsStream = Deck.class
                .getResourceAsStream(resourcePath);
        if (resourceAsStream == null)
            throw new NullPointerException("Empty resource!");

        return new BufferedReader(new InputStreamReader(resourceAsStream));
    }

    /**
     * Creates Face (or objectiveCard) list, to pass to other constructors.
     *
     * @param resourcePath of target file to deserialize
     * @param <K>          whether is Face or ObjectiveCard
     * @return the list of that file.
     * ArrayList return because List is interface, and would be deserialized according to custom interface deserializer
     */
    private static <K> ArrayList<K> createFaceList(String resourcePath) {
        GsonBuilder builder = new GsonBuilder().enableComplexMapKeySerialization();
        builder.registerTypeAdapter(CalculatePoints.class, new InterfaceAdaptor<>());

        return builder.create().fromJson(getReaderFromResource(resourcePath), new TypeToken<>() {
        });
    }

    public T getTop() {
        return deck.isEmpty() ? null : deck.peek();
    }

    /**
     * Checks whether a deck is empty or not.
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
