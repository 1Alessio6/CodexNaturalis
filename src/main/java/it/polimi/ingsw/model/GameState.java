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


    /**
     * Allows the player to place a certain side of a card in a game.
     *
     * @param game   in which the action takes place.
     * @param player who performs the placement.
     * @param side   of the card.
     * @throws InvalidPlayerActionException            if the player cannot perform the operation.
     * @throws Playground.UnavailablePositionException if the position is not available.
     * @throws Playground.NotEnoughResourcesException  if the player's resources are not enough to place the card.
     */
    public void placeStarter(Game game, Player player, Side side) throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {
    }

    /**
     * Allows the player to place the secret objective in a game.
     *
     * @param game            in which the action takes place.
     * @param player          who performs the placement.
     * @param chosenObjective card.
     * @throws InvalidPlayerActionException if the player cannot perform the operation.
     */
    public void placeObjectiveCard(Game game, Player player, int chosenObjective) throws InvalidPlayerActionException {
    }


    /**
     * Allows the player to place kind of card in the side and position provided in a game.
     *
     * @param game     in which the action takes place.
     * @param player   who performs the placement.
     * @param card     to place.
     * @param side     of the card.
     * @param position in the playground.
     * @throws InvalidPlayerActionException            if the player cannot perform the operation.
     * @throws Playground.UnavailablePositionException if the position is not available.
     * @throws Playground.NotEnoughResourcesException  if the player's resources are not enough to place the card.
     */
    public void placeCard(Game game, Player player, Card card, Side side, Position position) throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {

    }


    /**
     * Draws from the resource cards deck.
     *
     * @param game   in which the action takes place.
     * @param player who performs the placement.
     * @throws InvalidPlayerActionException if the player cannot perform the operation.
     * @throws EmptyDeckException           if the deck is empty.
     */
    public void drawFromResourceDeck(Game game, Player player) throws InvalidPlayerActionException, EmptyDeckException {
    }


    /**
     * Draws from the golden cards deck.
     *
     * @param game   in which the action takes place.
     * @param player who performs the placement.
     * @throws InvalidPlayerActionException if the player cannot perform the operation.
     * @throws EmptyDeckException           if the deck is empty.
     */
    public void drawFromGoldenDeck(Game game, Player player) throws InvalidPlayerActionException, EmptyDeckException {

    }


    /**
     * Draws from one of the available face up cards.
     *
     * @param game          in which the action takes place.
     * @param player        who performs the placement.
     * @param faceUpCardIdx specifying the face up card.
     * @throws InvalidPlayerActionException if the player cannot perform the operation.
     */
    public void drawFromFaceUpCards(Game game, Player player, int faceUpCardIdx) throws InvalidPlayerActionException {
    }


    /**
     * Skips the player's turn if the net collapses or the client crashes.
     *
     * @param game in which the action takes place.
     */
    public void skipTurn(Game game) {

    }


    /**
     * Update the state of a game.
     *
     * @param game      in which the action takes place.
     * @param nextState of the game.
     */
    protected void nextState(Game game, GameState nextState) {
        game.setStatus(nextState);
    }

}


/**
 * In the Setup class, the players will have already chosen their username, the color of their token and their secret
 * objective, apart from that, the common objectives will have already been selected, therefore, this class is
 * responsible for the placement of the starter card and the objective card inside the player's cards.
 */
class Setup extends GameState {


    /**
     * {@inheritDoc}
     */
    @Override
    public void placeStarter(Game game, Player player, Side side)
            throws InvalidPlayerActionException,
            Playground.UnavailablePositionException,
            Playground.NotEnoughResourcesException {
        player.placeStarter(side);
    }


    /**
     * {@inheritDoc}
     */
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


/**
 * The MatchStarted class is responsible for managing all the actions that occur from the conclusion of the setup state
 * to the beginning of the additional turn.
 * In this class, you can find methods to place a card, draw a card, skip the turn and a method to manage state change.
 */
class MatchStarted extends GameState {

    /**
     * Specifies if it is the last turn or not
     */
    private boolean lastNormalTurn = false;

    /**
     * Indicates the number of players that have played the last turn.
     */
    private int numPlayersPlayedLastTurn = 0;

    /**
     * Indicates the limit of points needed to move on to the additional turn.
     */
    private final int maxPoint = 20;


    /**
     * {@inheritDoc}
     */
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


    /**
     * Adds a new card to the player's card list and checks whether the condition to start the additional turn has been
     * filled.
     *
     * @param game   in which the action takes place.
     * @param player to which the card is added.
     * @param c      referring to the card.
     * @throws InvalidPlayerActionException if the player cannot perform the operation.
     */
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


    /**
     * Draws a card from a generic kind of deck.
     *
     * @param game   in which the action is performed.
     * @param player to which the card is added.
     * @param deck   from which the card is drawn.
     * @throws InvalidPlayerActionException if the player cannot perform the operation.
     * @throws EmptyDeckException           if the deck is empty.
     */
    private void drawFromDeck(Game game, Player player, Deck<Card> deck) throws InvalidPlayerActionException, EmptyDeckException {
        List<Player> players = game.getPlayers();
        if (players.indexOf(player) != game.getCurrentPlayerIdx()) {
            return;
        }

        Card newCard = deck.draw();
        addCard(game, player, newCard);
    }


    /**
     * {@inheritDoc}
     */
    public void drawFromResourceDeck(Game game, Player player) throws InvalidPlayerActionException, EmptyDeckException {
        drawFromDeck(game, player, game.getResourceDeck());
    }


    /**
     * {@inheritDoc}
     */
    public void drawFromGoldenDeck(Game game, Player player) throws InvalidPlayerActionException, EmptyDeckException {
        drawFromDeck(game, player, game.getGoldenDeck());
    }


    /**
     * {@inheritDoc}
     */
    public void drawFromFaceUpCards(Game game, Player player, int faceUpCardIdx) throws InvalidPlayerActionException {
        List<Player> players = game.getPlayers();
        if (players.indexOf(player) != game.getCurrentPlayerIdx()) {
            return;
        }

        Card newCard = game.getFaceUpCard(faceUpCardIdx);
        addCard(game, player, newCard);
    }


    /**
     * {@inheritDoc}
     */
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


/**
 * Additional turn is a class that contains the necessary methods to direct the last phase of the game, that is, the
 * last round, which takes place from the moment in which a player achieves 20 points until the moment in which the
 * last player places a card.
 *
 */
// using additional data I may include this state into GameState; this solution would decrease code repetitions.
class AdditionalTurn extends GameState {

    /**
     * number of played turns
     */
    int numTurns = 0;


    /**
     *{@inheritDoc}
     */
    @Override
    public void placeCard(Game game, Player player, Card card, Side side, Position position)
            throws InvalidPlayerActionException, Playground.UnavailablePositionException, Playground.NotEnoughResourcesException {

        player.placeCard(card, side, position);

        numTurns += 1;
        if (numTurns == game.getPlayers().size()) {
            game.setStatus(new GameEnd(game));
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void skipTurn(Game game) {
        numTurns += 1;
        if (numTurns == game.getPlayers().size()) {
            game.setStatus(new GameEnd(game));
        }
    }
}


/**
 * Endgame is the class that looks after the ending of the game.
 */
class GameEnd extends GameState {

    /**
     * Establishes the end of the game.
     * @param game that is over.
     */
    GameEnd(Game game) {
        game.setFinished();
    }
}
