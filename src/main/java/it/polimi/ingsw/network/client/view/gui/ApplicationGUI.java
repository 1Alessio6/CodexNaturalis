package it.polimi.ingsw.network.client.view.gui;

import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.ClientApplication;
import it.polimi.ingsw.network.client.ClientMain;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.view.View;
import it.polimi.ingsw.network.client.view.gui.controllers.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

public class ApplicationGUI extends Application implements View, ClientApplication {

    private Stage primaryStage;

    private SceneController currentSceneController;
    private Client client;
    private ClientController controller;
    private SceneType currentScene;

    private Parent currentRoot;

    @Override
    public void run(String typeConnection) {
        launch(typeConnection);
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Start the gui");
        client = ClientMain.createClient(getParameters().getUnnamed().getFirst());
        controller = new ClientController(client);
        this.primaryStage = primaryStage;
        runView();
    }

    @Override
    public void runView() {
        // todo. change scene to the one that requires ip/port
        loadScene(SceneType.CONNECTION);
        this.primaryStage.setTitle("Codex Naturalis");
    }

    @Override
    public void showServerCrash() {
        Platform.runLater(() -> {
            loadScene(SceneType.CRASH);
            //todo set crash message
            currentScene = SceneType.CRASH;
        });
    }

    public void showSelectUsername() {
        Platform.runLater(() -> {
            loadScene(SceneType.SELECT_USERNAME);
            currentScene = SceneType.SELECT_USERNAME;
        });
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

        Platform.runLater(() -> {
            loadScene(SceneType.CRASH);
            //todo set crash message
            currentScene = SceneType.CRASH;
        });
    }

    //todo fix bug after multiple cards in hand after a connection after a crash
    @Override
    public void showUpdateAfterConnection() {
        Platform.runLater(() -> {
            if (currentScene == SceneType.LOBBY) {
                currentScene = SceneType.SETUP;
                loadScene(SceneType.SETUP);
            } else {
                loadScene(SceneType.GAME);
                currentScene = SceneType.GAME;
            }
        });
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
        Platform.runLater(() -> {
            assert currentSceneController instanceof SetupScene;
            ((SetupScene) currentSceneController).updateAfterStarterPlace(username);
        });
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
        Platform.runLater(() -> {
            assert currentSceneController instanceof GameScene;
            ((GameScene) currentSceneController).updateAfterPlace(username);
        });
    }

    @Override
    public void showUpdateAfterDraw(String username) {
        Platform.runLater(() -> {
            assert currentSceneController instanceof GameScene;
            ((GameScene) currentSceneController).updateAfterDraw(username);
        });
    }

    @Override
    public void showUpdateChat() {
        Platform.runLater(() -> {
            Message lastSentMessage = controller.getMessages().getLast();
            assert currentSceneController instanceof GameScene;
            ((GameScene) currentSceneController).receiveMessage(lastSentMessage);
        });
    }

    @Override
    public void showUpdateCurrentPlayer() {
        Platform.runLater(() -> {
            if (currentScene == SceneType.SETUP) {
                currentScene = SceneType.GAME;
                loadScene(SceneType.GAME);
            }

            if (controller.getGamePhase() == GamePhase.End) {
                loadScene(SceneType.END);
                currentScene = SceneType.END;
            } else {
                //todo update current player in normal game scene
            }
        });
    }

    @Override
    public void showUpdateSuspendedGame() {
        //todo show a message of suspended game and disable all other command
    }

    @Override
    public void showWinners(List<String> winners) {
        Platform.runLater(() -> {
            loadScene(SceneType.END);
            currentScene = SceneType.END;
            assert currentSceneController instanceof EndScene;
            ((EndScene) currentSceneController).showWinners(winners);
        });

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
            alert.setTitle("Load scene error");
            alert.show();
            return;
        }
        primaryStage.setTitle("Codex Naturalis");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        currentSceneController = loader.getController();
        initializeCurrentSceneController();
        currentSceneController.initializeUsingGameInformation();
        primaryStage.show();
        primaryStage.setOnCloseRequest(windowEvent -> {
            try {
                controller.disconnect(controller.getMainPlayerUsername());
            } catch (RemoteException ignored) {

            }
            System.exit(0);
        });

        primaryStage.setResizable(false);


        //todo maybe could be removed currentRoot
        currentRoot = root;
    }


    private void initializeCurrentSceneController() {
        currentSceneController.setGui(this);
    }

    private void setCurrentScene(SceneType scene) {
        currentScene = scene;
    }


}
