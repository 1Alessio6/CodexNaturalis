package it.polimi.ingsw.network.client.view.gui;

import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.view.View;
import it.polimi.ingsw.network.client.view.gui.controllers.SelectUsernameScene;
import it.polimi.ingsw.network.server.rmi.ServerRMI;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Objects;

public class ClientGUI extends Application implements View {

    private Stage primaryStage;

    private VirtualView client;
    private ClientController controller;

    /*
    public ClientGUI(ClientController controller){
        super();
        this.controller = controller;
    }
    */

    //constructor used ONLY for test
    public ClientGUI(){
        super();
        this.controller = new ClientController(new ServerRMI());
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        //add all controllers constructors
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/gui/SelectUsernameScene.fxml")));
        Parent root = loader.load();
        System.out.println(loader.getLocation());

        SelectUsernameScene sceneController = loader.getController();
        sceneController.setGui(this);
        primaryStage.setTitle("Codex Naturalis");

        Scene scene = new Scene(root);
        this.primaryStage.setScene(scene);
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

    }

    @Override
    public void showUpdatePlayersInLobby() {

    }

    @Override
    public void showUpdateCreator() {

    }

    @Override
    public void showUpdateAfterLobbyCrash() {

    }

    @Override
    public void showUpdateAfterConnection() {

    }

    @Override
    public void showUpdatePlayerStatus() {

    }

    @Override
    public void showInitialPlayerStatus() {

    }

    @Override
    public void showBoardSetUp() {

    }

    @Override
    public void showStarterPlacement(String username) {

    }

    @Override
    public void showUpdateColor(String username) {

    }

    @Override
    public void showUpdateObjectiveCard() {

    }

    @Override
    public void showUpdateAfterPlace() {

    }

    @Override
    public void showUpdateAfterDraw() {

    }

    @Override
    public void showUpdateChat() {

    }

    @Override
    public void showUpdateCurrentPlayer() {

    }

    @Override
    public void showUpdateSuspendedGame() {

    }

    @Override
    public void showWinners() {

    }

    public ClientController getController() {
        return controller;
    }

    public VirtualView getClient() {
        return client;
    }

    public void loadScene(String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
        primaryStage.setTitle("Codex Naturalis");

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
