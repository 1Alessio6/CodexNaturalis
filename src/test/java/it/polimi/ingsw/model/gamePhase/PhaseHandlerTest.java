package it.polimi.ingsw.model.gamePhase;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.Color.CardColor;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PhaseHandlerTest {
    private List<Player> genericPlayers;
    private PhaseHandler phaseHandler;

    @BeforeEach
    void setUp() {
        List<Card> cards = new ArrayList<>(Arrays.asList(
                new Card(new Front(new HashMap<>()), new Back(CardColor.BLUE, new HashMap<>(), new HashMap<>())),
                new Card(new Front(new HashMap<>()), new Back(CardColor.BLUE, new HashMap<>(), new HashMap<>())),
                new Card(new Front(new HashMap<>()), new Back(CardColor.BLUE, new HashMap<>(), new HashMap<>()))
        ));

        List<ObjectiveCard> objectives = new ArrayList<>(Arrays.asList(
                new ObjectiveResourceCard(new HashMap<>(), 1),
                new ObjectiveResourceCard(new HashMap<>(), 1)
        ));

        genericPlayers = new ArrayList<>(Arrays.asList(
                new Player("user1",
                        new Card(new Front(new HashMap<>()), new Back(CardColor.BLUE, new HashMap<>(), new HashMap<>())),
                        new ArrayList<>(cards),
                        new ArrayList<>(objectives))
                ,
                new Player("user1",
                        new Card(new Front(new HashMap<>()), new Back(CardColor.BLUE, new HashMap<>(), new HashMap<>())),
                        new ArrayList<>(cards),
                        new ArrayList<>(objectives))));

        phaseHandler = new PhaseHandler(genericPlayers);
    }

    @Test
    void transitionFromSetup_nextPhaseSetup() {
        Assertions.assertEquals(GamePhase.Setup, phaseHandler.getNextPhase(GamePhase.Setup));
    }

    @Test
    void transitionFromSetup_nextPhasePlaceNormal() {
        Assertions.assertDoesNotThrow(
                () -> {
                    // finish setup
                    for (int i = 0; i < genericPlayers.size(); ++i) {
                        genericPlayers.get(i).placeStarter(Side.FRONT);
                        genericPlayers.get(i).assignColor(PlayerColor.BLUE);
                        genericPlayers.get(i).placeObjectiveCard(0);
                    }
                }
        );

        Assertions.assertEquals(GamePhase.PlaceNormal, phaseHandler.getNextPhase(GamePhase.Setup));
    }

    @Test
    void transitionFromPlaceNormal_nextPhaseDrawNormal() {
        Assertions.assertEquals(GamePhase.DrawNormal, phaseHandler.getNextPhase(GamePhase.PlaceNormal));
    }

    @Test
    void transitionFromDrawNormal_nextPhasePlaceNormal() {
        Assertions.assertEquals(GamePhase.PlaceNormal, phaseHandler.getNextPhase(GamePhase.DrawNormal));
    }

    @Test
    void transitionFromDrawNormal_nextPhasePlaceAdditional() {
        // the last normal turn starts
        phaseHandler.setLastNormalTurn();

        // all n-1 players play their last normal turn
        for (int i = 0; i < genericPlayers.size()-1; ++i) {
            phaseHandler.skipTurn(GamePhase.PlaceNormal);
        }

        // the last player plays their last normal turn and triggers the additional turn
        Assertions.assertEquals(GamePhase.PlaceAdditional, phaseHandler.getNextPhase(GamePhase.DrawNormal));
    }

    @Test
    void transitionFromPlaceAdditional_nextPhasePlaceAddtional() {
        Assertions.assertEquals(GamePhase.PlaceAdditional, phaseHandler.getNextPhase(GamePhase.PlaceAdditional));
    }

    @Test
    void transitionFromPlaceAdditional_nextPhaseEndTurn() {
        // n-1 players play their additional turn.
        for (int i = 0; i < genericPlayers.size()-1; ++i) {
            Assertions.assertEquals(GamePhase.PlaceAdditional, phaseHandler.getNextPhase(GamePhase.PlaceAdditional));
        }

        // the last player will trigger the end of the game
        Assertions.assertEquals(GamePhase.End, phaseHandler.getNextPhase(GamePhase.PlaceAdditional));
    }

}