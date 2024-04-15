package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.player.InvalidPlayerActionException;
import it.polimi.ingsw.model.player.Player;

import java.util.List;

/**
 * Representation of a possible game's state.
 * It defines which operations are allowed based on the overall information.
 * The fsm looks like Setup -> MatchStarted -> AdditionalTurn -> GameEnd
 */

// todo. it will require synchronized to handle different users' requests at the same time.

// NOTE. I assume there are at least two players when the request arrives to the model, otherwise the game should freeze.

/*
 * NOTE. Each action refers to the state until the request is made; the Game doesn't deal with future events like disconnections.
 */
public abstract class GameState {

    /*
      For now, requests not allowed in the current state result in no action; it's like the request doesn't produce any change.
     */

    public void placeStarter(Game game, Player player, Side side) throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
    }

    public void placeObjectiveCard(Game game, Player player, int chosenObjective) throws InvalidPlayerActionException {
    }

    public void placeCard(Game game, Player player, Card card, Side side, Position position) throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {

    }

    public void drawFromResourceDeck(Game game, Player player) throws InvalidPlayerActionException, EmptyDeckException {
    }


    public void drawFromGoldenDeck(Game game, Player player) throws InvalidPlayerActionException, EmptyDeckException {

    }

    public void drawFromFaceUpCards(Game game, Player player, int faceUpCardIdx) throws InvalidPlayerActionException {
    }

    public void skipTurn(Game game) {

    }

    protected void nextState(Game game, GameState nextState) {
        game.setStatus(nextState);
    }

}

class Setup extends GameState {

    @Override
    public void placeStarter(Game game, Player player, Side side)
            throws InvalidPlayerActionException,
            Playground.UnavailablePositionException,
            Playground.NotEnoughResourcesException {
        player.placeStarter(side);
    }

    @Override
    public void placeObjectiveCard(Game game, Player player, int chosenObjective) throws InvalidPlayerActionException {
        player.placeObjectiveCard(chosenObjective);

        for (Player p : game.getPlayers()) {
            if (!p.isSetupFinished()) {
                return;
            }
        }

        // match starts when all players have finished to set up, which is equivalent to say that there's no player in setup mode
        if (game.getPlayers().stream().filter(p -> !p.isSetupFinished()).toList().isEmpty()) {

            // find the first player. It may not be the one at index 0 since it could have disconnected from the game.
            List<Player> players = game.getPlayers();
            boolean foundCurrentPlayer = false;
            for (int i = 0; i < players.size(); ++i) {
                if (players.get(i).isConnected()) {
                    game.setCurrentPlayerIdx(i);
                    foundCurrentPlayer = true;
                }
            }

            // When the method is called there must be at least one active player: the caller themselves.
            assert (foundCurrentPlayer);
            nextState(game, new MatchStarted());
        }
    }

}

class MatchStarted extends GameState {
    private boolean lastNormalTurn = false;
    private int numPlayersPlayedLastTurn = 0;
    private final int maxPoint = 20;

    @Override
    public void placeCard(Game game, Player player, Card card, Side side, Position position)
            throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {

        if (game.getPlayers().indexOf(player) != game.getCurrentPlayerIdx()) {
            return;
        }

        int score = player.placeCard(card, side, position);
        if (score >= maxPoint) {
            lastNormalTurn = true;
        }
    }

    private void addCard(Game game, Player player, Card c) throws InvalidPlayerActionException {

        player.addCard(c);

        if (game.getGoldenDeck().isEmpty() && game.getResourceDeck().isEmpty()) {
            lastNormalTurn = true;
        }

        if (lastNormalTurn) {
            numPlayersPlayedLastTurn += 1;
            if (numPlayersPlayedLastTurn == game.getPlayers().size()) {
                game.setStatus(new AdditionalTurn());
            }
        }

    }

    private void drawFromDeck(Game game, Player player, Deck<Card> deck) throws InvalidPlayerActionException, EmptyDeckException {
        List<Player> players = game.getPlayers();
        if (players.indexOf(player) != game.getCurrentPlayerIdx()) {
            return;
        }

        Card newCard = deck.draw();
        addCard(game, player, newCard);
    }


    public void drawFromResourceDeck(Game game, Player player) throws InvalidPlayerActionException, EmptyDeckException {
        drawFromDeck(game, player, game.getResourceDeck());
    }


    public void drawFromGoldenDeck(Game game, Player player) throws InvalidPlayerActionException, EmptyDeckException {
        drawFromDeck(game, player, game.getGoldenDeck());
    }

    public void drawFromFaceUpCards(Game game, Player player, int faceUpCardIdx) throws InvalidPlayerActionException {
        List<Player> players = game.getPlayers();
        if (players.indexOf(player) != game.getCurrentPlayerIdx()) {
            return;
        }

        Card newCard = game.getFaceUpCard(faceUpCardIdx);
        addCard(game, player, newCard);
    }


    @Override
    public void skipTurn(Game game) {
        if (lastNormalTurn) {
            numPlayersPlayedLastTurn += 1;
            if (numPlayersPlayedLastTurn == game.getPlayers().size()) {
                game.setStatus(new AdditionalTurn());
            }
        }
    }

}

// using additional data I may include this state into GameState; this solution would decrease code repetitions.
class AdditionalTurn extends GameState {
    int numTurns = 0;

    @Override
    public void placeCard(Game game, Player player, Card card, Side side, Position position)
            throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {

        player.placeCard(card, side, position);

        numTurns += 1;
        if (numTurns == game.getPlayers().size()) {
            game.setStatus(new GameEnd(game));
        }
    }

    @Override
    public void skipTurn(Game game) {
        numTurns += 1;
        if (numTurns == game.getPlayers().size()) {
            game.setStatus(new GameEnd(game));
        }
    }
}

class GameEnd extends GameState {
    GameEnd(Game game) {
        game.setFinished();
    }
}
