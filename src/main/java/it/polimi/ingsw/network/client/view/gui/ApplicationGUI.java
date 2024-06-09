package it.polimi.ingsw.network.client.view.gui;

import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientApplication;
import it.polimi.ingsw.network.client.ClientMain;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.view.View;
import it.polimi.ingsw.network.client.view.gui.controllers.GameScene;
import it.polimi.ingsw.network.client.view.gui.controllers.LobbyScene;
import it.polimi.ingsw.network.client.view.gui.controllers.SceneController;
import it.polimi.ingsw.network.client.view.gui.controllers.SetupScene;
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
    private Client client;
    private ClientController controller;
    private SceneType currentScene;

    private Parent currentRoot;

    @Override
    public void run(String typeConnection, String host, String port) {
        launch(typeConnection, host, port);
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Start the gui");
        System.out.println("Prepare everything for the gui");
        this.client = ClientMain.createClient(getParameters().getUnnamed());
        this.controller = client.getController();
        this.primaryStage = primaryStage;
        client.addView(this);
        client.runView();


//        this.primaryStage = primaryStage;
//        //add all controllers constructors
//
//        //FXMLLoader loader = loadScene("/gui/SelectUsernameScene.fxml");
//        loadScene(SceneType.SELECT_USERNAME);
//        /*
//        SelectUsernameScene sceneController = loader.getController();
//
//         */
//
//        //  initializeCurrentSceneController();
//        //((LobbyScene)currentSceneController).initializeCreatorScene();
//
//
//        //((GameScene)currentSceneController).drawPlayground(null);
//
//        this.primaryStage.setTitle("Codex Naturalis");
//        this.primaryStage.show();
    }

    @Override
    public void runView(VirtualView client) {
        loadScene(SceneType.SELECT_USERNAME);
        this.primaryStage.setTitle("Codex Naturalis");
        this.primaryStage.show();
    }

    @Override
    public void showServerCrash() {
        loadScene(SceneType.CRASH);
        //todo set crash message
        currentScene = SceneType.CRASH;
    }

    @Override
    public void showUpdatePlayersInLobby() {
        Platform.runLater(() -> {
            if (currentScene != SceneType.LOBBY) {
                loadScene(SceneType.LOBBY);
                currentScene = SceneType.LOBBY;
            }
            assert currentSceneController instanceof LobbyScene;
            ((LobbyScene) currentSceneController).setPlayerConnected(controller.getConnectedUsernames());
        });
    }

    @Override
    public void showUpdateCreator() {
        System.out.println("Arrived notification from the server to the creator");
        System.out.println("Address: " + this);
        Platform.runLater(() -> {
            loadScene(SceneType.LOBBY);
            ((LobbyScene) currentSceneController).initializeCreatorScene();
            currentScene = SceneType.LOBBY;
        });
    }


    @Override
    public void showUpdateAfterLobbyCrash() {
        loadScene(SceneType.CRASH);
        //todo set crash message
        currentScene = SceneType.CRASH;
    }

    @Override
    public void showUpdateAfterConnection() {
        if(currentScene  == SceneType.LOBBY){
            currentScene = SceneType.SETUP;
            loadScene(SceneType.SETUP);
        }
        else{
            loadScene(SceneType.GAME);
            currentScene = SceneType.GAME;
        }
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
        assert currentSceneController instanceof SetupScene;
        ((SetupScene)currentSceneController).updateAfterStarterPlace();
    }

    @Override
    public void showUpdateColor(String username) {
        System.out.println(username + " has chosen a color");
        Platform.runLater(() -> {
            ((SetupScene) currentSceneController).updateAfterColor(username);
        });
    }

    @Override
    public void showUpdateObjectiveCard() {
        System.out.println("objective has been chosen");
        Platform.runLater(() -> {
            ((SetupScene) currentSceneController).updateObjectiveCard();
        });
    }

    @Override
    public void showUpdateAfterPlace(String username) {
        assert currentSceneController instanceof GameScene;
        ((GameScene)currentSceneController).updateAfterPlace(username);
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
        if(currentScene == SceneType.SETUP){
            currentScene = SceneType.GAME;
            loadScene(SceneType.GAME);
        }

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
