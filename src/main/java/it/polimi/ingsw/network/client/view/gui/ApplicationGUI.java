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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * ApplicationGUI runs and represents the client in a Graphical User interface
 */
public class ApplicationGUI extends Application implements View, ClientApplication {

    private Stage primaryStage;

    private SceneController currentSceneController;
    private Client client;
    private ClientController controller;
    private SceneType currentScene;

    private boolean isFullScreen;

    private boolean gameSuspendedDuringSetup;

    private Parent currentRoot;


    /**
     * {@inheritDoc}
     */
    @Override
    public void run(String typeConnection, String clientIp) {
        launch(typeConnection, clientIp);
    }

    /**
     * Runs the <code>ApplicationGUI</code>
     *
     * @param args the necessary arguments for the running
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the GUI
     * Overrides the <code>start(Stage primaryStage)</code> method in the Application class
     *
     * @param primaryStage the primary stage
     * @throws Exception if an exception occurs during the start
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Start the gui");
        isFullScreen = false;
        gameSuspendedDuringSetup = false;
        List<String> args = getParameters().getUnnamed();
        client = ClientMain.createClient(args.get(0), args.get(1));
        controller = new ClientController(client);
        this.primaryStage = primaryStage;
        runView();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runView() {
        // todo. change scene to the one that requires ip/port
        loadScene(SceneType.CONNECTION);
        this.primaryStage.setTitle("Codex Naturalis");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showServerCrash() {
        Platform.runLater(() -> {
            loadScene(SceneType.CRASH);
            //todo set crash message
            currentScene = SceneType.CRASH;
        });
    }

    /**
     * Method used to allow the player to select a username
     */
    public void showSelectUsername() {
        Platform.runLater(() -> {
            loadScene(SceneType.SELECT_USERNAME);
            currentScene = SceneType.SELECT_USERNAME;
        });
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateAfterLobbyCrash() {

        Platform.runLater(() -> {
            loadScene(SceneType.CRASH);
            //todo set crash message
            currentScene = SceneType.CRASH;
            ((CrashScene) currentSceneController).setReason("LOBBY CRASHED");
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateExceedingPlayer() {
        Platform.runLater(() -> {
            loadScene(SceneType.CRASH);
            currentScene = SceneType.CRASH;
            ((CrashScene) currentSceneController).setReason("EXCEEDING PLAYER");
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showInvalidLogin(String details) {
        reportError(details);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateAfterConnection() {
        Platform.runLater(() -> {
            if (currentScene == SceneType.LOBBY) {
                currentScene = SceneType.SETUP;
                loadScene(SceneType.SETUP);
            } else {
                //connection after a crash during setup phase with some players still in setup
                if (controller.getGamePhase() == GamePhase.Setup) {
                    loadScene(SceneType.SETUP);
                    currentScene = SceneType.SETUP;
                    ((SetupScene) currentSceneController).initializeCompletedSetup();
                } else {
                    //connection after a crash with game started case
                    loadScene(SceneType.GAME);
                    currentScene = SceneType.GAME;
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdatePlayerStatus() {
        if (currentScene == SceneType.GAME) {
            ((GameScene)currentSceneController).updatePlayersStatus();
        }
    }

    //todo check if it can be removed
    @Override
    public void showInitialPlayerStatus() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showStarterPlacement(String username) {
        Platform.runLater(() -> {
            assert currentSceneController instanceof SetupScene;
            ((SetupScene) currentSceneController).updateAfterStarterPlace(username);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateColor(String username) {
        System.out.println(username + " has chosen a color");
        Platform.runLater(() -> {
            assert currentSceneController instanceof SetupScene;
            ((SetupScene) currentSceneController).updateAfterColor(username);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateObjectiveCard() {
        System.out.println("objective has been chosen");
        Platform.runLater(() -> {
            assert currentSceneController instanceof SetupScene;
            ((SetupScene) currentSceneController).updateObjectiveCard();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateAfterPlace(String username) {
        Platform.runLater(() -> {
            assert currentSceneController instanceof GameScene;
            ((GameScene) currentSceneController).updateAfterPlace(username);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateAfterDraw(String username) {
        Platform.runLater(() -> {
            assert currentSceneController instanceof GameScene;
            ((GameScene) currentSceneController).updateAfterDraw(username);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateChat() {
        Platform.runLater(() -> {
            Message lastSentMessage = controller.getMessages().getLast();
            assert currentSceneController instanceof GameScene;
            ((GameScene) currentSceneController).receiveMessage(lastSentMessage);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateCurrentPlayer() {
        Platform.runLater(() -> {

            //todo check if the first two if could be removed
            if (currentScene == SceneType.SETUP) {
                currentScene = SceneType.GAME;
                loadScene(SceneType.GAME);
            }

            if (controller.getGamePhase() == GamePhase.End) {
                loadScene(SceneType.END);
                currentScene = SceneType.END;
            }

            if(currentScene == SceneType.GAME){
                if(gameSuspendedDuringSetup){
                    ((GameScene)currentSceneController).updateSuspendedGame();
                }
                else{
                    ((GameScene)currentSceneController).updateCurrentPhase();
                }
                ((GameScene)currentSceneController).updateCurrentPlayerUsername();
            }

        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showUpdateSuspendedGame() {
        Platform.runLater(() -> {
            if(currentScene == SceneType.GAME) {
                ((GameScene) currentSceneController).updateSuspendedGame();
            } else if (currentScene == SceneType.SETUP) {
                gameSuspendedDuringSetup = true;
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showWinners(List<String> winners) {
        Platform.runLater(() -> {
            loadScene(SceneType.END);
            currentScene = SceneType.END;
            assert currentSceneController instanceof EndScene;
            ((EndScene) currentSceneController).showWinners(winners);
        });

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportError(String details) {
        Platform.runLater(() -> {
            currentSceneController.showError(details);
        });
    }

    @Override
    public void showConnectionLost() {
        Platform.runLater(() -> {
            loadScene(SceneType.CRASH);
            currentScene = SceneType.CRASH;
            ((CrashScene) currentSceneController).setReason("CONNECTION LOST");
        });
    }

    public ClientController getController() {
        return controller;
    }

    public VirtualView getClient() {
        return client;
    }

    /**
     * Loads the <code>sceneType</code> scene
     *
     * @param sceneType the <code>sceneType</code> to load
     */
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
            controller.disconnect(controller.getMainPlayerUsername());
            System.exit(0);
        });

        primaryStage.setResizable(false);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        currentRoot = root;
        if (isFullScreen) {
            setFullScreenMode();
        }
    }

    /**
     * Switches the screen to full screen mode
     */
    public void setFullScreenMode() {
        Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
        double width = resolution.getWidth();
        double height = resolution.getHeight();
        double newWidth = width / currentSceneController.getSceneWindowWidth();
        double newHeight = height / currentSceneController.getSceneWindowHeight();
        Scale scale = new Scale(newWidth, newHeight, 0, 0);
        currentRoot.getTransforms().add(scale);
        primaryStage.setFullScreen(true);
        isFullScreen = true;
    }

    /**
     * Switches the screen to window screen mode
     */
    public void setWindowScreenMode() {
        Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
        double width = resolution.getWidth();
        double height = resolution.getHeight();
        double newWidth = currentSceneController.getSceneWindowWidth() / width;
        double newHeight = currentSceneController.getSceneWindowHeight() / height;
        Scale scale = new Scale(newWidth, newHeight, 0, 0);
        currentRoot.getTransforms().add(scale);
        primaryStage.setFullScreen(false);
        isFullScreen = false;
    }


    private void initializeCurrentSceneController() {
        currentSceneController.setGui(this);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public boolean getIsFullScreen() {
        return isFullScreen;
    }

}
