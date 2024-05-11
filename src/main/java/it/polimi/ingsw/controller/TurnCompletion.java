package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.InvalidGamePhaseException;
import it.polimi.ingsw.model.NonexistentPlayerException;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Color.InvalidColorException;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.model.player.InvalidPlayerActionException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.action.PlayerState;

import java.rmi.RemoteException;
import java.util.*;
import java.util.List;

/**
 * Class to handle turn completion after joining or leaving the game.
 * When the game is active, disconnection of players in setup mode or the current player, leads to an automatic turn completion;
 * if the game is suspended no completion is done.
 */

public class TurnCompletion {
    private boolean isGameActive;

    public TurnCompletion() {
        isGameActive = true;
    }

    private List<String> getListOfInactivePlayers(Game game) {
        List<String> playersInGame = game.getPlayers().stream().map(Player::getUsername).toList();
        List<String> activePlayers = game.getActivePlayers().stream().map(Player::getUsername).toList();
        playersInGame.removeAll(activePlayers);
        return playersInGame;
    }

    private void completeSetup(Game game, String username) {
        PlayerState playerState = game.getPlayerByUsername(username).getPlayerAction().getPlayerState();

        if (PlayerState.ChooseStarter == playerState) {
            List<Side> sides = Arrays.asList(Side.FRONT, Side.BACK);
            Collections.shuffle(sides);
            try {
                game.placeStarter(username, sides.getFirst());
            } catch (InvalidPlayerActionException | InvalidGamePhaseException ignored) {
            }
        }

        if (PlayerState.ChooseColor == playerState) {
            List<PlayerColor> remainingColors = new ArrayList<>(game.getAvailableColor());
            Collections.shuffle(remainingColors);
            try {
                game.assignColor(username, remainingColors.getFirst());
            } catch (InvalidPlayerActionException | InvalidColorException | NonexistentPlayerException |
                     InvalidGamePhaseException | RemoteException ignored) {
            }
        }

        if (PlayerState.ChooseObjective == playerState) {
            List<Integer> chosenObjective = Arrays.asList(0, 1);
            Collections.shuffle(chosenObjective);
            try {
                game.placeObjectiveCard(username, chosenObjective.getFirst());
            } catch (InvalidPlayerActionException | InvalidGamePhaseException ignored) {
            }
        }
    }

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
        } catch (InvalidPlayerActionException | InvalidGamePhaseException ignored) {
        }
    }

    private void completeCurrentPlayerTurn(Game game, Player currentPlayer) {
        if (currentPlayer.getPlayerAction().getPlayerState() == PlayerState.Place) {
            game.skipTurn(currentPlayer.getUsername());
        } else {
            automaticDraw(game, currentPlayer.getUsername());
        }
    }

    private void completePendingTurn(Game game) {
        List<String> inactivePlayers = getListOfInactivePlayers(game);
        for (String username : inactivePlayers) {
            if (game.getPhase() == GamePhase.Setup) {
                completeSetup(game, username);
            } else {
                Player currentPlayer = game.getCurrentPlayer();
                if (currentPlayer.getUsername().equals(username)) {
                    completeCurrentPlayerTurn(game, currentPlayer);
                }
            }
        }
    }

    public void handleJoin(Game game) {
        // transition from suspended to active: complete turn of all disconnected players
        if (!isGameActive && game.isActive()) {
            completePendingTurn(game);
        }
        isGameActive = game.isActive();
    }

    public void handleLeave(Game game) {
        // game is active: complete turn of the disconnected player.
        if (game.isActive()) {
            completePendingTurn(game);
        }
        isGameActive = game.isActive();
    }
}
