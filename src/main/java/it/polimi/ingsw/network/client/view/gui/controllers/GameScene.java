package it.polimi.ingsw.network.client.view.gui.controllers;

import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;
import it.polimi.ingsw.network.client.view.gui.util.PlayerInfoPane;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class GameScene extends SceneController {

    @FXML
    private Pane mainPane;

    @FXML
    private Pane playgroundPane;

    private Pane chat;

    @FXML
    private void mainPlayerCards(){

    }


    public GameScene() {
    }


    //todo modify with usernames
    public static void initializePlayerCards(Pane playerPane, List<ClientCard> cards, int cardWidth, int cardHeight, int distanceBetweenCards) {

        double layoutX = 0.0;

        for (int i = 0; i < 3; i++) {
            Rectangle rectangle = new Rectangle(cardWidth, cardHeight);
            ImagePattern backImage = new ImagePattern(new Image(cards.get(i).getBackPath()));
            ImagePattern frontImage = new ImagePattern(new Image(cards.get(i).getFrontPath()));
            rectangle.setFill(frontImage);
            rectangle.setLayoutX(layoutX);

            rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(rectangle.getFill() == frontImage){
                        rectangle.setFill(backImage);
                        return;
                    }
                    rectangle.setFill(frontImage);
                }
            });

            playerPane.getChildren().add(rectangle);
            layoutX = layoutX + cardWidth + distanceBetweenCards;
        }

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

    private ImagePattern getFacePath(String username, int cardHandPosition, Side side){
        String path = gui.getController().getPlayer(username).getPlayerCard(cardHandPosition).getSidePath(side);
        return new ImagePattern(new Image(path));
    }


}
