package it.polimi.ingsw.network.client.view.gui;

public enum SceneType {
    SELECT_USERNAME("/gui/SelectUsernameScene.fxml"),

    CONNECTION("/gui/ConnectionScene.fxml"),

    LOBBY("/gui/LobbyScene.fxml"),

    SETUP("/gui/SetupScene.fxml"),

    GAME("/gui/GameScene.fxml"),

    CRASH("/gui/CrashScene.fxml"),

    END("/gui/SelectUsernameScene.fxml");

    private final String path;

    SceneType(String path) {
        this.path = path;
    }

    public String getPath(){
        return path;
    }
}
