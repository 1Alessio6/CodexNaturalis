package it.polimi.ingsw.network.client.view.gui.util;

import it.polimi.ingsw.network.client.model.card.ClientCard;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class GUICards {

    public static final int boardCardsWidth = 126;

    public static final int boardCardsHeight = 81;

    public static final int playerCardsWidth = 151;

    public static final int playerCardsHeight = 98;



    //todo modify with usernames
    public static void initializePlayerCards(Pane playerPane, List<ClientCard> cards, int cardWidth, int cardHeight, int distanceBetweenCards, MouseButton flipButton) {

        double layoutX = 0.0;

        for (ClientCard card : cards) {
            Rectangle rectangle = new Rectangle(cardWidth, cardHeight);
            ImagePattern backImage = new ImagePattern(new Image(card.getBackPath()));
            ImagePattern frontImage = new ImagePattern(new Image(card.getFrontPath()));
            flipCard(rectangle, flipButton, frontImage, backImage);
            rectangle.setFill(frontImage);
            rectangle.setLayoutX(layoutX);
            playerPane.getChildren().add(rectangle);
            layoutX = layoutX + cardWidth + distanceBetweenCards;
        }

    }

    public static void flipCard(Rectangle rectangle, MouseButton mouseButton, ImagePattern frontImage, ImagePattern backImage) {

        if(mouseButton == null){
            return;
        }
        rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED && mouseEvent.getButton() == mouseButton) {
                    if (rectangle.getFill() == frontImage) {
                        rectangle.setFill(backImage);
                        return;
                    }
                    rectangle.setFill(frontImage);

                }
            }

        });
    }

    public static ImagePattern pathToImage(String path){
        return new ImagePattern(new Image(path));
    }

}
