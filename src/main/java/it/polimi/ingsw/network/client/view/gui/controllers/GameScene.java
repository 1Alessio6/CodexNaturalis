package it.polimi.ingsw.network.client.view.gui.controllers;

import it.polimi.ingsw.model.board.Availability;
import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.network.client.model.board.ClientPlayground;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;
import it.polimi.ingsw.network.client.view.gui.GUIPlayground;
import it.polimi.ingsw.network.client.view.gui.util.PlayerInfoPane;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

import static java.awt.image.ImageObserver.HEIGHT;
import static java.awt.image.ImageObserver.WIDTH;

public class GameScene extends SceneController {

    @FXML
    private Pane mainPane;

    @FXML
    private Pane playgroundPane;

    private Pane chat;

    private Pane secondPlayerPane;
    private Pane thirdPlayerPane;

    private Pane firstPlayerCards;
    private Pane secondPlayerCards;
    private Pane thirdPlayerCards;


    public GameScene() {
    }


    //todo modify with usernames
    public static Pane initializePlayerCards(Pane playerPane, int cardWidth, int cardHeight, int distanceBetweenCards) {

        double layoutX = 0.0;

        for (int i = 0; i < 3; i++) {
            Rectangle rectangle = new Rectangle(cardWidth, cardHeight);
            rectangle.setFill(new ImagePattern(new Image("gui/png/cards/10-front.png")));
            rectangle.setLayoutX(layoutX);
            playerPane.getChildren().add(rectangle);
            layoutX = layoutX + cardWidth + distanceBetweenCards;
        }

        return playerPane;
    }

    public void initialize() {



        initializePlayerInfoBox();

    }

    private void initializePlayerInfoBox() {

        int distance = 50;
        int layoutX = 70;
        int layoutY = 14;

        for (int i = 0; i < 3; i++) {
            PlayerInfoPane playerInfoPane = new PlayerInfoPane(new ClientPlayer("roberto"));
            Pane pane = playerInfoPane.getPlayerMainPane();
            pane.setLayoutX(layoutX);
            pane.setLayoutY(layoutY);
            mainPane.getChildren().add(pane);
            layoutX = layoutX + 436 + distance;
        }
    }



}
