package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.jsondeserializer.InterfaceAdaptor;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.strategies.CalculateNoCondition;
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
        GsonBuilder builder = new GsonBuilder().enableComplexMapKeySerialization();

        // @Carlo: builder.registerTypeAdapter(CalculatePoints.class, new JsonTestCode<>());

        // @Riccardo: builder.registerTypeAdapter(CalculatePoints.class, new InterfaceAdaptor<>());

        builder.registerTypeAdapter(CalculatePoints.class, new InterfaceAdaptor<>());
        gson = builder.create();
    }


    @Test
    void deserializeFront() {

        HashMap<CornerPosition, Corner> corners = new HashMap<>();

        corners.put(CornerPosition.LOWER_LEFT, new Corner());
        corners.put(CornerPosition.LOWER_RIGHT, new Corner());

        Front front =
                new Front(
                        Color.BLUE,
                        corners,
                        1
                );

        String fJson = gson.toJson(front);

        System.out.println(fJson);

        Front frontDes = gson.fromJson(fJson, Front.class);

        String frontSer = gson.toJson(frontDes);

        Assertions.assertEquals(fJson, frontSer);

        Assertions.assertEquals(front, frontDes);

    }


    @Test
    void deserializeBack() {

        HashMap<CornerPosition, Corner> corners = new HashMap<>();

        corners.put(CornerPosition.LOWER_LEFT, new Corner(Symbol.ANIMAL));
        corners.put(CornerPosition.LOWER_RIGHT, new Corner(Symbol.FUNGI));

        HashMap<Symbol, Integer> resources = new HashMap<>();

        resources.put(Symbol.ANIMAL, 1);
        resources.put(Symbol.FUNGI, 1);

        Back back =
                new Back(
                        Color.BLUE,
                        corners,
                        resources
                );

        String bJson = gson.toJson(back);

        System.out.println(bJson);

        Back backDes = gson.fromJson(bJson, Back.class);

        String backSer = gson.toJson(backDes);

        Assertions.assertEquals(bJson, backSer);

        Assertions.assertTrue(back.equals(backDes));

        Assertions.assertEquals(back, backDes);
    }

    @Test
    void deserializeGoldenFront() {
        Map<CornerPosition, Corner> corners = new HashMap<>();

        corners.put(CornerPosition.LOWER_LEFT, new Corner(Symbol.ANIMAL));

        Map<Symbol, Integer> resources = new HashMap<>();

        resources.put(Symbol.ANIMAL, 10);

        Front f =
                new GoldenFront(
                        Color.BLUE,
                        corners,
                        0,
                        Condition.CORNERS,
                        new CalculateResources(),
                        resources
                );

        String fJson = gson.toJson(f);

        Front goldenFront = gson.fromJson(fJson, GoldenFront.class);

        String goldenFrontJson = gson.toJson(goldenFront);

        Assertions.assertEquals(fJson, goldenFrontJson);

        Assertions.assertEquals(f, goldenFront);
    }

    @Test
    void deserializePosition() {
        Position pos = new Position(1, 1);

        String posJson = gson.toJson(pos);

        System.out.println(posJson);

        Position desPosition = gson.fromJson(posJson, Position.class);

        String desInv = gson.toJson(desPosition);

        Assertions.assertEquals(posJson, desInv);

        Assertions.assertEquals(pos, desPosition);
    }

    @Test
    void deserializeObjectivePositionCard() {
        Map<Position, Color> conditions = new HashMap<>();
        conditions.put(new Position(0, 10), Color.BLUE);


        ObjectiveCard objectiveCard =
                new ObjectivePositionCard(
                        conditions,
                        10
                );

        String serJson = gson.toJson(objectiveCard);

        System.out.println(serJson);

        ObjectiveCard desObjCard = gson.fromJson(serJson, ObjectivePositionCard.class);

        String desInv = gson.toJson(desObjCard);

        Assertions.assertEquals(serJson, desInv);

        Assertions.assertEquals(objectiveCard, desObjCard);
    }

    @Test
    void deserializeObjectiveResourceCard() {
        HashMap<Symbol, Integer> conditions = new HashMap<>();

        ObjectiveCard objectiveCard =
                new ObjectiveResourceCard(
                        conditions,
                        10
                );

        String serJson = gson.toJson(objectiveCard);

        ObjectiveCard desObjCard = gson.fromJson(serJson, ObjectiveResourceCard.class);

        String desInv = gson.toJson(desObjCard);

        Assertions.assertEquals(serJson, desInv);

        Assertions.assertEquals(objectiveCard, desObjCard);
    }

    private <T> List<T> createList(int numItems, T item) {
        List<T> list = new ArrayList<>();

        for (int i = 0; i < numItems; i++) {
            list.add(item);
        }

        return list;
    }

    @Test
    void createListOfGoldenFront_maintainsAttributes() {
        List<GoldenFront> goldenFrontList = createList(2, new GoldenFront(
                Color.BLUE,
                new HashMap<>(),
                10,
                Condition.CORNERS,
                new CalculateResources(),
                new HashMap<>()
        ));

        String expectedGoldenFrontListJson = gson.toJson(goldenFrontList);

        List<GoldenFront> gotGoldenFrontList = gson.fromJson(expectedGoldenFrontListJson, new TypeToken<List<GoldenFront>>() {
        });

        String actualGoldenFrontListJson = gson.toJson(gotGoldenFrontList);

        System.out.println("GoldenCardsList json: " + actualGoldenFrontListJson);

        Assertions.assertEquals(expectedGoldenFrontListJson, actualGoldenFrontListJson);

        Assertions.assertTrue(goldenFrontList.equals(gotGoldenFrontList));
    }

    @Test
    void deserializeListOfFront() {

        Map<CornerPosition, Corner> corners = new HashMap<>();

        corners.put(CornerPosition.LOWER_LEFT, new Corner(Symbol.ANIMAL));

        List<Front> frontList = createList(2, new Front(
                Color.BLUE,
                corners,
                10,
                new CalculateNoCondition())
        );

        String expectedFrontListJson = gson.toJson(frontList);

        System.out.println("FrontList json: " + expectedFrontListJson);

        List<Front> gotFrontList = gson.fromJson(expectedFrontListJson, new TypeToken<List<Front>>() {
        });

        String actualFrontListJson = gson.toJson(gotFrontList);

        Assertions.assertEquals(expectedFrontListJson, actualFrontListJson);

        Assertions.assertTrue(frontList.equals(gotFrontList));
    }

    @Test
    void deserializeListOfBack() {

        List<Back> backList = createList(2, new Back(
                Color.BLUE,
                new HashMap<>(),
                new HashMap<>()
        ));

        String expectedBackList = gson.toJson(backList);

        System.out.println("BackList json: " + expectedBackList);

        List<Back> gotBackList = gson.fromJson(expectedBackList, new TypeToken<List<Back>>() {
        });

        String actualBackList = gson.toJson(gotBackList);

        Assertions.assertEquals(expectedBackList, actualBackList);

        Assertions.assertTrue(backList.equals(gotBackList));
    }

    @Test
    void deserializeListOfObjectivePosition() {

        List<ObjectivePositionCard> objectivePositionList = createList(2, new ObjectivePositionCard(
                new HashMap<>(),
                1
        ));

        String expectedObjectivePositionListJson = gson.toJson(objectivePositionList);

        System.out.println("ObjectivePosition List json: " + expectedObjectivePositionListJson);

        List<ObjectivePositionCard> gotObjectivePositionList = gson.fromJson(expectedObjectivePositionListJson, new TypeToken<List<ObjectivePositionCard>>() {
        });

        String actualObjectivePositionList = gson.toJson(gotObjectivePositionList);

        Assertions.assertEquals(expectedObjectivePositionListJson, actualObjectivePositionList);

        Assertions.assertTrue(objectivePositionList.equals(gotObjectivePositionList));
    }

    @Test
    void deserializeListOfObjectiveResource() {
        List<ObjectiveResourceCard> objectiveResourceList = createList(2, new ObjectiveResourceCard(
                new HashMap<>(),
                1
        ));

        String expectedObjectiveResourceListJson = gson.toJson(objectiveResourceList);

        System.out.println("ObjectiveResource List json: " + expectedObjectiveResourceListJson);

        List<ObjectiveResourceCard> gotObjectiveResourceList = gson.fromJson(expectedObjectiveResourceListJson, new TypeToken<List<ObjectiveResourceCard>>() {
        });

        String actualObjectiveResourceList = gson.toJson(gotObjectiveResourceList);

        Assertions.assertEquals(expectedObjectiveResourceListJson, actualObjectiveResourceList);

        Assertions.assertTrue(objectiveResourceList.equals(gotObjectiveResourceList));
    }


    /*
        Problem: Card c generates json j, but from j the deserialization generates a Card cc which is not equal to c.
        That's because there's no way to pass the information of the golden front when the card is deserialized.

        Solution: build a Card after deserializing both Front and Back.
     */

    //@Test
    //void createCard() {
    //    Card c = new Card(
    //            new GoldenFront(
    //                    Color.BLUE,
    //                    new HashMap<>(),
    //                    0,
    //                    Condition.CORNERS,
    //                    new CalculateResources(),
    //                    new HashMap<>()
    //            ),
    //            new Back(
    //                    Color.BLUE,
    //                    new HashMap<CornerPosition, Corner>(),
    //                    new HashMap<>()
    //            )
    //    );

    //    String cardJson = gson.toJson(c);
    //    Card cInv = gson.fromJson(cardJson, Card.class);

    //    String cInvJson = gson.toJson(cInv);

    //    Assertions.assertEquals(cardJson, cInvJson);
    //}

    //@Test
    //void createList() {
    //    List<Card> cards = new ArrayList<Card>();
    //    cards.add(
    //            new Card(
    //                    new GoldenFront(
    //                            Color.BLUE,
    //                            new HashMap<>(),
    //                            0,
    //                            Condition.CORNERS,
    //                            new CalculateResources(),
    //                            new HashMap<>()
    //                    ),
    //                    new Back(
    //                            Color.BLUE,
    //                            new HashMap<CornerPosition, Corner>(),
    //                            new HashMap<>()
    //                    )
    //            )
    //    );

    //    cards.add(
    //            new Card(
    //                    new GoldenFront(
    //                            Color.BLUE,
    //                            new HashMap<>(),
    //                            0,
    //                            Condition.CORNERS,
    //                            new CalculateResources(),
    //                            new HashMap<>()
    //                    ),
    //                    new Back(
    //                            Color.BLUE,
    //                            new HashMap<CornerPosition, Corner>(),
    //                            new HashMap<>()
    //                    )
    //            )
    //    );

    //    String listJson = gson.toJson(cards);

    //    List<Card> cardList = gson.fromJson(listJson, new TypeToken<List<Card>>() {
    //    });

    //    String listJson2 = gson.toJson(cardList);

    //    System.out.println("manually created: " + listJson);
    //    System.out.println("automatically created: " + listJson2);
    //    Assertions.assertEquals(listJson, listJson2);

    //}

    //@Test
    //void deserializeGoldenCardsFromFile() {
    //    InputStream resourceAsStream = Corner.class.getResourceAsStream(goldenCardsPath);
    //    Reader reader = new BufferedReader(new InputStreamReader(resourceAsStream));
    //    ArrayList<Card> myCards = gson.fromJson(reader, new TypeToken<>() {
    //    });
    //}

    //@Test
    //void deserializeResourceCardsFromFile() {
    //    InputStream resourceAsStream = Corner.class.getResourceAsStream(resourceCardsPath);
    //    Reader reader = new BufferedReader(new InputStreamReader(resourceAsStream));
    //    ArrayList<Card> myCards = gson.fromJson(reader, new TypeToken<>() {
    //    });
    //}

    //@Test
    //void deserializeObjectiveCardsFromFile() {
    //    InputStream objectiveResourceAsStream = Corner.class.getResourceAsStream(resourceCardsPath);
    //    Reader reader = new BufferedReader(new InputStreamReader(objectiveResourceAsStream));
    //    ArrayList<ObjectiveResourceCard> objectiveResourceCards = gson.fromJson(reader, new TypeToken<ArrayList<ObjectiveResourceCard>>() {
    //    });

    //    InputStream objectivePositionAsStream = Corner.class.getResourceAsStream(objectivePositionCardsPath);
    //    reader = new BufferedReader(new InputStreamReader(objectivePositionAsStream));
    //    ArrayList<ObjectivePositionCard> objectivePosition = gson.fromJson(reader, new TypeToken<ArrayList<ObjectivePositionCard>>() {
    //    });

    //    System.out.println(objectivePosition.getFirst());
    //}

}