package it.polimi.ingsw.network.client.view;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.CornerPosition;
import it.polimi.ingsw.model.card.Symbol;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.model.ClientGame;
import it.polimi.ingsw.network.client.model.ClientPhase;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public abstract class View {

    private ClientController controller;

    //method used to read client input
    public abstract void run();

    //method used to update after a controller update, invoked by the client class

    public abstract void updateCreator();

    public abstract void updateAfterLobbyCrash();

    public abstract void updateAfterConnection();

    public abstract void showUpdatePlayerStatus();

    public abstract void showInitialPlayerStatus();

    public abstract void showBoardSetUp();

    public abstract void showStarterPlacement();

    public abstract void showUpdateColor();

    public abstract void showUpdateObjectiveCard();

    public abstract void showUpdateAfterPlace();

    public abstract void showUpdateAfterDraw();

    public abstract void showUpdateChat();

    public abstract void showUpdateCurrentPlayer();

    public abstract void showUpdateSuspendedGame();

    public abstract void showWinners();

}
