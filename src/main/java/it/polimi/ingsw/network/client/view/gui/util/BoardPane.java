package it.polimi.ingsw.network.client.view.gui.util;

import it.polimi.ingsw.model.Deck.DeckType;
import it.polimi.ingsw.network.client.model.board.ClientBoard;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.network.client.view.gui.util.GUICards.*;
import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.setBackgroundColor;

/**
 * BoardPane represents the pane in which golden/resource decks and face up cards resides, along with common objective
 * cards
 */
public class BoardPane {

    private Pane boardMainPane;
    private Rectangle goldenDeckTopCard;
    private Rectangle resourceDeckTopCard;
    private final List<Rectangle> goldenFaceUp;

    private final Pane commonObjectivePane;
    private final List<Rectangle> resourceFaceUp;

    /**
     * Constructs a <code>BoardPane</code> with the <code>clientBoard</code> provided
     *
     * @param clientBoard the playing area to construct
     */
    public BoardPane(ClientBoard clientBoard) {

        commonObjectivePane = new Pane();
        commonObjectivePane.setPrefSize(295,120);

        goldenFaceUp = new ArrayList<>();
        resourceFaceUp = new ArrayList<>();
        boardMainPane = new Pane();
        boardMainPane.setPrefSize(295, 500);
        boardMainPane.setLayoutX(24);
        boardMainPane.setLayoutY(217);

        createFaceUpCards(clientBoard.getFaceUpCards());
        createDeck(DeckType.GOLDEN, clientBoard.getGoldenDeckTopBack());
        createDeck(DeckType.RESOURCE, clientBoard.getResourceDeckTopBack());

        boardMainPane.getChildren().addAll(goldenFaceUp);
        boardMainPane.getChildren().addAll(resourceFaceUp);
        boardMainPane.getChildren().add(goldenDeckTopCard);
        boardMainPane.getChildren().add(resourceDeckTopCard);
        boardMainPane.setBackground(setBackgroundColor("#EEE5BC"));
        initializeBoardCardsPosition(34, 11);
        initializeCommonObjective(clientBoard.getCommonObjectives());
        boardMainPane.getChildren().add(commonObjectivePane);

    }

    /**
     * Initializes the positions in which the face up cards and the golden/resource deck are
     *
     * @param verticalDistance   the vertical distance between the cards
     * @param horizontalDistance the horizontal distance between the cards
     */
    public void initializeBoardCardsPosition(int verticalDistance, int horizontalDistance) {

        double layoutX;
        double layoutY = 30.0;

        for (int i = 0; i < 6; i++) {

            layoutX = 20.0;

            switch (i) {
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
                    goldenDeckTopCard.setLayoutY(layoutY);
                    break;
            }

            i++;
            layoutX = updateLayoutX(layoutX, horizontalDistance);

            switch (i) {
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
                    resourceDeckTopCard.setLayoutY(layoutY);
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

    private void initializeCommonObjective(List<ClientObjectiveCard> commonObjectives){
        commonObjectivePane.setLayoutY(383.5);

        Text commonObjectiveTitle = new Text();
        commonObjectiveTitle.setFont(new Font(GUIUtil.CAMBRIA_MATH,15));
        commonObjectiveTitle.setLayoutY(2);
        commonObjectiveTitle.setLayoutX(70);
        commonObjectiveTitle.setText("COMMON OBJECTIVE");
        commonObjectivePane.getChildren().add(commonObjectiveTitle);

        double layoutX = 20;

        for(ClientObjectiveCard clientObjectiveCard : commonObjectives){
            Rectangle card = createBoardRectangle();
            card.setFill(pathToImage(clientObjectiveCard.getPath()));
            card.setLayoutX(layoutX);
            card.setLayoutY(29.5);
            layoutX = updateLayoutX(layoutX,11);
            commonObjectivePane.getChildren().add(card);
        }
    }

    private Rectangle createBoardRectangle() {
        return new Rectangle(boardCardsWidth, boardCardsHeight);
    }

    private void createFaceUpCards(List<ClientCard> faceUp) {
        for (int i = 0; i < 4; i++) {
            Rectangle rectangle = createBoardRectangle();
            rectangle.setFill(pathToImage(faceUp.get(i).getFrontPath()));
            if (i <= 1) {
                resourceFaceUp.add(rectangle);
            } else {
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

    /**
     * Sets a new <code>face</code> in accordance with the <code>boardPosition</code> provided
     *
     * @param boardPosition the position of the <code>face</code> to update
     * @param face          the new face with which to update
     */
    public void setNewFace(int boardPosition, ClientFace face) {


        switch (boardPosition) {
            case 0:
                resourceFaceUp.getFirst().setFill(pathToImage(face.getPath()));
                break;
            case 1:
                resourceFaceUp.getLast().setFill(pathToImage(face.getPath()));
                break;
            case 2:
                goldenFaceUp.getFirst().setFill(pathToImage(face.getPath()));
                break;
            case 3:
                goldenFaceUp.getLast().setFill(pathToImage(face.getPath()));
                break;
            case 4:
                goldenDeckTopCard.setFill(pathToImage(face.getPath()));
                break;
            case 5:
                resourceDeckTopCard.setFill(pathToImage(face.getPath()));
                break;
            default:
                System.err.println("not valid id");
                break;
        }
    }

    /**
     * Updates the <code>ClientBoard</code> with board provided
     *
     * @param board the <code>ClientBoard</code> with the new cards with which to update
     */
    public void boardUpdate(ClientBoard board) {

        setNewFace(4, board.getGoldenDeckTopBack());
        setNewFace(5, board.getResourceDeckTopBack());
        setNewFace(0, board.getFaceUpCards().get(0).getFront());
        setNewFace(1, board.getFaceUpCards().get(1).getFront());
        setNewFace(2, board.getFaceUpCards().get(2).getFront());
        setNewFace(3, board.getFaceUpCards().get(3).getFront());
    }

    private double updateLayoutX(double layoutX, int distance) {
        return layoutX + boardCardsWidth + distance;
    }

    private double updateLayoutY(double layoutY, int distance) {
        return layoutY + boardCardsHeight + distance;
    }
}
