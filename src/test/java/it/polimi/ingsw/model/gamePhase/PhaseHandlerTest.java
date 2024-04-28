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
        Assertions.assertEquals(GamePhase.Setup, phaseHandler.getNextPhase(GamePhase.Setup, 0));
    }

    @Test
    void transitionFromSetup_nextPhasePlaceNormal() {
        Assertions.assertDoesNotThrow(
                () -> {
                    // finish setup
                    for (Player genericPlayer : genericPlayers) {
                        genericPlayer.placeStarter(Side.FRONT);
                        genericPlayer.assignColor(PlayerColor.BLUE);
                        genericPlayer.placeObjectiveCard(0);
                    }
                }
        );

        Assertions.assertEquals(GamePhase.PlaceNormal, phaseHandler.getNextPhase(GamePhase.Setup, 0));
    }

    @Test
    void transitionFromPlaceNormal_nextPhaseDrawNormal() {
        Assertions.assertEquals(GamePhase.DrawNormal, phaseHandler.getNextPhase(GamePhase.PlaceNormal, 0));
    }

    @Test
    void transitionFromDrawNormal_nextPhasePlaceNormal() {
        Assertions.assertEquals(GamePhase.PlaceNormal, phaseHandler.getNextPhase(GamePhase.DrawNormal, 0));
    }

    private void testLastTurn(int playerCausingLastNormalTurn, GamePhase phase) {
        for (int playerNormalTurn = playerCausingLastNormalTurn + 1; playerNormalTurn < genericPlayers.size(); ++playerNormalTurn) {
            Assertions.assertEquals(GamePhase.PlaceNormal, phase);
            phase = phaseHandler.getNextPhase(phase, playerCausingLastNormalTurn);
            Assertions.assertEquals(GamePhase.DrawNormal, phase);
            phase = phaseHandler.getNextPhase(phase, playerNormalTurn);
        }

        Assertions.assertEquals(phase, GamePhase.PlaceAdditional);
    }

    @Test
    void transitionFromDrawNormal_nextPhasePlaceAdditional() {
        for (int playerCausingLastNormalTurn = 0; playerCausingLastNormalTurn < genericPlayers.size(); ++playerCausingLastNormalTurn) {
            // triggered after reaching 20 points
            GamePhase phase = GamePhase.PlaceNormal;
            phaseHandler.setLastNormalTurn();
            phase = phaseHandler.getNextPhase(phase, playerCausingLastNormalTurn);
            Assertions.assertEquals(phase, GamePhase.DrawNormal);
            phase = phaseHandler.getNextPhase(phase, playerCausingLastNormalTurn);

            testLastTurn(playerCausingLastNormalTurn, phase);

            // triggered after reaching an empty deck
            phase = GamePhase.DrawNormal;
            phaseHandler.setLastNormalTurn();
            phase = phaseHandler.getNextPhase(phase, playerCausingLastNormalTurn);

            testLastTurn(playerCausingLastNormalTurn, phase);
        }
    }

    @Test
    void testTransitionsFromAdditionalTurn() {
        GamePhase phase = GamePhase.PlaceAdditional;
        for (int i = 0; i < genericPlayers.size(); ++i) {
            phase = phaseHandler.getNextPhase(phase, i);
            if (i < genericPlayers.size() - 1) {
                Assertions.assertEquals(GamePhase.PlaceAdditional, phase);
            } else {
                Assertions.assertEquals(GamePhase.End, phase);
            }
        }

        Assertions.assertEquals(GamePhase.End, phase);
    }

}