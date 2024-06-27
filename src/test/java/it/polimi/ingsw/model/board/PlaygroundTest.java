package it.polimi.ingsw.model.board;

import java.util.*;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.color.CardColor;
import it.polimi.ingsw.model.card.strategies.CalculateCorners;
import it.polimi.ingsw.model.card.strategies.CalculateResources;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to check the correct functioning of the <code>Playground</code>
 */
class PlaygroundTest {

    @Test
    void getAllPositions() {

    }

    @Test
    void getAvailablePositions() {
    }

    /**
     * A simple test which simulate the situation where 3 red back are placed
     *
     * @throws Playground.UnavailablePositionException if the position is unavailable
     * @throws Playground.NotEnoughResourcesException  if the player's resource are not enough to place a card
     */
    @Test
    void placeCardTest1() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {

        //test place back

        Playground test = new Playground();

        //cards creation
        Map<Symbol, Integer> fungiResource = new HashMap<>();
        fungiResource.put(Symbol.FUNGI, 1);

        Back back1 = new Back(CardColor.RED, createBackCorners(), fungiResource);
        Back back2 = new Back(CardColor.RED, createBackCorners(), fungiResource);
        Back back3 = new Back(CardColor.RED, createBackCorners(), fungiResource);

        //start of Test1

        test.placeCard(back1, new Position(0, 0));

        checkTileAvailability(test, new Position(1,1), Availability.EMPTY);
        checkTileAvailability(test, new Position(-1,1), Availability.EMPTY);
        checkTileAvailability(test, new Position(-1,-1), Availability.EMPTY);
        checkTileAvailability(test, new Position(1,-1), Availability.EMPTY);

        test.placeCard(back2, new Position(1, 1));

        checkTileAvailability(test, new Position(0,0), Availability.OCCUPIED);
        checkTileAvailability(test, new Position(2,0), Availability.EMPTY);
        checkTileAvailability(test, new Position(0,2), Availability.EMPTY);
        checkTileAvailability(test, new Position(2,2), Availability.EMPTY);

        test.placeCard(back3, new Position(1, -1));

        checkTileAvailability(test, new Position(0,0), Availability.OCCUPIED);
        checkTileAvailability(test, new Position(2,0), Availability.EMPTY);
        checkTileAvailability(test, new Position(0,-2), Availability.EMPTY);
        checkTileAvailability(test, new Position(2,-2), Availability.EMPTY);

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
        checkFaceStatus(test, pos, back1);

        pos = new Position(1, 1);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, false);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);
        checkFaceStatus(test, pos, back2);

        pos = new Position(1, -1);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, false);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);
        checkFaceStatus(test, pos, back3);

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

    /**
     * A test to check the correct placement of 3 different front cards
     *
     * @throws Playground.UnavailablePositionException if the position is unavailable
     * @throws Playground.NotEnoughResourcesException  if the player's resource are not enough to place a card
     */
    @Test
    void placeCardTest2() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        //second test

        //create test cards
        HashMap<Symbol, Integer> resources = new HashMap<>();
        resources.put(Symbol.FUNGI, 1);
        resources.put(Symbol.PLANT, 1);

        Back starterBack = new Back(null, createBackCorners(), resources);
        Front front1 = new Front(CardColor.BLUE, createCorners(new Corner(Symbol.PLANT), new Corner(Symbol.ANIMAL), new Corner(Symbol.MANUSCRIPT), null), 0);
        Front front2 = new Front(CardColor.BLUE, createCorners(new Corner(), new Corner(Symbol.ANIMAL), null, new Corner()), 1);
        Front front3 = new Front(CardColor.GREEN, createCorners(new Corner(), new Corner(), new Corner(Symbol.INSECT), null), 1);

        Playground test = new Playground();

        test.placeCard(starterBack, new Position(0, 0));

        checkTileAvailability(test, new Position(1,1), Availability.EMPTY);
        checkTileAvailability(test, new Position(-1,1), Availability.EMPTY);
        checkTileAvailability(test, new Position(-1,-1), Availability.EMPTY);
        checkTileAvailability(test, new Position(1,-1), Availability.EMPTY);

        test.placeCard(front1, new Position(-1, 1));

        checkTileAvailability(test, new Position(0,0), Availability.OCCUPIED);
        checkTileAvailability(test, new Position(-2,0), Availability.NOTAVAILABLE);
        checkTileAvailability(test, new Position(0,2), Availability.EMPTY);
        checkTileAvailability(test, new Position(-2,2), Availability.EMPTY);

        test.placeCard(front2, new Position(-1, -1));

        checkTileAvailability(test, new Position(0,0), Availability.OCCUPIED);
        checkTileAvailability(test, new Position(-2,0), Availability.NOTAVAILABLE);
        checkTileAvailability(test, new Position(0,-2), Availability.NOTAVAILABLE);
        checkTileAvailability(test, new Position(-2,-2), Availability.EMPTY);

        checkThrowsUnavailablePositionException(test, front3, new Position(-2, 0));

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
        checkFaceStatus(test, pos, starterBack);

        pos = new Position(-1, 1);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, false);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);
        checkFaceStatus(test, pos, front1);

        pos = new Position(-1, -1);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, false);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);
        checkFaceStatus(test, pos, front2);

        checkResource(test, Symbol.FUNGI, 1);
        checkResource(test, Symbol.PLANT, 2);
        checkResource(test, Symbol.ANIMAL, 2);
        checkResource(test, Symbol.MANUSCRIPT, 1);
        checkResource(test, Symbol.INKWELL, 0);
        checkResource(test, Symbol.INSECT, 0);
        checkResource(test, Symbol.QUILL, 0);

        checkPoints(test, 1);
    }

    /**
     * Test to check the correct placement of 7 different front cards
     *
     * @throws Playground.UnavailablePositionException if the position is unavailable
     * @throws Playground.NotEnoughResourcesException  if the player's resource are not enough to place a card
     */
    @Test
    void placeCardTest3() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {

        //create test cards
        HashMap<Symbol, Integer> resources = new HashMap<>();
        resources.put(Symbol.INSECT, 1);

        Back starterBack = new Back(null, createCorners(new Corner(), new Corner(Symbol.PLANT), new Corner(), new Corner(Symbol.INSECT)), resources);
        Front front1 = new Front(CardColor.RED, createCorners(new Corner(), new Corner(Symbol.FUNGI), null, new Corner()), 1);
        Front front2 = new Front(CardColor.BLUE, createCorners(new Corner(Symbol.PLANT), new Corner(Symbol.ANIMAL), new Corner(Symbol.MANUSCRIPT), null), 0);
        Front front3 = new Front(CardColor.GREEN, createCorners(null, new Corner(Symbol.INSECT), new Corner(Symbol.PLANT), new Corner(Symbol.QUILL)), 0);
        Front front4 = new Front(CardColor.PURPLE, createCorners(new Corner(Symbol.INSECT), new Corner(Symbol.PLANT), null, new Corner(Symbol.INKWELL)), 0);
        Front front5 = new Front(CardColor.GREEN, createCorners(new Corner(Symbol.FUNGI), new Corner(Symbol.PLANT), new Corner(Symbol.INKWELL), null), 0);
        Front front6 = new Front(CardColor.GREEN, createCorners(null, new Corner(Symbol.PLANT), new Corner(), new Corner()), 1);
        Front front7 = new Front(CardColor.BLUE, createCorners(new Corner(), new Corner(Symbol.ANIMAL), null, new Corner()), 1);
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

        //checks the tiles created

        checkTileAvailability(test, new Position(1,1), Availability.EMPTY);
        checkTileAvailability(test, new Position(-1,1), Availability.EMPTY);
        checkTileAvailability(test, new Position(-1,-1), Availability.EMPTY);
        checkTileAvailability(test, new Position(1,-1), Availability.EMPTY);

        test.placeCard(front1, new Position(1, 1));

        checkResource(test, Symbol.FUNGI, 1);
        checkResource(test, Symbol.PLANT, 0);
        checkResource(test, Symbol.ANIMAL, 0);
        checkResource(test, Symbol.MANUSCRIPT, 0);
        checkResource(test, Symbol.INKWELL, 0);
        checkResource(test, Symbol.INSECT, 2);
        checkResource(test, Symbol.QUILL, 0);
        checkPoints(test, 1);
        checkTileAvailability(test, new Position(0,0), Availability.OCCUPIED);
        checkTileAvailability(test, new Position(2,0), Availability.NOTAVAILABLE);
        checkTileAvailability(test, new Position(0,2), Availability.EMPTY);
        checkTileAvailability(test, new Position(2,2), Availability.EMPTY);

        test.placeCard(front2, new Position(2, 2));

        checkResource(test, Symbol.FUNGI, 0);
        checkResource(test, Symbol.PLANT, 1);
        checkResource(test, Symbol.ANIMAL, 1);
        checkResource(test, Symbol.MANUSCRIPT, 1);
        checkResource(test, Symbol.INKWELL, 0);
        checkResource(test, Symbol.INSECT, 2);
        checkResource(test, Symbol.QUILL, 0);
        checkPoints(test, 1);
        checkTileAvailability(test, new Position(1,1), Availability.OCCUPIED);
        checkTileAvailability(test, new Position(3,1), Availability.EMPTY);
        checkTileAvailability(test, new Position(1,3), Availability.EMPTY);
        checkTileAvailability(test, new Position(3,3), Availability.EMPTY);

        test.placeCard(front3, new Position(1, 3));

        checkResource(test, Symbol.FUNGI, 0);
        checkResource(test, Symbol.PLANT, 1);
        checkResource(test, Symbol.ANIMAL, 1);
        checkResource(test, Symbol.MANUSCRIPT, 1);
        checkResource(test, Symbol.INKWELL, 0);
        checkResource(test, Symbol.INSECT, 3);
        checkResource(test, Symbol.QUILL, 1);
        checkPoints(test, 1);
        checkTileAvailability(test, new Position(2,2), Availability.OCCUPIED);
        checkTileAvailability(test, new Position(2,4), Availability.EMPTY);
        checkTileAvailability(test, new Position(0,2), Availability.EMPTY);
        checkTileAvailability(test, new Position(0,4), Availability.NOTAVAILABLE);

        test.placeCard(front4, new Position(-1, 1));

        checkResource(test, Symbol.FUNGI, 0);
        checkResource(test, Symbol.PLANT, 2);
        checkResource(test, Symbol.ANIMAL, 1);
        checkResource(test, Symbol.MANUSCRIPT, 1);
        checkResource(test, Symbol.INKWELL, 1);
        checkResource(test, Symbol.INSECT, 4);
        checkResource(test, Symbol.QUILL, 1);
        checkPoints(test, 1);
        checkTileAvailability(test, new Position(0,0), Availability.OCCUPIED);
        checkTileAvailability(test, new Position(-2,0), Availability.EMPTY);
        checkTileAvailability(test, new Position(0,2), Availability.EMPTY);
        checkTileAvailability(test, new Position(-2,2), Availability.EMPTY);

        test.placeCard(front5, new Position(-2, 2));

        checkResource(test, Symbol.FUNGI, 1);
        checkResource(test, Symbol.PLANT, 3);
        checkResource(test, Symbol.ANIMAL, 1);
        checkResource(test, Symbol.MANUSCRIPT, 1);
        checkResource(test, Symbol.INKWELL, 2);
        checkResource(test, Symbol.INSECT, 3);
        checkResource(test, Symbol.QUILL, 1);
        checkPoints(test, 1);
        checkTileAvailability(test, new Position(-1,1), Availability.OCCUPIED);
        checkTileAvailability(test, new Position(-3,3), Availability.EMPTY);
        checkTileAvailability(test, new Position(-3,1), Availability.NOTAVAILABLE);
        checkTileAvailability(test, new Position(-1,3), Availability.EMPTY);

        test.placeCard(front6, new Position(-1, 3));

        checkResource(test, Symbol.FUNGI, 1);
        checkResource(test, Symbol.PLANT, 3);
        checkResource(test, Symbol.ANIMAL, 1);
        checkResource(test, Symbol.MANUSCRIPT, 1);
        checkResource(test, Symbol.INKWELL, 2);
        checkResource(test, Symbol.INSECT, 3);
        checkResource(test, Symbol.QUILL, 1);
        checkPoints(test, 2);
        checkTileAvailability(test, new Position(-2,2), Availability.OCCUPIED);
        checkTileAvailability(test, new Position(0,2), Availability.EMPTY);
        checkTileAvailability(test, new Position(0,4), Availability.NOTAVAILABLE);
        checkTileAvailability(test, new Position(-2,4), Availability.NOTAVAILABLE);

        test.placeCard(front7, new Position(0, 2));

        checkResource(test, Symbol.FUNGI, 1);
        checkResource(test, Symbol.PLANT, 2);
        checkResource(test, Symbol.ANIMAL, 2);
        checkResource(test, Symbol.MANUSCRIPT, 1);
        checkResource(test, Symbol.INKWELL, 2);
        checkResource(test, Symbol.INSECT, 3);
        checkResource(test, Symbol.QUILL, 0);
        checkPoints(test, 3);
        //checking the adjacent position it's not necessary it's checked in the next code portion

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
        checkFaceStatus(test, pos, starterBack);

        pos = new Position(1, 1);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, true);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, true);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);
        checkFaceStatus(test, pos, front1);

        pos = new Position(2, 2);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, false);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, true);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);
        checkFaceStatus(test, pos, front2);

        pos = new Position(1, 3);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, false);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, true);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);
        checkFaceStatus(test, pos, front3);

        pos = new Position(-1, 1);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, true);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, true);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);
        checkFaceStatus(test, pos, front4);

        pos = new Position(-2, 2);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, true);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);
        checkFaceStatus(test, pos, front5);

        pos = new Position(-1, 3);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, false);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, true);
        checkFaceStatus(test, pos, front6);

        pos = new Position(0, 2);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, false);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);
        checkFaceStatus(test, pos, front7);
    }

    /**
     * Test to check the correct placement of 6 different fronts and 4 golden fronts
     *
     * @throws Playground.UnavailablePositionException if the position is unavailable
     * @throws Playground.NotEnoughResourcesException  if the player's resource are not enough to place a card
     */
    @Test
    void placeCardTest4() throws Playground.UnavailablePositionException, Playground.NotEnoughResourcesException{
        HashMap<Symbol, Integer> resources = new HashMap<>();
        resources.put(Symbol.INSECT, 1);

        Back starterBack = new Back(null, createCorners(new Corner(), new Corner(Symbol.PLANT), new Corner(), new Corner(Symbol.INSECT)), resources);
        Front front1 = new Front(CardColor.RED, createCorners(new Corner(), new Corner(Symbol.FUNGI), null, new Corner()), 1);
        Front front2 = new Front(CardColor.BLUE, createCorners(new Corner(Symbol.PLANT), new Corner(Symbol.ANIMAL), new Corner(Symbol.MANUSCRIPT), null), 0);
        Front front3 = new Front(CardColor.GREEN, createCorners(null, new Corner(Symbol.INSECT), new Corner(Symbol.PLANT), new Corner(Symbol.QUILL)), 0);
        Front front4 = new Front(CardColor.PURPLE, createCorners(new Corner(Symbol.INSECT), new Corner(Symbol.PLANT), null, new Corner(Symbol.INKWELL)), 0);
        Front front5 = new Front(CardColor.GREEN, createCorners(new Corner(Symbol.FUNGI), new Corner(Symbol.PLANT), new Corner(Symbol.INKWELL), null), 0);
        Front front6 = new Front(CardColor.GREEN, createCorners(null, new Corner(Symbol.PLANT), new Corner(), new Corner()),1);

        HashMap<Symbol, Integer> requirements1 = new HashMap<>();
        requirements1.put(Symbol.INSECT, 3);
        requirements1.put(Symbol.ANIMAL, 1);

        GoldenFront goldenfront1 = new GoldenFront(CardColor.PURPLE, createCorners(new Corner(), new Corner(), new Corner(), null), 2, Condition.CORNERS, new CalculateCorners(), requirements1);

        HashMap<Symbol, Integer> requirements2 = new HashMap<>();
        requirements2.put(Symbol.INSECT, 1);
        requirements2.put(Symbol.ANIMAL, 3);
        GoldenFront goldenfront2 = new GoldenFront(CardColor.BLUE, createCorners(new Corner(), new Corner(), new Corner(), null), 2, Condition.CORNERS, new CalculateCorners(), requirements2);
        HashMap<Symbol, Integer> requirements3 = new HashMap<>();
        requirements3.put(Symbol.INSECT, 2);
        requirements3.put(Symbol.FUNGI, 1);
        GoldenFront goldenfront3 = new GoldenFront(CardColor.PURPLE, createCorners(null, new Corner(), new Corner(Symbol.INKWELL), new Corner()), 1, Condition.NUM_INKWELL, new CalculateResources(), requirements3);
        HashMap<Symbol, Integer> requirements4 = new HashMap<>();
        requirements4.put(Symbol.INSECT, 2);
        requirements4.put(Symbol.ANIMAL, 1);
        GoldenFront goldenfront4 = new GoldenFront(CardColor.PURPLE, createCorners(new Corner(), null, new Corner(), new Corner(Symbol.MANUSCRIPT)), 1, Condition.NUM_MANUSCRIPT, new CalculateResources(), requirements4);

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

        //checks the tiles created

        checkTileAvailability(test, new Position(1,1), Availability.EMPTY);
        checkTileAvailability(test, new Position(-1,1), Availability.EMPTY);
        checkTileAvailability(test, new Position(-1,-1), Availability.EMPTY);
        checkTileAvailability(test, new Position(1,-1), Availability.EMPTY);

        test.placeCard(front1, new Position(1, 1));

        checkResource(test, Symbol.FUNGI, 1);
        checkResource(test, Symbol.PLANT, 0);
        checkResource(test, Symbol.ANIMAL, 0);
        checkResource(test, Symbol.MANUSCRIPT, 0);
        checkResource(test, Symbol.INKWELL, 0);
        checkResource(test, Symbol.INSECT, 2);
        checkResource(test, Symbol.QUILL, 0);
        checkPoints(test, 1);
        checkTileAvailability(test, new Position(0,0), Availability.OCCUPIED);
        checkTileAvailability(test, new Position(2,0), Availability.NOTAVAILABLE);
        checkTileAvailability(test, new Position(0,2), Availability.EMPTY);
        checkTileAvailability(test, new Position(2,2), Availability.EMPTY);

        test.placeCard(front2, new Position(2, 2));

        checkResource(test, Symbol.FUNGI, 0);
        checkResource(test, Symbol.PLANT, 1);
        checkResource(test, Symbol.ANIMAL, 1);
        checkResource(test, Symbol.MANUSCRIPT, 1);
        checkResource(test, Symbol.INKWELL, 0);
        checkResource(test, Symbol.INSECT, 2);
        checkResource(test, Symbol.QUILL, 0);
        checkPoints(test, 1);
        checkTileAvailability(test, new Position(1,1), Availability.OCCUPIED);
        checkTileAvailability(test, new Position(3,1), Availability.EMPTY);
        checkTileAvailability(test, new Position(1,3), Availability.EMPTY);
        checkTileAvailability(test, new Position(3,3), Availability.EMPTY);

        test.placeCard(front3, new Position(1, 3));

        checkResource(test, Symbol.FUNGI, 0);
        checkResource(test, Symbol.PLANT, 1);
        checkResource(test, Symbol.ANIMAL, 1);
        checkResource(test, Symbol.MANUSCRIPT, 1);
        checkResource(test, Symbol.INKWELL, 0);
        checkResource(test, Symbol.INSECT, 3);
        checkResource(test, Symbol.QUILL, 1);
        checkPoints(test, 1);
        checkTileAvailability(test, new Position(2,2), Availability.OCCUPIED);
        checkTileAvailability(test, new Position(2,4), Availability.EMPTY);
        checkTileAvailability(test, new Position(0,2), Availability.EMPTY);
        checkTileAvailability(test, new Position(0,4), Availability.NOTAVAILABLE);

        test.placeCard(front4, new Position(-1, 1));

        checkResource(test, Symbol.FUNGI, 0);
        checkResource(test, Symbol.PLANT, 2);
        checkResource(test, Symbol.ANIMAL, 1);
        checkResource(test, Symbol.MANUSCRIPT, 1);
        checkResource(test, Symbol.INKWELL, 1);
        checkResource(test, Symbol.INSECT, 4);
        checkResource(test, Symbol.QUILL, 1);
        checkPoints(test, 1);
        checkTileAvailability(test, new Position(0,0), Availability.OCCUPIED);
        checkTileAvailability(test, new Position(-2,0), Availability.EMPTY);
        checkTileAvailability(test, new Position(0,2), Availability.EMPTY);
        checkTileAvailability(test, new Position(-2,2), Availability.EMPTY);

        test.placeCard(front5, new Position(-2, 2));

        checkResource(test, Symbol.FUNGI, 1);
        checkResource(test, Symbol.PLANT, 3);
        checkResource(test, Symbol.ANIMAL, 1);
        checkResource(test, Symbol.MANUSCRIPT, 1);
        checkResource(test, Symbol.INKWELL, 2);
        checkResource(test, Symbol.INSECT, 3);
        checkResource(test, Symbol.QUILL, 1);
        checkPoints(test, 1);
        checkTileAvailability(test, new Position(-1,1), Availability.OCCUPIED);
        checkTileAvailability(test, new Position(-3,3), Availability.EMPTY);
        checkTileAvailability(test, new Position(-3,1), Availability.NOTAVAILABLE);
        checkTileAvailability(test, new Position(-1,3), Availability.EMPTY);

        test.placeCard(front6, new Position(-1, 3));

        checkResource(test, Symbol.FUNGI, 1);
        checkResource(test, Symbol.PLANT, 3);
        checkResource(test, Symbol.ANIMAL, 1);
        checkResource(test, Symbol.MANUSCRIPT, 1);
        checkResource(test, Symbol.INKWELL, 2);
        checkResource(test, Symbol.INSECT, 3);
        checkResource(test, Symbol.QUILL, 1);
        checkPoints(test, 2);
        checkTileAvailability(test, new Position(-2,2), Availability.OCCUPIED);
        checkTileAvailability(test, new Position(0,2), Availability.EMPTY);
        checkTileAvailability(test, new Position(0,4), Availability.NOTAVAILABLE);
        checkTileAvailability(test, new Position(-2,4), Availability.NOTAVAILABLE);

        test.placeCard(goldenfront1, new Position(0, 2));

        checkResource(test, Symbol.FUNGI, 1);
        checkResource(test, Symbol.PLANT, 2);
        checkResource(test, Symbol.ANIMAL, 1);
        checkResource(test, Symbol.MANUSCRIPT, 1);
        checkResource(test, Symbol.INKWELL, 2);
        checkResource(test, Symbol.INSECT, 3);
        checkResource(test, Symbol.QUILL, 0);
        checkPoints(test, 10);

        //check if exception is correctly thrown, goldenfront2 requires 3 ANIMAl but the playground has only 1
        checkThrowsNotEnoughResourcesException(test, goldenfront2);

        test.placeCard(goldenfront3, new Position(1,-1));

        checkResource(test, Symbol.FUNGI, 1);
        checkResource(test, Symbol.PLANT, 2);
        checkResource(test, Symbol.ANIMAL, 1);
        checkResource(test, Symbol.MANUSCRIPT, 1);
        checkResource(test, Symbol.INKWELL, 3);
        checkResource(test, Symbol.INSECT, 3);
        checkResource(test, Symbol.QUILL, 0);
        checkPoints(test, 13);
        checkTileAvailability(test, new Position(0,0), Availability.OCCUPIED);
        checkTileAvailability(test, new Position(2,-2), Availability.EMPTY);
        checkTileAvailability(test, new Position(2,0), Availability.NOTAVAILABLE);
        checkTileAvailability(test, new Position(0,-2), Availability.EMPTY);

        test.placeCard(goldenfront4, new Position(3, 1));

        checkResource(test, Symbol.FUNGI, 1);
        checkResource(test, Symbol.PLANT, 2);
        checkResource(test, Symbol.ANIMAL, 1);
        checkResource(test, Symbol.MANUSCRIPT, 1);
        checkResource(test, Symbol.INKWELL, 3);
        checkResource(test, Symbol.INSECT, 3);
        checkResource(test, Symbol.QUILL, 0);
        checkPoints(test, 14);
        checkTileAvailability(test, new Position(2,2), Availability.OCCUPIED);
        checkTileAvailability(test, new Position(2,0), Availability.NOTAVAILABLE);
        checkTileAvailability(test, new Position(4,0), Availability.EMPTY);
        checkTileAvailability(test, new Position(4,2), Availability.NOTAVAILABLE);

        //current position we're checking
        Position pos = new Position(0, 0);

        //check tile availability
        checkTileAvailability(test, pos, Availability.OCCUPIED);

        //check if the corners are correctly covered
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, true);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, true);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, true);

        //check if the color is correct
        checkFaceStatus(test, pos, starterBack);

        pos = new Position(1, 1);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, true);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, true);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);
        checkFaceStatus(test, pos, front1);

        pos = new Position(2, 2);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, false);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, true);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, true);
        checkFaceStatus(test, pos, front2);

        pos = new Position(1, 3);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, false);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, true);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);
        checkFaceStatus(test, pos, front3);

        pos = new Position(-1, 1);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, true);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, true);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);
        checkFaceStatus(test, pos, front4);

        pos = new Position(-2, 2);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, true);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);
        checkFaceStatus(test, pos, front5);

        pos = new Position(-1, 3);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, false);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, true);
        checkFaceStatus(test, pos, front6);

        pos = new Position(0, 2);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, false);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);
        checkFaceStatus(test, pos, goldenfront1);

        pos = new Position(1, -1);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, false);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);
        checkFaceStatus(test, pos, goldenfront3);

        pos = new Position(3, 1);
        checkTileAvailability(test, pos, Availability.OCCUPIED);
        checkCoveredCorner(test, pos, CornerPosition.TOP_RIGHT, false);
        checkCoveredCorner(test, pos, CornerPosition.TOP_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_LEFT, false);
        checkCoveredCorner(test, pos, CornerPosition.LOWER_RIGHT, false);
        checkFaceStatus(test, pos, goldenfront4);
    }

    /**
     * Method used to check if a corner is covered or not.
     * <p>
     * Status is true when the corner is covered otherwise is false
     *
     * @param test           the playground on which the check is made
     * @param position       the position of the card
     * @param cornerPosition the position of the corner
     * @param status         the status of the corner
     */
    private void checkCoveredCorner(Playground test, Position position, CornerPosition cornerPosition, boolean status) {
        if (test.getTile(position).getFace().getCorners().containsKey(cornerPosition)) {
            assertEquals(test.getTile(position).getFace().getCorners().get(cornerPosition).isCovered(), status);
        } else {
            assertFalse(status);
        }
    }

    /**
     * Method used to check if the color of the <B>card</B> <code>c</code> is correct
     *
     * @param test     the playground on which the check is made
     * @param position the position of the card
     * @param c        the card whose color is to be checked
     */
    private void checkFaceColor(Playground test, Position position, CardColor c) {
        assertSame(test.getTile(position).getFace().getColor(), c);
    }

    /**
     * Method used to check if the color of <code>previousFace</code> is correct
     *
     * @param test         the playground on which the check is made
     * @param position     the position of the card
     * @param previousFace previous face represents the face before its placement
     */
    private void checkFaceStatus(Playground test, Position position, Face previousFace) {
        checkFaceColor(test, position, previousFace.getColor());
        for (CornerPosition c : previousFace.getCorners().keySet()) {
            assertEquals(previousFace.getCorners().get(c).getSymbol(), test.getTile(position).getFace().getCorners().get(c).getSymbol());
        }
        assertEquals(previousFace.getScore(), test.getTile(position).getFace().getScore());
        assertEquals(previousFace.getResources(), test.getTile(position).getFace().getResources());
        assertEquals(previousFace, test.getTile(position).getFace());

    }

    /**
     * Method used to check the availability of a tile
     *
     * @param test         the playground on which the check is made
     * @param position     the position of the card
     * @param availability the availability of the tile
     */
    private void checkTileAvailability(Playground test, Position position, Availability availability) {
        assertTrue(test.getTile(position).sameAvailability(availability));
    }

    /**
     * Method used to check the points present in the <code>test</code>
     *
     * @param test   the playground on which the check is made
     * @param points the points to be checked
     */
    private void checkPoints(Playground test, int points) {
        assertEquals(test.getPoints(), points);
    }

    /**
     * Method used to check the resources present in the <code>test</code>
     *
     * @param test           the playground on which the check is made
     * @param resource       the resource to be checked
     * @param expectedAmount the amount of resources expected
     */
    private void checkResource(Playground test, Symbol resource, int expectedAmount) {
        assertEquals(test.getResources().get(resource), expectedAmount);
    }

    /**
     * Method used to check the availability of the <code>correctList</code> provided
     *
     * @param correctList the list of positions to check
     * @param Test        the playground on which the check is made
     */
    private void checkAvailableList(List<Position> correctList, Playground Test) {
        assertEquals(correctList.size(), Test.getAvailablePositions().size());
        assertTrue(Test.getAvailablePositions().containsAll(correctList) && correctList.containsAll(Test.getAvailablePositions()));
    }

    /**
     * Method used to check if a placement throws an <code>UnavailablePositionException</code>
     *
     * @param test                the playground on which the check is made
     * @param face                the face to be placed
     * @param unavailablePosition if the position isn't available
     */
    private void checkThrowsUnavailablePositionException(Playground test, Face face, Position unavailablePosition){
        assertThrows(Playground.UnavailablePositionException.class, () -> test.placeCard(face, unavailablePosition));
    }

    /**
     * Method used to check if a placement throws a <code>NotEnoughResourcesException</code>
     *
     * @param test the playground on which the check is made
     * @param face the face to be placed
     */
    private void checkThrowsNotEnoughResourcesException(Playground test, Face face){
        Position pos = test.getAvailablePositions().getFirst();
        assertThrows(Playground.NotEnoughResourcesException.class, () -> test.placeCard(face, pos));
    }

    /**
     * Method used to create corners
     *
     * @param pos1 the corner on the TOP_LEFT corner
     * @param pos2 the corner on the TOP_RIGHT corner
     * @param pos3 the corner on the LOWER_RIGHT corner
     * @param pos4 the corner on the LOWER_LEFT corner
     * @return a map containing the corners and the Corner Positions
     */
    private Map<CornerPosition, Corner> createCorners(Corner pos1, Corner pos2, Corner pos3, Corner pos4) {
        Map<CornerPosition, Corner> corners = new HashMap<>();

        placeCorner(corners, CornerPosition.TOP_LEFT, pos1);
        placeCorner(corners, CornerPosition.TOP_RIGHT, pos2);
        placeCorner(corners, CornerPosition.LOWER_RIGHT, pos3);
        placeCorner(corners, CornerPosition.LOWER_LEFT, pos4);

        return corners;
    }

    /**
     * Method used to place a corner <code>c</code> and a corner position <code>pos</code> in the <code>map</code>
     *
     * @param map the CornerPosition-Corner map
     * @param pos the corner position to add
     * @param c   the corner to add
     */
    private void placeCorner(Map<CornerPosition, Corner> map, CornerPosition pos, Corner c) {
        if (c != null) {
            map.put(pos, c);
        }
    }

    /**
     * Method used to create the corners of a back card
     *
     * @return a CornerPosition-Corner map with the back corners
     */
    private Map<CornerPosition, Corner> createBackCorners() {
        Map<CornerPosition, Corner> corners = new HashMap<>();

        for (CornerPosition c : CornerPosition.values()) {
            corners.put(c, new Corner());
        }

        return corners;
    }
}