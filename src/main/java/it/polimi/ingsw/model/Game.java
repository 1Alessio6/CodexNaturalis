package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.chat.ChatDatabase;
import it.polimi.ingsw.model.player.Player;

import java.util.List;


/**
 * Representation of the game.
 * Each player is recognised through the system by their username.
 */

public class Game {
     private Deck resourceCards;
    private Deck goldenCards;

    private List<ObjectiveCard> ObjectiveCardDeck;
    private List<Card> starterCards;
    private List<ObjectiveCard> objectiveCards;

     private List<Front> faceUpCards; // todo. immutable

     private List<ObjectiveCard> commonObjects; // todo. immutable

    private enum GamePhase {
        NOT_STARTED,
        ONGOING,
        LAST_TURN,
        ENDED
    }

    GamePhase phase;

    private int numRequiredPlayers;
     private int currentPlayer; // index in the current player list.

     private List<Player> players;

     private ChatDatabase chatDatabase;

     // persistence
     // todo. define attribute to abstract access to the disk in order to save the current status

    public Game(Player creator, int numPlayersToStart) throws IllegalArgumentException {

    }

    /**
     * adds new player to the game, if possible.
     * @param newPlayer player to add
     * @return false if <code>newPlayer</code> contains a valid reference but their username has already in use or the game has already started.
     * @throws IllegalArgumentException if <code>newPlayer</code> is a null reference
     */
    // generic method to add new player. throws an exception for invalid players
    public boolean addPlayer(Player newPlayer) throws IllegalArgumentException {
        return true;
    }

    public void assignColor(String username, Color color) {

    }

    /**
     *
     */
    public void chooseObjectiveCard(ObjectiveCard chosenObjective) {

    }

    // todo. (maybe factor away if none need the information externally.
    // the game status is controlled by the model only, the other components (Controller+View) receives information by the model
    boolean canStart() {
        return false;
    }

    // we can simply send a message containing the receiver and the event
    private List<Player> activePlayers() {
        return null;
    }

    // maybe done automatically by the model at the end of each turn
    void placeFaceUpCards() {

    }

    // todo(better name)
    public boolean isReachedTwenty() {
        return false;
    }

    public void drawFromRsourceDeck() {

    }
    public void drawFromGoldenDeck() {

    }

   // dynamic type passed by the controller
   public void placeCard(Face face) {

   }

   // handle next turn, skipping inactive players
    private void nextTurn() {

    }

    private void startLastTurn() {

    }

    // todo(needed). add method to know whether the client has disconnected and notify all clients
}
