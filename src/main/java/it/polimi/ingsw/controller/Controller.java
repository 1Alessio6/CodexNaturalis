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
    public void placeCard(int cardIdx, Side side, Position pos, String username){
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

    public void drawFromFaceUpCards(int faceUpCardIdx, String username) {
        try {
            game.drawFromFaceUpCards(username, faceUpCardIdx);
        } catch (InvalidPlayerActionException e) {
            e.printStackTrace();
        }
    }

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

    public void joinGame(String username) {
            game.setNetworkStatus(username, true);
    }

    public void joinLobby(String username, Color color){
        lobby.addPlayer(username, color);
    }

    public void createGame() {
        try {
            this.game = this.lobby.createGame();
        } catch (IllegalStateException e){
            e.printStackTrace();
        }
    }

    public void playerDisconnected() {
        //timer.schedule(game.setFinished(), countdown);
        timer.cancel();
    }

    public void createLobby(String creator, Color color, int numPlayers) {
        this.lobby = new Lobby(creator, color, numPlayers);
    }
}
