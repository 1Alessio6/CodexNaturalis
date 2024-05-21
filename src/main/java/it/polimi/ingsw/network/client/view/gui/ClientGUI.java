package it.polimi.ingsw.network.client.view.gui;

import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.view.View;
import it.polimi.ingsw.network.server.rmi.ServerRMI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class ClientGUI extends Application implements View {

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

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/gui/ClientGUI.fxml")));
        primaryStage.setTitle("Codex Naturalis");

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public ClientController run(VirtualView client) {
        return controller;
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
    public void showStarterPlacement() {

    }

    @Override
    public void showUpdateColor() {

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

    @Override
    public void beginCommandAcquisition() {

    }
}
