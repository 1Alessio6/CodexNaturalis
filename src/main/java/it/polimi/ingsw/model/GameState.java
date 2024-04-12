package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.player.InvalidPlayerActionException;
import it.polimi.ingsw.model.player.Player;

import java.nio.channels.NoConnectionPendingException;
import java.util.List;

/**
 * Representation of a possible game's state.
 * It defines which operations are allowed based on the overall information.
 */

// todo. it will require synchronized to handle different users' requests at the same time.

// NOTE. I assume there are at least two players when the request arrives to the model, otherwise the game should freeze.

/*
 * NOTE. Each action refers to the state until the request is made; the Game doesn't deal with future events like disconnections.
 */
public abstract class GameState {

    // notify about impossible requests.
    private void checkConsistency(Game game, String username) {
        // the username must be valid
        assert (!game.getPlayers().stream().filter(p -> p.getUsername().equals(username)).findFirst().isEmpty());
    }

    /*
     * For now, requests not allowed in the current state result in no action; it's like the request doesn't produce any change.
     */

    public void placeStarter(Game game, String username, Side side) throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        checkConsistency(game, username);
        Player player = game.getPlayers().stream().filter(p -> p.getUsername().equals(username)).findFirst().get();

        placeStarter(game, player, side);
    }

    protected void placeStarter(Game game, Player player, Side side) throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {

    }

    public void placeObjectiveCard(Game game, String username, ObjectiveCard chosenObjective) throws InvalidPlayerActionException {
        checkConsistency(game, username);
        Player player = game.getPlayers().stream().filter(p -> p.getUsername().equals(username)).findFirst().get();

        placeObjective(game, player, chosenObjective);
    }

    protected void placeObjective(Game game, Player player, ObjectiveCard chosenObjective) throws InvalidPlayerActionException {

    }

    public void placeCard(Game game, String username, Card card, Side side, Position position) throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
        checkConsistency(game, username);
        Player player = game.getPlayers().stream().filter(p -> p.getUsername().equals(username)).findFirst().get();

        placeCard(game, player, card, side, position);
    }

    protected void placeCard(Game game, Player player, Card card, Side side, Position position) throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {

    }

    public void drawFromResourceDeck(Game game, String username) throws InvalidPlayerActionException {
        checkConsistency(game, username);
        Player player = game.getPlayers().stream().filter(p -> p.getUsername().equals(username)).findFirst().get();

        drawFromDeck(game, player, game.getResourceDeck());
    }


    public void drawFromGoldenDeck(Game game, String username) throws InvalidPlayerActionException {
        checkConsistency(game, username);
        Player player = game.getPlayers().stream().filter(p -> p.getUsername().equals(username)).findFirst().get();

        drawFromDeck(game, player, game.getGoldenDeck());
    }


    protected void drawFromDeck(Game game, Player player, Deck<Card> deck) throws InvalidPlayerActionException {

    }

    public void drawFromFaceUpCards(Game game, String username, int faceUpCardIdx) throws InvalidPlayerActionException {
        checkConsistency(game, username);
        Player player = game.getPlayers().stream().filter(p -> p.getUsername().equals(username)).findFirst().get();

        drawFromFaceUpCards(game, player, faceUpCardIdx);
    }

    protected void drawFromFaceUpCards(Game game, Player player, int faceUpCardIdx) throws InvalidPlayerActionException {

    }

    // by default the player's network status is set to disconnected.
    public void handleDisconnection(Game game, String username) {
        checkConsistency(game, username);
        Player player = game.getPlayers().stream().filter(p -> p.getUsername().equals(username)).findFirst().get();

        player.setNetworkStatus(false);
        completeDraw(game, player);
    }

    protected void completeDraw(Game game, Player player) {

    }

    public void skipTurn(Game game) {

    }

    protected void nextState(Game game, GameState nextState) {
        game.setStatus(nextState);
    }

}

class Setup extends GameState {

    @Override
    protected void placeStarter(Game game, Player player, Side side)
            throws InvalidPlayerActionException,
            Playground.UnavailablePositionException,
            Playground.NotEnoughResourcesException {
        player.placeStarter(side);
    }

    @Override
    protected void placeObjective(Game game, Player player, ObjectiveCard chosenObjective) throws InvalidPlayerActionException {
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
    private boolean reachedTwenty = false;
    private int numPlaysAfterReachingTwenty = 0;
    private final int maxPoint = 20;

    @Override
    protected void placeCard(Game game, Player player, Card card, Side side, Position position)
            throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {

        if (game.getPlayers().indexOf(player) != game.getCurrentPlayerIdx()) {
            return;
        }

        int score = player.placeCard(card, side, position);
        if (score == maxPoint
                && !reachedTwenty)  // no one has reached maxPoint yet
        {
            reachedTwenty = true;
        }
    }

    // todo. to improve: code repetition between drawFromDeck and DrawFromFaceUpCards
    @Override
    protected void drawFromDeck(Game game, Player player, Deck<Card> deck) throws InvalidPlayerActionException {
        List<Player> players = game.getPlayers();
        if (players.indexOf(player) != game.getCurrentPlayerIdx()) {
            return;
        }

        try {
            player.addCard(deck.draw());
        } catch (EmptyDeckException e) {
            e.printStackTrace();
        }

        if (reachedTwenty) {
            numPlaysAfterReachingTwenty += 1;
            if (numPlaysAfterReachingTwenty == players.size()) {
                game.setStatus(new AdditionalTurn());
            }
        }
    }

    // todo. complete method. Refactoring is needed to avoid writing the same code as drawFromDeck twice
    protected void drawFromFaceUpCards(Game game, Player player, int faceUpCardIdx) throws InvalidPlayerActionException {
    }


    @Override
    public void skipTurn(Game game) {
        if (reachedTwenty) {
            numPlaysAfterReachingTwenty += 1;
            if (numPlaysAfterReachingTwenty == game.getPlayers().size()) {
                game.setStatus(new AdditionalTurn());
            }
        }
    }

}

// using additional data I may include this state into GameState; this solution would decrease code repetitions.
class AdditionalTurn extends GameState {
    int numTurns = 0;

    @Override
    protected void placeCard(Game game, Player player, Card card, Side side, Position position)
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
