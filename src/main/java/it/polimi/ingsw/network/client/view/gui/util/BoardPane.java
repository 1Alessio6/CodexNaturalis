package it.polimi.ingsw.network.client.view.gui.util;

import it.polimi.ingsw.model.Deck.DeckType;
import it.polimi.ingsw.network.client.model.board.ClientBoard;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;


import java.util.ArrayList;
import java.util.List;

public class BoardPane {

    private Pane boardMainPane;
    private Rectangle goldenDeckTopCard;
    private Rectangle resourceDeckTopCard;

    private final List<Rectangle> goldenFaceUp;

    private final List<Rectangle> resourceFaceUp;

    public BoardPane(ClientBoard clientBoard) {
        goldenFaceUp = new ArrayList<>();
        resourceFaceUp = new ArrayList<>();
        boardMainPane = new Pane();
        boardMainPane.setPrefSize(413, 600);
        boardMainPane.setLayoutX(24);
        boardMainPane.setLayoutY(268);

        createFaceUpCards(clientBoard.getFaceUpCards());
        createDeck(DeckType.GOLDEN, clientBoard.getGoldenDeckTopBack());
        createDeck(DeckType.RESOURCE, clientBoard.getResourceDeckTopBack());

        initializeBoardCardsPosition(34,11);

        boardMainPane.getChildren().addAll(goldenFaceUp);
        boardMainPane.getChildren().addAll(resourceFaceUp);
        boardMainPane.getChildren().add(goldenDeckTopCard);
        boardMainPane.getChildren().add(resourceDeckTopCard);

    }


    public void initializeBoardCardsPosition(int verticalDistance, int horizontalDistance) {

        double layoutX;
        double layoutY = 82.0;

        for(int i = 0; i < 6; i++) {

            layoutX = 20.0;

            switch (i){
                case 0:
                    resourceFaceUp.getFirst().setLayoutX(layoutX);
                    resourceFaceUp.getFirst().setLayoutY(layoutY);
                    break;
                case 2:
                    goldenFaceUp.getFirst().setLayoutX(layoutX);
                    goldenFaceUp.getFirst().setLayoutY(layoutY);
                    break;
                case 4:

                    goldenDeckTopCard.setLayoutX(layoutX);
                    goldenDeckTopCard.setLayoutX(layoutY);
                    break;
            }

            i++;
            layoutX = updateLayoutX(layoutX, horizontalDistance);

            switch (i){
                case 1:
                    resourceFaceUp.getLast().setLayoutX(layoutX);
                    resourceFaceUp.getLast().setLayoutY(layoutY);
                    break;

                case 3:
                    goldenFaceUp.getLast().setLayoutX(layoutX);
                    goldenFaceUp.getLast().setLayoutY(layoutY);
                    break;

                case 5:
                    resourceDeckTopCard.setLayoutX(layoutX);
                    resourceDeckTopCard.setLayoutX(layoutY);
                    break;
            }

            layoutY = updateLayoutY(layoutY, verticalDistance);
        }
    }

    private void createDeck(DeckType Type, ClientFace face) {
        if (Type == DeckType.GOLDEN) {
            goldenDeckTopCard = createBoardRectangle();
            goldenDeckTopCard.setFill(new ImagePattern(new Image(face.getPath())));

        } else {
            resourceDeckTopCard = createBoardRectangle();
            resourceDeckTopCard.setFill(new ImagePattern(new Image(face.getPath())));
        }
    }

    private Rectangle createBoardRectangle() {
        return new Rectangle(GUICards.boardCardsWidth, GUICards.boardCardsHeight);
    }

    private void createFaceUpCards(List<ClientCard> faceUp){
        for(int i = 0; i < 4; i++) {
            Rectangle rectangle = createBoardRectangle();
            rectangle.setFill(GUICards.pathToImage(faceUp.get(i).getFrontPath()));
            if(i <= 1 ){
                resourceFaceUp.add(rectangle);
            }
            else{
                goldenFaceUp.add(rectangle);
            }
        }
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
