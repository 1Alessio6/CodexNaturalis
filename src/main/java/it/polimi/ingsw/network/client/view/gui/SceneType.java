package it.polimi.ingsw.network.client.view.gui;

/**
 * Enumeration representing the possible scene types
 */
public enum SceneType {
    /**
     * SELECT_USERNAME refers to the scene in which the player chooses his/her username
     */
    SELECT_USERNAME("/gui/SelectUsernameScene.fxml"),
    /**
     * CONNECTION refers to the scene in which the player connects
     */
    CONNECTION("/gui/ConnectionScene.fxml"),
    /**
     * LOBBY refers to the scene in which the number of players set by the first player is expected to be fulfilled
     */
    LOBBY("/gui/LobbyScene.fxml"),
    /**
     * SETUP refers to the scene in which the selection of the starter side, the token color and secret objective are
     * made
     */
    SETUP("/gui/SetupScene.fxml"),
    /**
     * GAME refers to the scene in which the game is active
     */
    GAME("/gui/GameScene.fxml"),
    /**
     * CRASH refers to the scene in which lobby/server crashes
     */
    CRASH("/gui/CrashScene.fxml"),
    /**
     * END refers to the scene in which the game ends
     */
    END("/gui/EndScene.fxml");

    private final String path;

    /**
     * Constructs the <code>SceneType</code> with the <code>path</code>
     *
     * @param path the fxml scene path
     */
    SceneType(String path) {
        this.path = path;
    }

    public String getPath(){
        return path;
    }
}
