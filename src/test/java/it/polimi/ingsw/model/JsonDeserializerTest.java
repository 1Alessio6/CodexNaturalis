package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.strategies.CalculatePoints;
import it.polimi.ingsw.model.card.strategies.CalculateResources;
import it.polimi.ingsw.model.jsondeserializer.CornerDeserializer;
import it.polimi.ingsw.model.jsondeserializer.PositionDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

public class JsonDeserializerTest {
    private Gson gson;
    private final static String goldenCardsPath = "/cards/goldenCards.json";
    private final static String resourceCardsPath = "/cards/resourceCards.json";
    private final static String startingCardsPath = "/cards/startingCards.json";
    private final static String objectivePositionCardsPath = "/cards/objectivePositionCards.json";
    private final static String objectiveResourceCardsPath = "/cards/objectiveResourceCards.json";

    @BeforeEach
    public void setUp() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(CalculatePoints.class, new JsonTestCode<>());
        builder.registerTypeAdapter(Corner.class, new CornerDeserializer());
        builder.registerTypeAdapter(Position.class, new PositionDeserializer());
        gson = builder.create();
    }

    @Test
    void testDeSerializationFront() {
        Front front = new GoldenFront(
                Color.BLUE,
                new HashMap<>(),
                0,
                Condition.CORNERS,
                new CalculateResources(),
                new HashMap<>()
        );

        String frontJson = gson.toJson(front);

        Front f = gson.fromJson(frontJson, Front.class);
        Assertions.assertEquals(front.getColor(), Color.BLUE);
    }

    @Test
    void testDeSerializationBack() {
        Back back = new Back(
                Color.BLUE,
                new HashMap<>(),
                new HashMap<>()
        );

        String backJson = gson.toJson(back);

        Back b = gson.fromJson(backJson, Back.class);
        System.out.println(b);
        Assertions.assertEquals(back.getColor(), b.getColor());
    }

    @Test
    void createCard() {
        Card c = new Card(
                new GoldenFront(
                        Color.BLUE,
                        new HashMap<>(),
                        0,
                        Condition.CORNERS,
                        new CalculateResources(),
                        new HashMap<>()
                ),
                new Back(
                        Color.BLUE,
                        new HashMap<CornerPosition, Corner>(),
                        new HashMap<>()
                )
        );

        String cardJson = gson.toJson(c);
        Card cInv = gson.fromJson(cardJson, Card.class);
        Assertions.assertEquals(cInv.getFace(Side.FRONT).getColor(), Color.BLUE);
    }

    @Test
    void deserializeGoldenCardsFromFile() {
        InputStream resourceAsStream = Corner.class.getResourceAsStream(goldenCardsPath);
        Reader reader = new BufferedReader(new InputStreamReader(resourceAsStream));
        ArrayList<Card> myCards = gson.fromJson(reader, new TypeToken<>(){});
    }

    @Test
    void deserializeResourceCardsFromFile() {
        InputStream resourceAsStream = Corner.class.getResourceAsStream(resourceCardsPath);
        Reader reader = new BufferedReader(new InputStreamReader(resourceAsStream));
        ArrayList<Card> myCards = gson.fromJson(reader, new TypeToken<>(){});
    }

    @Test
    void deserializeObjectiveCardsFromFile() {
        InputStream objectiveResourceAsStream = Corner.class.getResourceAsStream(resourceCardsPath);
        Reader reader = new BufferedReader(new InputStreamReader(objectiveResourceAsStream));
        ArrayList<ObjectiveResourceCard> objectiveResourceCards = gson.fromJson(reader, new TypeToken<ArrayList<ObjectiveResourceCard>>(){});

        InputStream objectivePositionAsStream = Corner.class.getResourceAsStream(objectivePositionCardsPath);
        reader = new BufferedReader(new InputStreamReader(objectivePositionAsStream));
        ArrayList<ObjectivePositionCard> objectivePosition = gson.fromJson(reader, new TypeToken<ArrayList<ObjectivePositionCard>>(){});

        System.out.println(objectivePosition.getFirst());
    }

}