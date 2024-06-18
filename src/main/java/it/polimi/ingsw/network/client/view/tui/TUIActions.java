package it.polimi.ingsw.network.client.view.tui;

/**
 * This enumeration represents the actions that the player can perform and their descriptions.
 */
public enum TUIActions {
    /**
     * BACK refers to the action of returning to the playground
     */
    BACK("go back at your playground"),
    /**
     * COLOR refers to the action of choosing the token color of the player
     */
    COLOR("choose color"),
    /**
     * DRAW refers to the action of drawing a card from the face up cards or 1 of the 2 decks
     */
    DRAW("draw a card"),
    /**
     * FLIP refers to the action of flipping over the cards
     */
    FLIP("flip the cards"),
    /**
     * HELP refers to the action that allows the player to see the current available actions
     */
    HELP("show this message"),
    /**
     * LOBBYSIZE refers to the action that allows the first player to choose the number of players of the game
     */
    LOBBYSIZE("<n>: set size of the lobby"),
    /**
     * OBJECTIVE refers to the action that allows a player to choose his/her the secret objective
     */
    OBJECTIVE("choose your objective"),
    /**
     * PLACE refers to the action that allows the player to place a card in his/her playground
     */
    PLACE("place a card in your playground"),
    /**
     * STARTER refers to the action that allows the player to place the starter card in his/her playground
     */
    STARTER("place your starter"),
    /**
     * SPY refers to the action that allows the player to look the n-player playground
     */
    SPY("<n>: look at n-index player"),
    /**
     * PM refers to the action that allows the player to write a private message
     */
    PM("write a private message"),
    /**
     * M refers to the action that allows the player to write a public message
     */
    M("write a public message"),
    /**
     * RULEBOOK refers to the action that allows the player to take a look at the manual
     */
    RULEBOOK("take a look at the manual"),
    /**
     * MVPG refers to the action that allows the player to move the playground to the given offset
     */
    MVPG("<x,y>: move playground to the given offset"),
    /**
     * QUIT refers to the action that allows the player to quit the game safely
     */
    QUIT("quit the game safely");

    private final String description;

    TUIActions(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
