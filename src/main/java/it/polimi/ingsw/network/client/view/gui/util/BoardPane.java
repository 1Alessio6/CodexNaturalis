package it.polimi.ingsw.network.client.view.gui.util;

import it.polimi.ingsw.model.Deck.DeckType;
import it.polimi.ingsw.network.client.model.board.ClientBoard;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;


import java.util.List;

public class BoardPane {

    private Pane BoardMainPane;
    private Rectangle goldenDeckTopCard;
    private Rectangle resourceDeckTopCard;

    private List<Rectangle> goldenFaceUp;

    private List<Rectangle> resourceFaceUp;

    public BoardPane(ClientBoard clientBoard) {



    }

    private void createDeck(DeckType Type, ClientFace face){
        if(Type == DeckType.GOLDEN){
            goldenDeckTopCard = createBoardRectangle();
            goldenDeckTopCard.setFill(new ImagePattern(new Image(face.getPath())));

        }
        else{
            resourceDeckTopCard = createBoardRectangle();
            resourceDeckTopCard.setFill(new ImagePattern(new Image(face.getPath())));
        }
    }

    private Rectangle createBoardRectangle(){
        return new Rectangle(GUICards.boardCardsWidth, GUICards.boardCardsHeight);
    }

    public Pane getBoardMainPane() {
        return BoardMainPane;
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
}
