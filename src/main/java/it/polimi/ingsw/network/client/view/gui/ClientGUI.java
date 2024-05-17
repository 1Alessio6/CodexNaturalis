package it.polimi.ingsw.network.client.view.gui;

import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.view.View;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientGUI extends Application implements View {

    private ClientController controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

    }

    @Override
    public void run(VirtualView client) {

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
}
