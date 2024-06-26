package it.polimi.ingsw.network.client.view.gui.util;

/**
 * Enumeration representing the icons used in GUIs interface
 */
public enum Icon {

    //todo add credits icons8 and flatIcons for 4th place
    /**
     * OBSERVE_PLAYGROUND is the icon that allows the player to see other players' playground
     */
    OBSERVE_PLAYGROUND("/gui/png/icons/observe_playground.png"),
    /**
     * HOME is the icon that allows the player to return to his/her own playground
     */
    HOME("/gui/png/icons/home.png"),
    /**
     * SETTINGS is the icon that allows the player to resize his/her screen
     */
    SETTINGS("gui/png/icons/settings.png"),
    /**
     * RULEBOOK is the icon that allows the player to consult the rulebook
     */
    RULEBOOK("/gui/png/icons/book.png"),
    /**
     * FIRST is the icon representing the player is occupying the first rank
     */
    FIRST("/gui/png/icons/first-place.png"),
    /**
     * SECOND is the icon representing the player is occupying the second rank
     */
    SECOND("/gui/png/icons/second-place.png"),
    /**
     * THIRD is the icon representing the player is occupying the third rank
     */
    THIRD("/gui/png/icons/third-place.png"),
    /**
     * FOURTH is the icon representing the player is occupying the fourth rank
     */
    FOURTH("/gui/png/icons/fourth-place.png"),
    /**
     * RETURN is the icon that allows the player to return to the menu
     */
    RETURN("/gui/png/icons/return-to-menu.png"),
    /**
     * SERVER is the icon that allows the player to change server
     */
    SERVER("/gui/png/icons/server.png"),
    /**
     * FULLSCREEN is the icon that allows the player to enter in fullscreen mode
     */
    FULLSCREEN("/gui/png/icons/full-screen.png"),
    /**
     * LOGOUT is the icon that allows the player to close the application
     */
    LOGOUT("/gui/png/icons/logout.png");

    private final String path;

    /**
     * Constructs an <code>Icon</code> with the <code>path</code> provided
     *
     * @param path the path where the icon resides
     */
    Icon(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
