package it.polimi.ingsw.jsondeserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.controller.PlainGameListener;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.CardColor;
import it.polimi.ingsw.model.card.strategies.CalculateNoCondition;
import it.polimi.ingsw.model.card.strategies.CalculatePoints;
import it.polimi.ingsw.model.card.strategies.CalculateResources;
import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;
import it.polimi.ingsw.network.client.model.ClientGame;
import it.polimi.ingsw.network.client.socket.message.UpdateAfterConnectionMessage;
import it.polimi.ingsw.network.heartbeat.HeartBeatMessage;
import it.polimi.ingsw.network.server.socket.message.DrawMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test to check the correct operation of the deserializer
 */
public class JsonDeserializerTest {
    private Gson gson;
    private final static String goldenCardsPath = "/cards/goldenCards.json";
    private final static String resourceCardsPath = "/cards/resourceCards.json";
    private final static String startingCardsPath = "/cards/startingCards.json";
    private final static String objectivePositionCardsPath = "/cards/objectivePositionCards.json";
    private final static String objectiveResourceCardsPath = "/cards/objectiveResourceCards.json";

    /**
     * Creates a new Gson instance and registers the custom type adapter before each test
     */
    @BeforeEach
    public void setUp() {
        GsonBuilder builder = new GsonBuilder().enableComplexMapKeySerialization();
        builder.registerTypeAdapter(CalculatePoints.class, new InterfaceAdaptor<>());
        gson = builder.create();
    }

    /**
     * Test to check if fronts are deserialized correctly
     */
    @Test
    void deserializeFront() {

        HashMap<CornerPosition, Corner> corners = new HashMap<>();

        corners.put(CornerPosition.LOWER_LEFT, new Corner());
        corners.put(CornerPosition.LOWER_RIGHT, new Corner());

        Front front =
                new Front(
                        CardColor.BLUE,
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

    /**
     * Test to check if backs are deserialized correctly
     */
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
                        CardColor.BLUE,
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

    /**
     * Test to check if golden fronts are deserialized correctly
     */
    @Test
    void deserializeGoldenFront() {
        Map<CornerPosition, Corner> corners = new HashMap<>();

        corners.put(CornerPosition.LOWER_LEFT, new Corner(Symbol.ANIMAL));

        Map<Symbol, Integer> resources = new HashMap<>();

        resources.put(Symbol.ANIMAL, 10);

        Front f =
                new GoldenFront(
                        CardColor.BLUE,
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

    /**
     * Test to check if positions are deserialized correctly
     */
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

    /**
     * Test to check if objective position cards are deserialized correctly
     */
    @Test
    void deserializeObjectivePositionCard() {
        Map<Position, CardColor> conditions = new HashMap<>();
        conditions.put(new Position(0, 10), CardColor.BLUE);


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

    /**
     * Test to check if objective resource cards are deserialized correctly
     */
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

    /**
     * Method used to create a list of <code>T</code> items
     *
     * @param numItems the number of items of the list
     * @param item     the item
     * @param <T>      the kind of the item
     * @return a list of <code>T</code> items
     */
    private <T> List<T> createList(int numItems, T item) {
        List<T> list = new ArrayList<>();

        for (int i = 0; i < numItems; i++) {
            list.add(item);
        }

        return list;
    }

    /**
     * Test to check if a list of golden fronts maintains the same attributes after its serialization and deserialization
     */
    @Test
    void createListOfGoldenFront_maintainsAttributes() {
        List<GoldenFront> goldenFrontList = createList(2, new GoldenFront(
                CardColor.BLUE,
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

    /**
     * Test to check if a list of fronts is deserialized correctly
     */
    @Test
    void deserializeListOfFront() {

        Map<CornerPosition, Corner> corners = new HashMap<>();

        corners.put(CornerPosition.LOWER_LEFT, new Corner(Symbol.ANIMAL));

        List<Front> frontList = createList(2, new Front(
                CardColor.BLUE,
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

    /**
     * Test to check if a list of backs is deserialized correctly
     */
    @Test
    void deserializeListOfBack() {

        List<Back> backList = createList(2, new Back(
                CardColor.BLUE,
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

    /**
     * Test to check if a list of objective positions is deserialized correctly
     */
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

    /**
     * Test to check if a list of objective resources is deserialized correctly
     */
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

    /**
     * Test to check if a serialized message is deserialized correctly
     */
    @Test
    void serializeMessage_deserializeCorrectly() {
        int idToDraw = 100;
        NetworkMessage message = new DrawMessage("player", idToDraw);
        gson = new Gson();
        String json = gson.toJson(message);
        System.out.println(json);

        DrawMessage received = gson.fromJson(json, DrawMessage.class);
        Assertions.assertEquals(received.getNetworkType(), Type.DRAW);
        Assertions.assertEquals(received.getIdDraw(), idToDraw);
    }

    /**
     * Test to check if an empty client game is deserialized correctly
     */
    @Test
    void testDeserializedEmptyClientGame() {
        ClientGame clientGame = new ClientGame(new Game(createList(3, "user")));
        String json = gson.toJson(clientGame);
        System.out.println(json);
    }

    /**
     * Test to check if a non-empty client game is deserialized correctly
     */
    @Test
    void testDeserializedNonEmptyGame() {
        Game game = new Game(createList(2, "User"));
        Assertions.assertDoesNotThrow(
                () -> {
                    game.add("User", new PlainGameListener());
                }
        );
        ClientGame clientGame = new ClientGame(game);
        String json = gson.toJson(clientGame);
        System.out.println("Game in json: " + json);
        ClientGame clientGameFromJson = gson.fromJson(json, ClientGame.class);
        UpdateAfterConnectionMessage updateAfterConnectionMessage = new UpdateAfterConnectionMessage(clientGame);
        String messageJson = gson.toJson(updateAfterConnectionMessage);
        UpdateAfterConnectionMessage desMessageFromServer = gson.fromJson(messageJson, UpdateAfterConnectionMessage.class);
    }

    /**
     * Test to check if a deserialized heart beat message keeps the ID
     */
    @Test
    void deserializeHeartBeatMessage_keepId() {
        int id = 2;
        HeartBeatMessage heartBeatMessage = new HeartBeatMessage("user", id);
        String json = gson.toJson(heartBeatMessage);
        System.out.println(json);
        HeartBeatMessage heartBeatMessageFromJson = gson.fromJson(json, HeartBeatMessage.class);
        Assertions.assertEquals(id, heartBeatMessageFromJson.getId());
    }

    /**
     * Test to check if a network message deserialization works correctly
     */
    @Test
    void testDeserialization() {
        String json = """
                {"id":1,"networkType":"HEARTBEAT","sender":"handler"}""";
        NetworkMessage message = gson.fromJson(json, NetworkMessage.class);
        Assertions.assertEquals(Type.HEARTBEAT, message.getNetworkType());
        HeartBeatMessage heartBeatMessage = gson.fromJson(json, HeartBeatMessage.class);
        Assertions.assertEquals(1, heartBeatMessage.getId());
    }
}