package it.polimi.ingsw.model.card;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.strategies.CalculatePoints;
import it.polimi.ingsw.model.jsondeserializer.CalculatorDeserializer;
import it.polimi.ingsw.model.jsondeserializer.CornerDeserializer;
import it.polimi.ingsw.model.jsondeserializer.PositionDeserializer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
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

    // TODO: repetition needed (?) because can't create static method of generic
    public static ArrayList<Card> getPlayableCardListFromJson(String jsonPath){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(CalculatePoints.class, new CalculatorDeserializer<>());
        builder.registerTypeAdapter(Corner.class, new CornerDeserializer());
        builder.registerTypeAdapter(Position.class, new PositionDeserializer());
        Gson gson = builder.create();

        InputStream resourceAsStream = Corner.class.getResourceAsStream(jsonPath);
        Reader reader = new BufferedReader(new InputStreamReader(resourceAsStream));

        // use ArrayList because TypeAdapter for generic interfaces don't allow TypeToken of list
        return gson.fromJson(reader, new TypeToken<>(){});
    }

    public static ArrayList<ObjectiveResourceCard> getObjectiveResourceCardListFromJson(String jsonPath){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(CalculatePoints.class, new CalculatorDeserializer<>());
        builder.registerTypeAdapter(Corner.class, new CornerDeserializer());
        builder.registerTypeAdapter(Position.class, new PositionDeserializer());
        Gson gson = builder.create();

        InputStream resourceAsStream = Corner.class.getResourceAsStream(jsonPath);
        Reader reader = new BufferedReader(new InputStreamReader(resourceAsStream));

        // use ArrayList because TypeAdapter for generic interfaces don't allow TypeToken of list
        return gson.fromJson(reader, new TypeToken<>(){});
    }

    public static ArrayList<ObjectivePositionCard> getObjectivePositionCardListFromJson(String jsonPath){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(CalculatePoints.class, new CalculatorDeserializer<>());
        builder.registerTypeAdapter(Corner.class, new CornerDeserializer());
        builder.registerTypeAdapter(Position.class, new PositionDeserializer());
        Gson gson = builder.create();

        InputStream resourceAsStream = Corner.class.getResourceAsStream(jsonPath);
        Reader reader = new BufferedReader(new InputStreamReader(resourceAsStream));

        // use ArrayList because TypeAdapter for generic interfaces don't allow TypeToken of list
        return gson.fromJson(reader, new TypeToken<>(){});
    }

    public boolean isEmpty() {
        return deck.isEmpty();
    }

    public void add(T obj) {
        deck.push(obj);
    }
}
