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

        Playground test = new Playground();

        //cards creation
        Map<Symbol, Integer> fungiResource = new HashMap<>();
        fungiResource.put(Symbol.FUNGI, 1);

        Back back1 = new Back(Color.RED, createBackCorners(), fungiResource);
        Back back2 = new Back(Color.RED, createBackCorners(), fungiResource);
        Back back3 = new Back(Color.RED, createBackCorners(), fungiResource);

        //start of Test1

        test.placeCard(back1,new Position(0,0));
        test.placeCard(back2,new Position(1,1));
        test.placeCard(back3,new Position(1,-1));

        //current position we're checking
        Position pos = new Position(0,0);

        //check tile availability
        checkTileAvailability(test,pos,Availability.OCCUPIED);

        //check if the corners are correctly covered
        checkCoveredCorner(test,pos,CornerPosition.TOP_RIGHT,true);
        checkCoveredCorner(test,pos,CornerPosition.TOP_LEFT,false);
        checkCoveredCorner(test,pos,CornerPosition.LOWER_LEFT,false);
        checkCoveredCorner(test,pos,CornerPosition.LOWER_RIGHT,true);

        //check if the color is correct
        checkFaceColor(test,pos,Color.RED);

        pos = new Position(1,1);
        checkTileAvailability(test,pos,Availability.OCCUPIED);
        checkCoveredCorner(test,pos,CornerPosition.TOP_RIGHT,false);
        checkCoveredCorner(test,pos,CornerPosition.TOP_LEFT,false);
        checkCoveredCorner(test,pos,CornerPosition.LOWER_LEFT,false);
        checkCoveredCorner(test,pos,CornerPosition.LOWER_RIGHT,false);
        checkFaceColor(test,pos,Color.RED);

        pos = new Position(1,-1);
        checkTileAvailability(test,pos,Availability.OCCUPIED);
        checkCoveredCorner(test,pos,CornerPosition.TOP_RIGHT,false);
        checkCoveredCorner(test,pos,CornerPosition.TOP_LEFT,false);
        checkCoveredCorner(test,pos,CornerPosition.LOWER_LEFT,false);
        checkCoveredCorner(test,pos,CornerPosition.LOWER_RIGHT,false);
        checkFaceColor(test,pos,Color.RED);

        //check available position
        List<Position> correctAvailablePosition = new ArrayList<>();
        correctAvailablePosition.add(new Position(-1,-1));
        correctAvailablePosition.add(new Position(-1,1));
        correctAvailablePosition.add(new Position(0,2));
        correctAvailablePosition.add(new Position(2,2));
        correctAvailablePosition.add(new Position(2,0));
        correctAvailablePosition.add(new Position(2,-2));
        correctAvailablePosition.add(new Position(0,-2));
        checkAvailableList(correctAvailablePosition, test);

        checkResource(test, Symbol.FUNGI, 3);
        checkResource(test, Symbol.PLANT, 0);
        checkResource(test, Symbol.ANIMAL, 0);
        checkResource(test, Symbol.MANUSCRIPT, 0);
        checkResource(test, Symbol.INKWELL, 0);
        checkResource(test, Symbol.INSECT, 0);
        checkResource(test, Symbol.QUILL, 0);

        checkPoints(test, 0);

    }

    @Test
    void placeCardTest2() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException{
        //second test

        //create test cards
        HashMap<Symbol,Integer> resorces = new HashMap<>();
        resorces.put(Symbol.FUNGI,1);
        resorces.put(Symbol.PLANT,1);

        Back starterBack = new Back(null, createBackCorners(),resorces);
        Front front1 = new Front(Color.BLUE,createCorners(new Corner(Symbol.PLANT), new Corner(Symbol.ANIMAL), new Corner(Symbol.MANUSCRIPT),null),0);
        Front front2 = new Front(Color.BLUE,createCorners(new Corner(), new Corner(Symbol.ANIMAL), null , new Corner()),1);
        Front front3 = new Front(Color.GREEN,createCorners(new Corner(), new Corner(), new Corner(Symbol.INSECT),null),1);

        Playground test = new Playground();

        test.placeCard(starterBack, new Position(0,0));
        test.placeCard(front1, new Position(-1,1));
        test.placeCard(front2, new Position(-1,-1));
        assertThrows(Playground.UnavailablePositionException.class, () -> { test.placeCard(front3,new Position(-2,0));});

        //current position we're checking
        Position pos = new Position(0,0);

        //check tile availability
        checkTileAvailability(test,pos,Availability.OCCUPIED);

        //check if the corners are correctly covered
        checkCoveredCorner(test,pos,CornerPosition.TOP_RIGHT,false);
        checkCoveredCorner(test,pos,CornerPosition.TOP_LEFT,true);
        checkCoveredCorner(test,pos,CornerPosition.LOWER_LEFT,true);
        checkCoveredCorner(test,pos,CornerPosition.LOWER_RIGHT,false);

        //check if the color is correct
        checkFaceColor(test,pos,null);

        pos = new Position(-1,1);
        checkTileAvailability(test,pos,Availability.OCCUPIED);
        checkCoveredCorner(test,pos,CornerPosition.TOP_RIGHT,false);
        checkCoveredCorner(test,pos,CornerPosition.TOP_LEFT,false);
        checkCoveredCorner(test,pos,CornerPosition.LOWER_LEFT,false);//e' false
        checkCoveredCorner(test,pos,CornerPosition.LOWER_RIGHT,false);
        checkFaceColor(test,pos,Color.BLUE);

        pos = new Position(-1,-1);
        checkTileAvailability(test,pos,Availability.OCCUPIED);
        checkCoveredCorner(test,pos,CornerPosition.TOP_RIGHT,false);
        checkCoveredCorner(test,pos,CornerPosition.TOP_LEFT,false);
        checkCoveredCorner(test,pos,CornerPosition.LOWER_LEFT,false);
        checkCoveredCorner(test,pos,CornerPosition.LOWER_RIGHT,false);
        checkFaceColor(test,pos,Color.BLUE);

        //List<Card> finalCardsTest = cardsTest;

        checkResource(test, Symbol.FUNGI, 1);
        checkResource(test, Symbol.PLANT, 2);
        checkResource(test, Symbol.ANIMAL, 2);
        checkResource(test, Symbol.MANUSCRIPT, 1);
        checkResource(test, Symbol.INKWELL, 0);
        checkResource(test, Symbol.INSECT, 0);
        checkResource(test, Symbol.QUILL, 0);

        checkPoints(test, 1);
    }

    /*
    Status is true when the corner is covered otherwise is false
     */
    private void checkCoveredCorner(Playground test, Position position, CornerPosition cornerPosition, boolean status){
        if(test.getTile(position).getFace().getCorners().containsKey(cornerPosition)){
            assertEquals(test.getTile(position).getFace().getCorners().get(cornerPosition).isCovered(), status);
        }
        else{
            assertFalse(status);
        }
    }

    private void checkFaceColor(Playground test, Position position, Color c){
        assertSame(test.getTile(position).getFace().getColor(), c);
    }

    private void checkTileAvailability(Playground test, Position position, Availability availability){
        assertTrue(test.getTile(position).sameAvailability(availability));
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

    private Map<CornerPosition,Corner> createCorners(Corner pos1, Corner pos2, Corner pos3, Corner pos4){
        Map<CornerPosition, Corner> corners = new HashMap<>();

        placeCorner(corners,CornerPosition.TOP_LEFT,pos1);
        placeCorner(corners,CornerPosition.TOP_RIGHT,pos2);
        placeCorner(corners,CornerPosition.LOWER_RIGHT,pos3);
        placeCorner(corners,CornerPosition.LOWER_LEFT,pos4);

        return corners;
    }

    private Map<CornerPosition, Corner> placeCorner(Map<CornerPosition,Corner> map, CornerPosition pos, Corner c){
        if(c != null){
            map.put(pos,c);
        }
        return map;
    }

    private Map<CornerPosition,Corner> createBackCorners(){
        Map<CornerPosition, Corner> corners = new HashMap<>();

        for(CornerPosition c : CornerPosition.values()){
            corners.put(c,new Corner());
        }

        return corners;
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