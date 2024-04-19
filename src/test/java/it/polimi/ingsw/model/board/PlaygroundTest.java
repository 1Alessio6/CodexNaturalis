package it.polimi.ingsw.model.board;

import java.util.*;

import it.polimi.ingsw.model.card.*;
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

        test.placeCard(back1, new Position(0, 0));
        test.placeCard(back2, new Position(1, 1));
        test.placeCard(back3, new Position(1, -1));

        //current position we're checking
        Position pos = new Position(0, 0);

        //check tile availability
        checkTileAvailability(test, pos, Availability.OCCUPIED);

        //check if the corners are correctly covered
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, true);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, true);

        //check if the color is correct
        checkFaceColor(test, pos, Color.RED);

        pos = new Position(1, 1);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, false);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);
        checkFaceColor(test, pos, Color.RED);

        pos = new Position(1, -1);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, false);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);
        checkFaceColor(test, pos, Color.RED);

        //check available position
        List<Position> correctAvailablePosition = new ArrayList<>();
        correctAvailablePosition.add(new Position(-1, -1));
        correctAvailablePosition.add(new Position(-1, 1));
        correctAvailablePosition.add(new Position(0, 2));
        correctAvailablePosition.add(new Position(2, 2));
        correctAvailablePosition.add(new Position(2, 0));
        correctAvailablePosition.add(new Position(2, -2));
        correctAvailablePosition.add(new Position(0, -2));
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
    void placeCardTest2() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        //second test

        //create test cards
        HashMap<Symbol, Integer> resources = new HashMap<>();
        resources.put(Symbol.FUNGI, 1);
        resources.put(Symbol.PLANT, 1);

        Back starterBack = new Back(null, createBackCorners(), resources);
        Front front1 = new Front(Color.BLUE, createCorners(new Corner(Symbol.PLANT), new Corner(Symbol.ANIMAL), new Corner(Symbol.MANUSCRIPT), null), 0);
        Front front2 = new Front(Color.BLUE, createCorners(new Corner(), new Corner(Symbol.ANIMAL), null, new Corner()), 1);
        Front front3 = new Front(Color.GREEN, createCorners(new Corner(), new Corner(), new Corner(Symbol.INSECT), null), 1);

        Playground test = new Playground();

        test.placeCard(starterBack, new Position(0, 0));
        test.placeCard(front1, new Position(-1, 1));
        test.placeCard(front2, new Position(-1, -1));
        assertThrows(Playground.UnavailablePositionException.class, () -> {
            test.placeCard(front3, new Position(-2, 0));
        });

        //current position we're checking
        Position pos = new Position(0, 0);

        //check tile availability
        checkTileAvailability(test, pos, Availability.OCCUPIED);

        //check if the corners are correctly covered
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, false);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, true);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, true);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);

        //check if the color is correct
        checkFaceColor(test, pos, null);

        pos = new Position(-1, 1);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, false);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);//e' false
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);
        checkFaceColor(test, pos, Color.BLUE);

        pos = new Position(-1, -1);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, false);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);
        checkFaceColor(test, pos, Color.BLUE);

        checkResource(test, Symbol.FUNGI, 1);
        checkResource(test, Symbol.PLANT, 2);
        checkResource(test, Symbol.ANIMAL, 2);
        checkResource(test, Symbol.MANUSCRIPT, 1);
        checkResource(test, Symbol.INKWELL, 0);
        checkResource(test, Symbol.INSECT, 0);
        checkResource(test, Symbol.QUILL, 0);

        checkPoints(test, 1);
    }

    @Test
    void placeCardTest3() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {

        //create test cards
        HashMap<Symbol, Integer> resources = new HashMap<>();
        resources.put(Symbol.INSECT, 1);

        Back starterBack = new Back(null, createCorners(new Corner(), new Corner(Symbol.PLANT), new Corner(), new Corner(Symbol.INSECT)), resources);
        Front front1 = new Front(Color.RED, createCorners(new Corner(), new Corner(Symbol.FUNGI), null, new Corner()), 1);
        Front front2 = new Front(Color.BLUE, createCorners(new Corner(Symbol.PLANT), new Corner(Symbol.ANIMAL), new Corner(Symbol.MANUSCRIPT), null), 0);
        Front front3 = new Front(Color.GREEN, createCorners(null, new Corner(Symbol.INSECT), new Corner(Symbol.PLANT), new Corner(Symbol.QUILL)), 0);
        Front front4 = new Front(Color.PURPLE, createCorners(new Corner(Symbol.INSECT), new Corner(Symbol.PLANT), null, new Corner(Symbol.INKWELL)), 0);
        Front front5 = new Front(Color.GREEN, createCorners(new Corner(Symbol.FUNGI), new Corner(Symbol.PLANT), new Corner(Symbol.INKWELL), null), 0);
        Front front6 = new Front(Color.GREEN, createCorners(null, new Corner(Symbol.PLANT), new Corner(), new Corner()), 1);
        Front front7 = new Front(Color.BLUE, createCorners(new Corner(), new Corner(Symbol.ANIMAL), null, new Corner()), 1);
        //create test playground
        Playground test = new Playground();

        //place card and check the resources and points after every placing
        test.placeCard(starterBack, new Position(0, 0));

        checkResource(test, Symbol.FUNGI, 0);
        checkResource(test, Symbol.PLANT, 1);
        checkResource(test, Symbol.ANIMAL, 0);
        checkResource(test, Symbol.MANUSCRIPT, 0);
        checkResource(test, Symbol.INKWELL, 0);
        checkResource(test, Symbol.INSECT, 2);
        checkResource(test, Symbol.QUILL, 0);
        checkPoints(test, 0);

        test.placeCard(front1, new Position(1, 1));

        checkResource(test, Symbol.FUNGI, 1);
        checkResource(test, Symbol.PLANT, 0);
        checkResource(test, Symbol.ANIMAL, 0);
        checkResource(test, Symbol.MANUSCRIPT, 0);
        checkResource(test, Symbol.INKWELL, 0);
        checkResource(test, Symbol.INSECT, 2);
        checkResource(test, Symbol.QUILL, 0);
        checkPoints(test, 1);

        test.placeCard(front2, new Position(2, 2));

        checkResource(test, Symbol.FUNGI, 0);
        checkResource(test, Symbol.PLANT, 1);
        checkResource(test, Symbol.ANIMAL, 1);
        checkResource(test, Symbol.MANUSCRIPT, 1);
        checkResource(test, Symbol.INKWELL, 0);
        checkResource(test, Symbol.INSECT, 2);
        checkResource(test, Symbol.QUILL, 0);
        checkPoints(test, 1);

        test.placeCard(front3, new Position(1, 3));

        checkResource(test, Symbol.FUNGI, 0);
        checkResource(test, Symbol.PLANT, 1);
        checkResource(test, Symbol.ANIMAL, 1);
        checkResource(test, Symbol.MANUSCRIPT, 1);
        checkResource(test, Symbol.INKWELL, 0);
        checkResource(test, Symbol.INSECT, 3);
        checkResource(test, Symbol.QUILL, 1);
        checkPoints(test, 1);

        test.placeCard(front4, new Position(-1, 1));

        checkResource(test, Symbol.FUNGI, 0);
        checkResource(test, Symbol.PLANT, 2);
        checkResource(test, Symbol.ANIMAL, 1);
        checkResource(test, Symbol.MANUSCRIPT, 1);
        checkResource(test, Symbol.INKWELL, 1);
        checkResource(test, Symbol.INSECT, 4);
        checkResource(test, Symbol.QUILL, 1);
        checkPoints(test, 1);

        test.placeCard(front5, new Position(-2, 2));

        checkResource(test, Symbol.FUNGI, 1);
        checkResource(test, Symbol.PLANT, 3);
        checkResource(test, Symbol.ANIMAL, 1);
        checkResource(test, Symbol.MANUSCRIPT, 1);
        checkResource(test, Symbol.INKWELL, 2);
        checkResource(test, Symbol.INSECT, 3);
        checkResource(test, Symbol.QUILL, 1);
        checkPoints(test, 1);

        test.placeCard(front6, new Position(-1, 3));

        checkResource(test, Symbol.FUNGI, 1);
        checkResource(test, Symbol.PLANT, 3);
        checkResource(test, Symbol.ANIMAL, 1);
        checkResource(test, Symbol.MANUSCRIPT, 1);
        checkResource(test, Symbol.INKWELL, 2);
        checkResource(test, Symbol.INSECT, 3);
        checkResource(test, Symbol.QUILL, 1);
        checkPoints(test, 2);

        test.placeCard(front7, new Position(0, 2));

        checkResource(test, Symbol.FUNGI, 1);
        checkResource(test, Symbol.PLANT, 2);
        checkResource(test, Symbol.ANIMAL, 2);
        checkResource(test, Symbol.MANUSCRIPT, 1);
        checkResource(test, Symbol.INKWELL, 2);
        checkResource(test, Symbol.INSECT, 3);
        checkResource(test, Symbol.QUILL, 0);
        checkPoints(test, 3);

        //current position we're checking
        Position pos = new Position(0, 0);

        //check tile availability
        checkTileAvailability(test, pos, Availability.OCCUPIED);

        //check if the corners are correctly covered
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, true);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, true);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);

        //check if the color is correct
        checkFaceColor(test, pos, null);

        pos = new Position(1, 1);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, true);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, true);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);
        checkFaceColor(test, pos, Color.RED);

        pos = new Position(2, 2);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, false);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, true);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);
        checkFaceColor(test, pos, Color.BLUE);

        pos = new Position(1, 3);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, false);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, true);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);
        checkFaceColor(test, pos, Color.GREEN);

        pos = new Position(-1, 1);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, true);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, true);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);
        checkFaceColor(test, pos, Color.PURPLE);

        pos = new Position(-2, 2);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, true);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);
        checkFaceColor(test, pos, Color.GREEN);

        pos = new Position(-1, 3);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, false);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, true);
        checkFaceColor(test, pos, Color.GREEN);

        pos = new Position(0, 2);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, false);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);
        checkFaceColor(test, pos, Color.BLUE);


    }

    /*
    Status is true when the corner is covered otherwise is false
     */
    private void checkCoveredCorner(Playground test, Position position, CornerPosition cornerPosition, boolean status) {
        if (test.getTile(position).getFace().getCorners().containsKey(cornerPosition)) {
            assertEquals(test.getTile(position).getFace().getCorners().get(cornerPosition).isCovered(), status);
        } else {
            assertFalse(status);
        }
    }

    private void checkFaceColor(Playground test, Position position, Color c) {
        assertSame(test.getTile(position).getFace().getColor(), c);
    }

    private void checkTileAvailability(Playground test, Position position, Availability availability) {
        assertTrue(test.getTile(position).sameAvailability(availability));
    }

    private void checkPoints(Playground test, int points) {
        assertEquals(test.getPoints(), points);
    }

    private void checkResource(Playground test, Symbol resource, int expectedAmount) {
        assertEquals(test.getResources().get(resource), expectedAmount);
    }

    private void checkAvailableList(List<Position> correctList, Playground Test) {
        assertEquals(correctList.size(), Test.getAvailablePositions().size());
        assertTrue(Test.getAvailablePositions().containsAll(correctList) && correctList.containsAll(Test.getAvailablePositions()));
    }

    private Map<CornerPosition, Corner> createCorners(Corner pos1, Corner pos2, Corner pos3, Corner pos4) {
        Map<CornerPosition, Corner> corners = new HashMap<>();

        placeCorner(corners, CornerPosition.TOP_LEFT, pos1);
        placeCorner(corners, CornerPosition.TOP_RIGHT, pos2);
        placeCorner(corners, CornerPosition.LOWER_RIGHT, pos3);
        placeCorner(corners, CornerPosition.LOWER_LEFT, pos4);

        return corners;
    }

    private void placeCorner(Map<CornerPosition, Corner> map, CornerPosition pos, Corner c) {
        if (c != null) {
            map.put(pos, c);
        }
    }

    private Map<CornerPosition, Corner> createBackCorners() {
        Map<CornerPosition, Corner> corners = new HashMap<>();

        for (CornerPosition c : CornerPosition.values()) {
            corners.put(c, new Corner());
        }

        return corners;
    }
}