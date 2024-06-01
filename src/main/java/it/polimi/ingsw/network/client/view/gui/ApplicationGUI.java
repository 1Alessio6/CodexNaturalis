package it.polimi.ingsw.network.client.view.gui;

import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientApplication;
import it.polimi.ingsw.network.client.ClientMain;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.view.View;
import it.polimi.ingsw.network.client.view.gui.controllers.LobbyScene;
import it.polimi.ingsw.network.client.view.gui.controllers.SceneController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ApplicationGUI extends Application implements View, ClientApplication {

    private Stage primaryStage;

    private SceneController currentSceneController;
    private VirtualView client;
    private final ClientController controller;
    private SceneType currentScene;

    private Parent currentRoot;

    public ApplicationGUI() {
        super();
        this.controller = controller;
    }
    */


    @Override
    public void run(String typeConnection, String host, String port) {
        launch(typeConnection, host, port);
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        //add all controllers constructors

        //FXMLLoader loader = loadScene("/gui/SelectUsernameScene.fxml");
        loadScene(SceneType.GAME);
        /*
        SelectUsernameScene sceneController = loader.getController();

         */

        initializeCurrentSceneController();
        //((LobbyScene)currentSceneController).initializeCreatorScene();


        //((GameScene)currentSceneController).drawPlayground(null);

        this.primaryStage.setTitle("Codex Naturalis");
        this.primaryStage.show();


    }

    @Override
    public ClientController run(VirtualView client) {
        this.client = client;
        return controller;
    }

    @Override
    public void beginCommandAcquisition() {

    }


    @Override
    public void showServerCrash() {
        loadScene(SceneType.CRASH);
        //todo set crash message
        currentScene = SceneType.CRASH;
    }

    @Override
    public void showUpdatePlayersInLobby() {
        if (currentScene != SceneType.LOBBY) {
            loadScene(SceneType.LOBBY);
            currentScene = SceneType.LOBBY;
        }
        assert currentSceneController instanceof LobbyScene;
        ((LobbyScene) currentSceneController).setPlayerConnected(controller.getConnectedUsernames());

    }

    @Override
    public void showUpdateCreator() {

        loadScene(SceneType.LOBBY);
        ((LobbyScene) currentSceneController).initializeCreatorScene();
        currentScene = SceneType.LOBBY;
    }


    @Override
    public void showUpdateAfterLobbyCrash() {
        loadScene(SceneType.CRASH);
        //todo set crash message
        currentScene = SceneType.CRASH;
    }

    @Override
    public void showUpdateAfterConnection() {
        loadScene(SceneType.GAME);
        currentScene = SceneType.GAME;
    }

    @Override
    public void showUpdatePlayerStatus() {

        //todo add update without loading a new scene

    }

    //todo check if it can be removed
    @Override
    public void showInitialPlayerStatus() {

    }


    @Override
    public void showStarterPlacement(String username) {
        //todo add update without loading a new scene
    }

    @Override
    public void showUpdateColor(String username) {
        //todo add update without loading a new scene
    }

    @Override
    public void showUpdateObjectiveCard() {
        //todo add update without loading a new scene
    }

    @Override
    public void showUpdateAfterPlace(String username) {
        //todo add update without loading a new scene
    }

    @Override
    public void showUpdateAfterDraw() {
        //todo add update without loading a new scene
    }

    @Override
    public void showUpdateChat() {
        //todo add a dropdown menu for chat
    }

    @Override
    public void showUpdateCurrentPlayer() {
        if (controller.getGamePhase() == GamePhase.End) {
            loadScene(SceneType.END);
            currentScene = SceneType.END;
        } else {
            //todo update current player in normal game scene
        }
    }

    @Override
    public void showUpdateSuspendedGame() {
        //todo show a message of suspended game and disable all other command
    }

    @Override
    public void showWinners(List<String> winners) {
        loadScene(SceneType.END);
        currentScene = SceneType.END;
        //todo add show winners
    }

    public ClientController getController() {
        return controller;
    }

    public VirtualView getClient() {
        return client;
    }

    public void loadScene(SceneType sceneType) {

        String fxmlPath = sceneType.getPath();

        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

        try {
            root = loader.load();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.setTitle("IOException");
            alert.show();
            return;
        }
        primaryStage.setTitle("Codex Naturalis");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.show();
        currentSceneController = loader.getController();
        initializeCurrentSceneController();

        currentRoot = root;
    }





    private void initializeCurrentSceneController() {
        currentSceneController.setGui(this);
    }

    private void setCurrentScene(SceneType scene) {
        currentScene = scene;
    }



}
