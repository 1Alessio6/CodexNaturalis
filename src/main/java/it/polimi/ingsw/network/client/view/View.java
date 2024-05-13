package it.polimi.ingsw.network.client.view;

import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.controller.ClientController;

import java.util.List;


public abstract class View {

    private ClientController controller;

    public View(ClientController controller) {
        this.controller = controller;
    }

    //method used to read client input
    public abstract void run(VirtualView client);

    //method used to update after a controller update, invoked by the client class

    public abstract void showUpdatePlayersInLobby(List<String> usernames);

    public abstract void showUpdateCreator();

    public abstract void showUpdateAfterLobbyCrash();

    public abstract void showUpdateAfterConnection();

    public abstract void showUpdatePlayerStatus();

    public abstract void showInitialPlayerStatus();

    public abstract void showBoardSetUp();

    public abstract void showStarterPlacement();

    public abstract void showUpdateColor(PlayerColor color, String username);

    public abstract void showUpdateObjectiveCard();

    public abstract void showUpdateAfterPlace();

    public abstract void showUpdateAfterDraw();

    public abstract void showUpdateChat();

    public abstract void showUpdateCurrentPlayer();

    public abstract void showUpdateSuspendedGame();

    public abstract void showWinners();

    public ClientController getController() {
        return controller;
    }
}
