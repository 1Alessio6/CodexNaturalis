package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.card.EmptyDeckException;
import it.polimi.ingsw.model.player.InvalidPlayerActionException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.NonexistentPlayerException;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color;
import it.polimi.ingsw.model.card.Side;

import java.util.EventListener;
import java.util.Timer;

public class Controller implements EventListener {
    private Timer timer;
    private Lobby lobby;
    private Game game;

    public Controller() {
    }

    private static final long countdown = 60000;
    // event listener

    /**
     * Places the card on the side and position specified.
     *
     * @param cardIdx  specifying the card.
     * @param side     of the card.
     * @param pos      of the playground.
     * @param username of the player.
     */
    public void placeCard(int cardIdx, Side side, Position pos, String username) {
        try {
            game.placeCard(username, game.getUserByUsername(username).getCards().get(cardIdx), side, pos);
        } catch (InvalidPlayerActionException e) {
            throw new RuntimeException(e);
        } catch (Playground.UnavailablePositionException e) {
            throw new RuntimeException(e);
        } catch (Playground.NotEnoughResourcesException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Draws from one of the available face up cards.
     *
     * @param faceUpCardIdx specifying the face up card.
     * @param username      of the player.
     */
    public void drawFromFaceUpCards(int faceUpCardIdx, String username) {
        try {
            game.drawFromFaceUpCards(username, faceUpCardIdx);
        } catch (InvalidPlayerActionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws a resource card or a golden card from the deck.
     *
     * @param deckType the type of the deck.
     * @param username of the player.
     */
    public void drawFromDeck(String deckType, String username) {
        try {
            // TODO: deck type could be passed in a better way
            if (deckType.equals("a"))
                game.drawFromResourceDeck(username);
            else
                game.drawFromGoldenDeck(username);
        } catch (EmptyDeckException e) {
            e.printStackTrace();
        } catch (InvalidPlayerActionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Allows the connection of a player into the game.
     *
     * @param username of the player.
     */
    public void joinGame(String username) {
        game.setNetworkStatus(username, true);
    }

    /**
     * Allows the player to join into the lobby
     *
     * @param username of the player.
     * @param color    token color chosen for the player.
     */
    public void joinLobby(String username, Color color) {
        lobby.addPlayer(username, color);
    }

    /**
     * Creates a new game with the players in the lobby
     */
    public void createGame() {
        try {
            this.game = this.lobby.createGame();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void playerDisconnected() {
        //timer.schedule(game.setFinished(), countdown);
        timer.cancel();
    }

    /**
     * Creates a lobby
     *
     * @param creator    of the lobby
     * @param color      of the creator
     * @param numPlayers required to start the game
     */
    public void createLobby(String creator, Color color, int numPlayers) {
        this.lobby = new Lobby(creator, color, numPlayers);
    }

    /**
     * Places the secret objective from one of the two available.
     *
     * @param objectiveIdx specifying the secret card
     * @param username     of the player
     */
    public void chooseObjectiveCard(int objectiveIdx, String username) throws InvalidPlayerActionException {
        try {
            game.placeObjectiveCard(username, objectiveIdx);
        } catch (InvalidPlayerActionException e) {
            e.printStackTrace();
        }

    }

    /**
     * Assigns a name to the player
     *
     * @param username of the player
     */
    public void nameUsername(String username) {
        lobby.addPlayer(username, null);
    }
    //assuming that a "NULL" value is going to be assigned in the color's enumeration

    /**
     * Assigns a color for the player
     *
     * @param username of the player
     * @param color    token color chosen by the player
     */
    public void assignColor(String username, Color color) {

        if (lobby.getRemainColors().stream().anyMatch(remainColor -> remainColor == color)) {
            lobby.addPlayer(username, color);
        } else {
            System.out.println("Already assign color");
        }
    }
    //another approach can be adopted, as it is the try-catch statement

}
