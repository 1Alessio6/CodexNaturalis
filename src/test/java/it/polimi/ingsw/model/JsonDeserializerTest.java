package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.strategies.CalculatePoints;
import it.polimi.ingsw.model.card.strategies.CalculateResources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class JsonDeserializerTest {
    private Gson gson;

    @BeforeEach
    public void setUp() {
        System.out.println("Setup");
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(CalculatePoints.class, new JsonTestCode<>());
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
        System.out.println(frontJson);

        Front f = gson.fromJson(frontJson, Front.class);
        Assertions.assertEquals(front.getColor(), Color.BLUE);
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
        System.out.println(cardJson);

//        Card cInv = gson.fromJson(cardJson, Card.class);
//        Assertions.assertEquals(cInv.getFace(Side.FRONT).getColor(), Color.BLUE);
    }


}