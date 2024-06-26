package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.InvalidGamePhaseException;
import it.polimi.ingsw.model.NonexistentPlayerException;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Color.InvalidColorException;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.InvalidFaceUpCardException;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.model.player.InvalidPlayerActionException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.action.PlayerState;

import java.util.*;
import java.util.List;

/**
 * Class to handle turn completion after joining or leaving the game.
 * When the game is active, disconnection of players in setup mode or the current player, leads to an automatic turn completion;
 * if the game is suspended no completion is done.
 */

public class TurnCompletion {
    private boolean isGameActive;

    /**
     * Constructs a new <code>TurnCompletion</code> in which the game is active
     */
    public TurnCompletion() {
        isGameActive = true;
    }

    private List<String> getListOfInactivePlayers(Game game) {
        List<String> playersInGame = new ArrayList<>(game.getPlayers().stream().map(Player::getUsername).toList());
        List<String> activePlayers = game.getActivePlayers().stream().map(Player::getUsername).toList();
        playersInGame.removeAll(activePlayers);
        return playersInGame;
    }

    /**
     * Completes the setup for the <code>player</code>.
     *
     * @param game   the representation of the game
     * @param player to complete the setup
     */
    private void completeSetup(Game game, Player player) {
        if (PlayerState.ChooseStarter == player.getPlayerAction().getPlayerState()) {
            List<Side> sides = Arrays.asList(Side.FRONT, Side.BACK);
            Collections.shuffle(sides);
            try {
                game.placeStarter(player.getUsername(), sides.getFirst());
            } catch (InvalidPlayerActionException | InvalidGamePhaseException ignored) {
            }
        }

        if (PlayerState.ChooseColor == player.getPlayerAction().getPlayerState()) {
            List<PlayerColor> remainingColors = new ArrayList<>(game.getAvailableColor());
            Collections.shuffle(remainingColors);
            try {
                game.assignColor(player.getUsername(), remainingColors.getFirst());
            } catch (InvalidPlayerActionException | InvalidColorException | NonexistentPlayerException |
                     InvalidGamePhaseException ignored) {
            }
        }

        if (PlayerState.ChooseObjective == player.getPlayerAction().getPlayerState()) {
            List<Integer> chosenObjective = Arrays.asList(0, 1);
            Collections.shuffle(chosenObjective);
            try {
                game.placeObjectiveCard(player.getUsername(), chosenObjective.getFirst());
            } catch (InvalidPlayerActionException | InvalidGamePhaseException ignored) {
            }
        }
    }

    /**
     * Automatically draws a card instead of the <code>username</code>.
     *
     * @param game     the representation of the game.
     * @param username of the player the card is added to.
     */
    private void automaticDraw(Game game, String username) {
        List<Integer> faceUpCardIdx = new ArrayList<>();
        List<Card> faceUpCards = game.getFaceUpCards();
        for (int i = 0; i < faceUpCards.size(); ++i) {
            if (faceUpCards.get(i) != null) {
                faceUpCardIdx.add(i);
            }
        }
        Collections.shuffle(faceUpCardIdx);
        try {
            game.drawFromFaceUpCards(username, faceUpCardIdx.getFirst());
        } catch (InvalidPlayerActionException | InvalidGamePhaseException | InvalidFaceUpCardException ignored) {
        }
    }

    /**
     * Completes the <code>current player</code>'s turn.
     *
     * @param game          the representation of the game.
     * @param currentPlayer whose turn is to be completed.
     */
    private void completeCurrentPlayerTurn(Game game, Player currentPlayer) {
        if (currentPlayer.getPlayerAction().getPlayerState() == PlayerState.Place) {
            game.skipTurn(currentPlayer.getUsername());
        } else {
            automaticDraw(game, currentPlayer.getUsername());
        }
    }

    /**
     * Completes the turn of all inactive players in the game.
     *
     * @param game the representation of the game.
     */
    private void completePendingTurn(Game game) {
        List<String> inactivePlayers = getListOfInactivePlayers(game);
        for (String username : inactivePlayers) {
            if (game.getPhase() == GamePhase.Setup) {
                if (game.getPlayerByUsername(username).getPlayerAction().getPlayerState() != PlayerState.Place) {
                    completeSetup(game, game.getPlayerByUsername(username));
                }
            } else {
                Player currentPlayer = game.getCurrentPlayer();
                if (currentPlayer.getUsername().equals(username)) {
                    completeCurrentPlayerTurn(game, currentPlayer);
                }
            }
        }
    }

    /**
     * Handles the transition from suspended to active game.
     *
     * @param game the representation of the game.
     */
    public void handleJoin(Game game) {
        // transition from suspended to active: complete turn of all disconnected players
        if (!isGameActive && game.isActive() && !game.isFinished()) {
            completePendingTurn(game);
        }
        isGameActive = game.isActive();
    }

    /**
     * Handles the leaving of a player by completing the turn of inactive players.
     *
     * @param game the representation of the game.
     */
    public void handleLeave(Game game) {
        GamePhase currentPhase = game.getPhase();
        if (!game.isFinished() && (currentPhase == GamePhase.Setup || game.isActive())) {
            completePendingTurn(game);
        }
        isGameActive = game.isActive();
    }
}
