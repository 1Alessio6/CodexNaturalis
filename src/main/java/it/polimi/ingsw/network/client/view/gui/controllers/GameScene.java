package it.polimi.ingsw.network.client.view.gui.controllers;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class GameScene extends SceneController{

    @FXML
    private Pane playgroundPane;

    private Pane chat;

    private Pane firstPlayerPane;
    private Pane secondPlayerPane;
    private Pane thirdPlayerPane;

    private Pane firstPlayerCards;
    private Pane secondPlayerCards;
    private Pane thirdPlayerCards;


    public GameScene(){}

}
