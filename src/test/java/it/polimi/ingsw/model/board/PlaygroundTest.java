package it.polimi.ingsw.model.board;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.jsondeserializer.CornerDeserializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaygroundTest {

    @Test
    void contains() {
        Playground p = new Playground();

    }

    @Test
    void getAllPositions() {

    }

    @Test
    void getAvailablePositions() {
    }

    @Test
    void placeCard() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {

        //test place back

        Playground PlaygroundTestBack = new Playground();
        List<Card> cardsTest = createTestResourceCards("/playground/Test1.json");

        PlaygroundTestBack.placeCard(cardsTest.get(0).getFace(Side.BACK),new Position(0,0));
        PlaygroundTestBack.placeCard(cardsTest.get(1).getFace(Side.BACK),new Position(1,1));
        PlaygroundTestBack.placeCard(cardsTest.get(2).getFace(Side.BACK),new Position(1,-1));

        assert(PlaygroundTestBack.getTile(new Position(0, 0)).sameAvailability(Availability.OCCUPIED));
        assert(PlaygroundTestBack.getTile(new Position(0, 0)).getFace().getCorners().get(CornerPosition.TOP_RIGHT).isCovered());
        assert(!PlaygroundTestBack.getTile(new Position(0, 0)).getFace().getCorners().get(CornerPosition.TOP_LEFT).isCovered());
        assert(!PlaygroundTestBack.getTile(new Position(0, 0)).getFace().getCorners().get(CornerPosition.LOWER_LEFT).isCovered());
        assert(PlaygroundTestBack.getTile(new Position(0, 0)).getFace().getCorners().get(CornerPosition.LOWER_RIGHT).isCovered());
        assert(PlaygroundTestBack.getTile(new Position(0, 0)).getFace().getColor() == Color.RED);

        assert(PlaygroundTestBack.getTile(new Position(1, 1)).sameAvailability(Availability.OCCUPIED));
        assert(PlaygroundTestBack.getTile(new Position(0, 0)).getFace().getCorners().get(CornerPosition.TOP_RIGHT).isCovered());
        assert(!PlaygroundTestBack.getTile(new Position(0, 0)).getFace().getCorners().get(CornerPosition.TOP_LEFT).isCovered());
        assert(!PlaygroundTestBack.getTile(new Position(0, 0)).getFace().getCorners().get(CornerPosition.LOWER_LEFT).isCovered());
        assert(PlaygroundTestBack.getTile(new Position(0, 0)).getFace().getCorners().get(CornerPosition.LOWER_RIGHT).isCovered());
        assert(PlaygroundTestBack.getTile(new Position(0, 0)).getFace().getColor() == Color.RED);
        assert(PlaygroundTestBack.getTile(new Position(1, -1)).sameAvailability(Availability.OCCUPIED));

        assert(PlaygroundTestBack.getTile(new Position(0, 0)).getFace().getCorners().get(CornerPosition.TOP_RIGHT).isCovered());
        assert(!PlaygroundTestBack.getTile(new Position(0, 0)).getFace().getCorners().get(CornerPosition.TOP_LEFT).isCovered());
        assert(!PlaygroundTestBack.getTile(new Position(0, 0)).getFace().getCorners().get(CornerPosition.LOWER_LEFT).isCovered());
        assert(PlaygroundTestBack.getTile(new Position(0, 0)).getFace().getCorners().get(CornerPosition.LOWER_RIGHT).isCovered());
        assert(PlaygroundTestBack.getTile(new Position(0, 0)).getFace().getColor() == Color.RED);

        List<Position> correctAvailablePosition = new ArrayList<>();

        correctAvailablePosition.add(new Position(-1,-1));
        correctAvailablePosition.add(new Position(-1,1));
        correctAvailablePosition.add(new Position(0,2));
        correctAvailablePosition.add(new Position(2,2));
        correctAvailablePosition.add(new Position(2,0));
        correctAvailablePosition.add(new Position(2,-2));
        correctAvailablePosition.add(new Position(0,-2));

        checkAvailableList(correctAvailablePosition, PlaygroundTestBack);

        assert(PlaygroundTestBack.getResources().get(Symbol.FUNGI) == 3);
        assert(PlaygroundTestBack.getPoints() == 0);

        //System.out.println(PlaygroundTestBack);
        //System.out.println(PlaygroundTestBack.getAvailablePositions());

        //second test

        cardsTest = createTestResourceCards("/playground/Test2.json");
        //List<Card> StartingCardsTest = CustomJsonDeserializer.createStartingCards();

        Playground PlaygroundSecondTest = new Playground();

        PlaygroundSecondTest.placeCard(cardsTest.get(0).getFace(Side.BACK),new Position(0,0));
        System.out.println(PlaygroundSecondTest);

        PlaygroundSecondTest.placeCard(cardsTest.get(0).getFace(Side.FRONT),new Position(-1,1));
        System.out.println(PlaygroundSecondTest);

        PlaygroundSecondTest.placeCard(cardsTest.get(1).getFace(Side.FRONT),new Position(-1,-1));
        System.out.println(PlaygroundSecondTest);

        List<Card> finalCardsTest = cardsTest;
        assertThrows(Playground.UnavailablePositionException.class, () -> { PlaygroundSecondTest.placeCard(finalCardsTest.get(2).getFace(Side.FRONT),new Position(-2,0));});
        System.out.println(PlaygroundSecondTest);


    }

    private void checkAvailableList(List<Position> correctList, Playground Test){
        assertTrue(correctList.size() == Test.getAvailablePositions().size());
        assertTrue(Test.getAvailablePositions().containsAll(correctList) && correctList.containsAll(Test.getAvailablePositions()));
    }

    private List<Card> createTestResourceCards(String resourceCardsPath){
        Gson gson = new GsonBuilder().registerTypeAdapter(Corner.class, new CornerDeserializer()).create();
        List<Card> cards = new ArrayList<>();

        for (JsonElement j : getCardsFromJson(resourceCardsPath)){
            JsonObject jsonFront = j.getAsJsonObject().get("front").getAsJsonObject();
            JsonObject jsonBack = j.getAsJsonObject().get("back").getAsJsonObject();

            /* front logic */
            Color color = gson.fromJson(jsonFront.get("color"), Color.class);
            int score = gson.fromJson(jsonFront.get("score"), Integer.class);
            Map<CornerPosition, Corner> frontCorners = gson.fromJson(jsonFront.get("corners"), new TypeToken<>(){});

            /* back logic */
            Map<Symbol, Integer> backResources = gson.fromJson(jsonBack.get("resources"), new TypeToken<>(){});
            Map<CornerPosition, Corner> backCorners = new HashMap<>();
            Arrays.stream(CornerPosition.values()).forEach(cp -> backCorners.put(cp, new Corner()));

            Front front = new Front(color, frontCorners, score);
            Back back = new Back(color, backCorners, backResources);

            cards.add(new Card(front, back));
        }

        return cards;
    }

    private JsonArray getCardsFromJson(String resourcePath) throws NullPointerException {
        /* json as streams, so even after jar build it can retrieve the correct file */
        InputStream resourceAsStream = this.getClass()
                .getResourceAsStream(resourcePath);
        if (resourceAsStream == null)
            throw new NullPointerException("Empty resource!");

        Reader cardReader = new BufferedReader(new InputStreamReader(resourceAsStream));

        return JsonParser.parseReader(cardReader).getAsJsonArray();
    }
}