package it.polimi.ingsw.network.client.view.gui.util;

import it.polimi.ingsw.model.Deck.DeckType;
import it.polimi.ingsw.network.client.model.board.ClientBoard;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.network.client.view.gui.util.GUICards.*;
import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.*;

/**
 * BoardPane represents the pane in which golden/resource decks and face up cards resides, along with common objective
 * cards
 */
public class BoardPane {

    private final Pane boardMainPane;
    private Rectangle goldenDeckTopCard;
    private Rectangle resourceDeckTopCard;
    private final List<Rectangle> goldenFaceUp;

    private List<Rectangle> emptySlotsToInitialize;
    private final List<Text> emptySlots;

    private final Pane commonObjectivePane;
    private final List<Rectangle> resourceFaceUp;

    /**
     * Constructs a <code>BoardPane</code> with the <code>clientBoard</code> provided
     *
     * @param clientBoard the playing area to construct
     */
    public BoardPane(ClientBoard clientBoard) {

        emptySlots = new ArrayList<>();
        emptySlotsToInitialize = new ArrayList<>();
        commonObjectivePane = new Pane();
        commonObjectivePane.setPrefSize(295, 120);

        goldenFaceUp = new ArrayList<>();
        resourceFaceUp = new ArrayList<>();
        boardMainPane = new Pane();
        boardMainPane.setPrefSize(295, 500);
        boardMainPane.setLayoutX(24);
        boardMainPane.setLayoutY(227);

        createFaceUpCards(clientBoard.getFaceUpCards());
        createDeck(DeckType.GOLDEN, clientBoard.getGoldenDeckTopBack());
        createDeck(DeckType.RESOURCE, clientBoard.getResourceDeckTopBack());

        boardMainPane.getChildren().addAll(goldenFaceUp);
        boardMainPane.getChildren().addAll(resourceFaceUp);
        boardMainPane.getChildren().add(goldenDeckTopCard);
        boardMainPane.getChildren().add(resourceDeckTopCard);
        //boardMainPane.setBackground(setBackgroundColor("#EEE5BC"));
        initializeBoardCardsPosition(20, 11);
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
        double layoutY = 50.0;

        Text FaceUpTitle = new Text();
        FaceUpTitle.setFont(new Font(CAMBRIA_MATH, 15));
        FaceUpTitle.setLayoutY(layoutY);
        FaceUpTitle.setLayoutX(20);
        FaceUpTitle.setText("Face-up Cards");
        boardMainPane.getChildren().add(FaceUpTitle);


        layoutY = layoutY + 10;

        for (int i = 0; i < 6; i++) {

            layoutX = 20.0;

            switch (i) {
                case 0:
                    resourceFaceUp.getFirst().setLayoutX(layoutX);
                    resourceFaceUp.getFirst().setLayoutY(layoutY);
                    setEmptyText(layoutX + 12, layoutY + 35, "Empty Face-up\n          Card", 0);
                    break;
                case 2:
                    goldenFaceUp.getFirst().setLayoutX(layoutX);
                    goldenFaceUp.getFirst().setLayoutY(layoutY);
                    setEmptyText(layoutX + 12, layoutY + 35, "Empty Face-up\n          Card", 2);
                    break;
                case 4:
                    Text DeckTitle = new Text();
                    DeckTitle.setFont(new Font(CAMBRIA_MATH, 15));
                    DeckTitle.setLayoutY(layoutY + 16);
                    DeckTitle.setLayoutX(20);
                    DeckTitle.setText("Decks");
                    layoutY = layoutY + 25.5;
                    boardMainPane.getChildren().add(DeckTitle);
                    goldenDeckTopCard.setLayoutX(layoutX);
                    goldenDeckTopCard.setLayoutY(layoutY);
                    setEmptyText(layoutX + 15, layoutY + 35, "Empty Golden\n        Deck", 4);
                    break;
            }

            i++;
            layoutX = updateLayoutX(layoutX, horizontalDistance);

            switch (i) {
                case 1:
                    resourceFaceUp.getLast().setLayoutX(layoutX);
                    resourceFaceUp.getLast().setLayoutY(layoutY);
                    setEmptyText(layoutX + 12, layoutY + 35, "Empty Face-up\n          Card", 1);
                    break;

                case 3:
                    goldenFaceUp.getLast().setLayoutX(layoutX);
                    goldenFaceUp.getLast().setLayoutY(layoutY);
                    setEmptyText(layoutX + 12, layoutY + 35, "Empty Face-up\n          Card", 3);
                    break;

                case 5:
                    resourceDeckTopCard.setLayoutX(layoutX);
                    resourceDeckTopCard.setLayoutY(layoutY);
                    setEmptyText(layoutX + 10, layoutY + 35, "Empty Resource\n          Deck", 5);
                    break;
            }

            layoutY = updateLayoutY(layoutY, verticalDistance);
        }
    }

    private void setEmptyText(double layoutX, double layoutY, String text, int index){
        Text emptySlot = new Text(text);
        emptySlot.setFont(new Font(CAMBRIA_MATH,15));
        emptySlot.setLayoutX(layoutX);
        emptySlot.setLayoutY(layoutY);
        boardMainPane.getChildren().add(emptySlot);
        emptySlot.setVisible(false);
        emptySlots.add(index,emptySlot);
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

    private void initializeCommonObjective(List<ClientObjectiveCard> commonObjectives) {
        commonObjectivePane.setLayoutY(383.5);

        Text commonObjectiveTitle = new Text();
        commonObjectiveTitle.setFont(new Font(CAMBRIA_MATH, 15));
        commonObjectiveTitle.setLayoutY(20);
        commonObjectiveTitle.setLayoutX(20);
        commonObjectiveTitle.setText("Common Objectives");
        commonObjectivePane.getChildren().add(commonObjectiveTitle);

        double layoutX = 20;

        for (ClientObjectiveCard clientObjectiveCard : commonObjectives) {
            Rectangle card = createBoardRectangle();
            card.setFill(pathToImage(clientObjectiveCard.getPath()));
            card.setLayoutX(layoutX);
            card.setLayoutY(29.5);
            layoutX = updateLayoutX(layoutX, 11);
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
                if (face != null) {
                    resourceFaceUp.getFirst().setFill(pathToImage(face.getPath()));
                } else {
                    resourceFaceUp.set(0, createEmptySlotPlaceHolder(resourceFaceUp.getFirst()));
                    emptySlotsToInitialize.add(resourceFaceUp.getFirst());
                    emptySlots.get(boardPosition).setVisible(true);
                }
                break;
            case 1:
                if (face != null) {
                    resourceFaceUp.getLast().setFill(pathToImage(face.getPath()));
                } else {
                    resourceFaceUp.set(1, createEmptySlotPlaceHolder(resourceFaceUp.getLast()));
                    emptySlotsToInitialize.add(resourceFaceUp.getLast());
                    emptySlots.get(boardPosition).setVisible(true);
                }
                break;
            case 2:
                if (face != null) {
                    goldenFaceUp.getFirst().setFill(pathToImage(face.getPath()));
                } else {
                    goldenFaceUp.set(0, createEmptySlotPlaceHolder(goldenFaceUp.getFirst()));
                    emptySlotsToInitialize.add(goldenFaceUp.getFirst());
                    emptySlots.get(boardPosition).setVisible(true);
                }
                break;
            case 3:
                if (face != null) {
                    goldenFaceUp.getLast().setFill(pathToImage(face.getPath()));
                } else {
                    goldenFaceUp.set(1, createEmptySlotPlaceHolder(goldenFaceUp.getLast()));
                    emptySlotsToInitialize.add(goldenFaceUp.getLast());
                    emptySlots.get(boardPosition).setVisible(true);
                }
                break;
            case 4:
                if (face != null) {
                    goldenDeckTopCard.setFill(pathToImage(face.getPath()));
                } else {
                    goldenDeckTopCard = createEmptySlotPlaceHolder(goldenDeckTopCard);
                    emptySlotsToInitialize.add(goldenDeckTopCard);
                    emptySlots.get(boardPosition).setVisible(true);
                }
                break;
            case 5:
                if (face != null) {
                    resourceDeckTopCard.setFill(pathToImage(face.getPath()));
                } else {
                    resourceDeckTopCard = createEmptySlotPlaceHolder(resourceDeckTopCard);
                    emptySlotsToInitialize.add(resourceDeckTopCard);
                    emptySlots.get(boardPosition).setVisible(true);
                }
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

        for (int index = 0; index <= 5; index++) {
            updateCardAtIndexPosition(board, index);
        }

    }

    private void updateCardAtIndexPosition(ClientBoard board, int index) {
        switch (index) {
            case 4:
                setNewFace(4, board.getGoldenDeckTopBack());
                break;
            case 5:
                setNewFace(5, board.getResourceDeckTopBack());
                break;
            default:
                if (board.getFaceUpCards().get(index) != null) {
                    setNewFace(index, board.getFaceUpCards().get(index).getFront());
                } else {
                    setNewFace(index, null);
                }
                break;
        }
    }

    private Rectangle createEmptySlotPlaceHolder(Rectangle oldFace) {

        Rectangle emptyFace = new Rectangle(boardCardsWidth, boardCardsHeight);
        emptyFace.setOpacity(0.4);
        emptyFace.setFill(Color.web("#B0E0E6"));
        emptyFace.setLayoutX(oldFace.getLayoutX());
        emptyFace.setLayoutY(oldFace.getLayoutY());
        boardMainPane.getChildren().remove(oldFace);
        boardMainPane.getChildren().add(emptyFace);

        return emptyFace;
    }

    public List<Rectangle> getEmptySlotsToInitialize() {
        return emptySlotsToInitialize;
    }

    public void setEmptySlotsToInitialize(List<Rectangle> emptySlotsToInitialize){
        this.emptySlotsToInitialize = emptySlotsToInitialize;
    }

    private double updateLayoutX(double layoutX, int distance) {
        return layoutX + boardCardsWidth + distance;
    }

    private double updateLayoutY(double layoutY, int distance) {
        return layoutY + boardCardsHeight + distance;
    }
}
