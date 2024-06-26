package it.polimi.ingsw.network.client.view.tui;

/**
 * This enum contains the possible exceptions that can be thrown while running TUI interface
 */
public enum ExceptionsTUI {
    /**
     * INVALID_CARD_POSITION is thrown when the player has chosen an invalid card position.
     */
    INVALID_CARD_POSITION("not a valid card position"),
    /**
     * INVALID_COLOR is thrown when the player has chosen a nonexistent color.
     */
    INVALID_COLOR("we don't have that color, choose another one"),
    /**
     * INVALID_SIDE is thrown when the player hasn't chosen a valid side.
     */
    INVALID_SIDE("that's not a valid side"),
    /**
     * INVALID_IDX is thrown when the player has chosen an invalid objective idx.
     */
    INVALID_IDX("you have chosen an invalid idx"),
    /**
     * INVALID_PAGE is thrown when the player has chosen an invalid page number.
     */
    INVALID_PAGE("choose a valid page number"),
    /**
     * INVALID_SPY_INPUT is thrown when the player doesn't digit the spy command using a valid structure, that is,
     * spy n.
     */
    INVALID_SPY_INPUT("incorrect entry for spying"),
    /**
     * INVALID_MESSAGE_INPUT is thrown when the player doesn't digit the message command(pm or m) using a valid structure,
     * that is pm recipient message or m message.
     */
    INVALID_MESSAGE_INPUT("incorrect entry for a message"),
    /**
     * INVALID_SPY_COMMAND is thrown when a player attempt to spy its own playground.
     */
    INVALID_SPY_COMMAND("you can't spy yourself..."),
    /**
     * INVALID_USERNAME is thrown when the username digitized by the player exceeds 15 characters.
     */
    INVALID_USERNAME("Username too long"),
    /**
     * INVALID_GAME_COMMAND is thrown when a player digits an invalid game command.
     */
    INVALID_GAME_COMMAND("Invalid game command");
    private final String message;

    ExceptionsTUI(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

