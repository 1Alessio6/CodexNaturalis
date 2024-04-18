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

    /*
    A simple test which simulate the situation where 3 red back are placed
    */
    @Test
    void placeCardTest1() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {

        //test place back

        Playground PlaygroundTestBack = new Playground();

        //cards creation
        Map<CornerPosition, Corner> back1Corners = new HashMap<>();
        for(CornerPosition c : CornerPosition.values()){
            back1Corners.put(c,new Corner());
        }

        Map<CornerPosition, Corner> back2Corners = new HashMap<>();
        for(CornerPosition c : CornerPosition.values()){
            back2Corners.put(c,new Corner());
        }

        Map<CornerPosition, Corner> back3Corners = new HashMap<>();
        for(CornerPosition c : CornerPosition.values()){
            back3Corners.put(c,new Corner());
        }

        Map<Symbol, Integer> fungiResource = new HashMap<>();
        fungiResource.put(Symbol.FUNGI, 1);

        Back back1 = new Back(Color.RED, back1Corners, fungiResource);
        Back back2 = new Back(Color.RED, back2Corners, fungiResource);
        Back back3 = new Back(Color.RED, back3Corners, fungiResource);

        //start of Test1

        PlaygroundTestBack.placeCard(back1,new Position(0,0));
        PlaygroundTestBack.placeCard(back2,new Position(1,1));
        PlaygroundTestBack.placeCard(back3,new Position(1,-1));

        //current position we're checking
        Position pos = new Position(0,0);

        //check tile availability
        checkTileAvailability(PlaygroundTestBack,pos,Availability.OCCUPIED);

        //check if the corners are correctly covered
        checkCoveredCorner(PlaygroundTestBack,pos,CornerPosition.TOP_RIGHT,true);
        checkCoveredCorner(PlaygroundTestBack,pos,CornerPosition.TOP_LEFT,false);
        checkCoveredCorner(PlaygroundTestBack,pos,CornerPosition.LOWER_LEFT,false);
        checkCoveredCorner(PlaygroundTestBack,pos,CornerPosition.LOWER_RIGHT,true);

        //check if the color is correct
        checkFaceColor(PlaygroundTestBack,pos,Color.RED);

        pos = new Position(1,1);
        checkTileAvailability(PlaygroundTestBack,pos,Availability.OCCUPIED);
        checkCoveredCorner(PlaygroundTestBack,pos,CornerPosition.TOP_RIGHT,false);
        checkCoveredCorner(PlaygroundTestBack,pos,CornerPosition.TOP_LEFT,false);
        checkCoveredCorner(PlaygroundTestBack,pos,CornerPosition.LOWER_LEFT,false);
        checkCoveredCorner(PlaygroundTestBack,pos,CornerPosition.LOWER_RIGHT,false);
        checkFaceColor(PlaygroundTestBack,pos,Color.RED);

        pos = new Position(1,-1);
        checkTileAvailability(PlaygroundTestBack,pos,Availability.OCCUPIED);
        checkCoveredCorner(PlaygroundTestBack,pos,CornerPosition.TOP_RIGHT,false);
        checkCoveredCorner(PlaygroundTestBack,pos,CornerPosition.TOP_LEFT,false);
        checkCoveredCorner(PlaygroundTestBack,pos,CornerPosition.LOWER_LEFT,false);
        checkCoveredCorner(PlaygroundTestBack,pos,CornerPosition.LOWER_RIGHT,false);
        checkFaceColor(PlaygroundTestBack,pos,Color.RED);

        //check available position
        List<Position> correctAvailablePosition = new ArrayList<>();
        correctAvailablePosition.add(new Position(-1,-1));
        correctAvailablePosition.add(new Position(-1,1));
        correctAvailablePosition.add(new Position(0,2));
        correctAvailablePosition.add(new Position(2,2));
        correctAvailablePosition.add(new Position(2,0));
        correctAvailablePosition.add(new Position(2,-2));
        correctAvailablePosition.add(new Position(0,-2));
        checkAvailableList(correctAvailablePosition, PlaygroundTestBack);

        checkResource(PlaygroundTestBack, Symbol.FUNGI, 3);
        checkPoints(PlaygroundTestBack, 0);

        /*
        System.out.println(PlaygroundTestBack);
        System.out.println(PlaygroundTestBack.getAvailablePositions());
        */

    }

    @Test
    void placeCardTest2() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException{
        //second test

        List<Card> cardsTest = createTestResourceCards("/playground/Test2.json");
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

    /*
    Status is true when the corner is covered otherwise is false
     */
    private void checkCoveredCorner(Playground test, Position position, CornerPosition cornerPosition, boolean status){
        assertEquals(test.getTile(position).getFace().getCorners().get(cornerPosition).isCovered(), status);
    }

    private void checkFaceColor(Playground test, Position position, Color c){
        assertSame(test.getTile(position).getFace().getColor(), c);
    }

    private void checkTileAvailability(Playground test, Position position, Availability availability){
        assertTrue(test.getTile(new Position(1, 1)).sameAvailability(availability));
    }

    private void checkPoints(Playground test, int points){
        assertEquals(test.getPoints(),points);
    }

    private void checkResource(Playground test, Symbol resource, int expectedAmount){
        assertEquals(test.getResources().get(resource),expectedAmount);
    }

    private void checkAvailableList(List<Position> correctList, Playground Test){
        assertEquals(correctList.size(), Test.getAvailablePositions().size());
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