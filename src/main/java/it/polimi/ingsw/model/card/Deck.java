package it.polimi.ingsw.model.card;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.jsondeserializer.InterfaceAdaptor;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.strategies.CalculatePoints;
import it.polimi.ingsw.model.jsondeserializer.CalculatorDeserializer;
import it.polimi.ingsw.model.jsondeserializer.CornerDeserializer;
import it.polimi.ingsw.model.jsondeserializer.PositionDeserializer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

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

    /**
     * Retrieves the StreamReader of the resource
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
     * @param resourcePath of target file to deserialize
     * @return the list of that file.
     * ArrayList return because List is interface, and would be deserialized according to custom interface deserializer
     * @param <K> whether is Face or ObjectiveCard
     */
    private static <K> ArrayList<K> createFaceList(String resourcePath){
        GsonBuilder builder = new GsonBuilder().enableComplexMapKeySerialization();
        builder.registerTypeAdapter(CalculatePoints.class, new InterfaceAdaptor<>());

        return builder.create().fromJson(getReaderFromResource(resourcePath), new TypeToken<>(){});
    }

    /**
     * Creates the actual list to be passed to Deck constructor
     */
    public static List<Card> createCardList(){
        // todo: replace with correct paths
        List<Front> fronts = createFaceList("aa");
        List<Back> backs = createFaceList("aa");

        List<Card> cardList = new ArrayList<>();
        for (int i = 0; i < fronts.size(); ++i){
            cardList.add(new Card(fronts.removeFirst(), backs.removeFirst()));
        }

        return cardList;
    }

    /**
     * Creates the actual objective list to be passed to Deck constructor
     */
    public static List<ObjectiveCard> createObjectiveCardList(){
        Gson gson = new Gson();
        List<ObjectiveCard> objectives = new ArrayList<>();

        // todo: replace with correct paths
        objectives.addAll(createFaceList("path1"));
        objectives.addAll(createFaceList("path2"));

        return objectives;
    }

    public boolean isEmpty() {
        return deck.isEmpty();
    }

    public void add(T obj) {
        deck.push(obj);
    }
}
