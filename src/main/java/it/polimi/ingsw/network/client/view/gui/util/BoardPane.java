package it.polimi.ingsw.network.client.view.gui.util;

import it.polimi.ingsw.model.Deck.DeckType;
import it.polimi.ingsw.network.client.model.board.ClientBoard;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;


import java.util.List;

public class BoardPane {

    private Pane boardMainPane;
    private Rectangle goldenDeckTopCard;
    private Rectangle resourceDeckTopCard;

    private List<Rectangle> goldenFaceUp;

    private List<Rectangle> resourceFaceUp;

    public BoardPane(ClientBoard clientBoard) {
        Pane BoardMainPane = new Pane();
        BoardMainPane.setPrefSize(413, 310);
        BoardMainPane.setLayoutX(24);
        BoardMainPane.setLayoutY(268);



        initializeBoardCardsPosition(34,11);

    }


    public void initializeBoardCardsPosition(int verticalDistance, int horizontalDistance) {

        double layoutX = 20.0;
        double layoutY = 82.0;

        for(int i = 0; i < 6; i++) {

            layoutX = 20.0;

            switch (i){
                case 0:
                    resourceFaceUp.get(i).setLayoutX(layoutX);
                    resourceFaceUp.get(i).setLayoutY(layoutY);

                case 2:
                    goldenFaceUp.get(i).setLayoutX(layoutX);
                    goldenFaceUp.get(i).setLayoutY(layoutY);

                case 4:
                    goldenDeckTopCard.setLayoutX(layoutX);
                    goldenDeckTopCard.setLayoutX(layoutY);
            }

            i++;
            layoutX = updateLayoutX(layoutX, horizontalDistance);

            switch (i){
                case 1:
                    resourceFaceUp.get(i).setLayoutX(layoutX);
                    resourceFaceUp.get(i).setLayoutY(layoutY);

                case 3:
                    goldenFaceUp.get(i).setLayoutX(layoutX);
                    goldenFaceUp.get(i).setLayoutY(layoutY);

                case 5:
                    resourceDeckTopCard.setLayoutX(layoutX);
                    resourceDeckTopCard.setLayoutX(layoutY);
            }

            layoutY = updateLayoutY(layoutY, verticalDistance);
        }
    }

    private Rectangle createDeck(DeckType Type, ClientFace face) {
        if (Type == DeckType.GOLDEN) {
            goldenDeckTopCard = createBoardRectangle();
            goldenDeckTopCard.setFill(new ImagePattern(new Image(face.getPath())));
            return goldenDeckTopCard;

        } else {
            resourceDeckTopCard = createBoardRectangle();
            resourceDeckTopCard.setFill(new ImagePattern(new Image(face.getPath())));
            return resourceDeckTopCard;
        }
    }

    private Rectangle createBoardRectangle() {
        return new Rectangle(GUICards.boardCardsWidth, GUICards.boardCardsHeight);
    }



    public Pane getBoardMainPane() {
        return boardMainPane;
    }

    public Rectangle getGoldenDeckTopCard() {
        return goldenDeckTopCard;
    }

    public Rectangle getResourceDeckTopCard() {
        return resourceDeckTopCard;
    }

    public List<Rectangle> getResourceFaceUp() {
        return resourceFaceUp;
    }

    public List<Rectangle> getGoldenFaceUp() {
        return goldenFaceUp;
    }

    public void setDeckTopCard(DeckType type, ClientFace face) {
        if (type.equals(DeckType.GOLDEN)) {
            goldenDeckTopCard.setFill(GUICards.pathToImage(face.getPath()));
        } else {
            resourceDeckTopCard.setFill(GUICards.pathToImage(face.getPath()));
        }
    }

    public void setNewFace(int boardPosition, ClientFace face) {
        switch (boardPosition) {
            case 0:
                resourceFaceUp.get(0).setFill(GUICards.pathToImage(face.getPath()));
            case 1:
                resourceFaceUp.get(1).setFill(GUICards.pathToImage(face.getPath()));
            case 2:
                goldenFaceUp.get(0).setFill(GUICards.pathToImage(face.getPath()));
            case 3:
                goldenFaceUp.get(1).setFill(GUICards.pathToImage(face.getPath()));
            case 4:
                goldenDeckTopCard.setFill(GUICards.pathToImage(face.getPath()));
            case 5:
                resourceDeckTopCard.setFill(GUICards.pathToImage(face.getPath()));
        }
    }

    private double updateLayoutX(double layoutX, int distance) {
        return layoutX + GUICards.boardCardsWidth + distance;
    }

    private double updateLayoutY(double layoutY, int distance) {
        return layoutY + GUICards.boardCardsHeight + distance;
    }
}
