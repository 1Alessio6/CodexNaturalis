package it.polimi.ingsw.network.client.view.tui;

/**
 * This enumeration represents the actions that the player can perform and their descriptions.
 */
public enum TUIActions {
    BACK("go back at your playground"),
    COLOR("choose color"),
    DRAW("draw a card"),
    FLIP("flip the cards"),
    HELP("show this message"),
    LOBBYSIZE("set size of the lobby"),
    OBJECTIVE("choose your objective"),
    PLACE("place a card in your playground"),
    STARTER("place your starter"),
    SPY("spy <n>: look at n-index player"),
    PM("write a private message"),
    M("write a public message"),
    RULEBOOK("take a look at the manual"),
    QUIT("quit the game safely");

    private final String description;

    TUIActions(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
