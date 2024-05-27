package it.polimi.ingsw.network.client.view.gui.controllers;

import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.network.client.model.board.ClientPlayground;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;
import it.polimi.ingsw.network.client.view.gui.GUIPlayground;
import it.polimi.ingsw.network.client.view.gui.util.GUICards;
import it.polimi.ingsw.network.client.view.gui.util.PlayerInfoPane;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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

    private Pane mainPlayerCardsPane;

    private Pane chat;

    private List<Side> playerCardsVisibleSide;
    private int selectedCardHandPosition;




    public GameScene() {
    }

    public void initialize() {
        initializePlayerInfoBox();
        playerCardsVisibleSide = new ArrayList<>();
        selectedCardHandPosition = -1;

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

    private ImagePattern getFacePath(String username, int cardHandPosition, Side side) {
        String path = gui.getController().getPlayer(username).getPlayerCard(cardHandPosition).getSidePath(side);
        return new ImagePattern(new Image(path));
    }

    //todo needs to be called after every place in order to have the correct association between cards and images
    private void initializeMainPlayerCards() {
        GUICards.initializePlayerCards(mainPlayerCardsPane, gui.getController().getMainPlayerCards(), 151, 98, 20, MouseButton.SECONDARY);

        double layoutX = 0.0;
        List<ClientCard> cards = gui.getController().getMainPlayerCards();

        for (int i = 0; i < gui.getController().getMainPlayerCards().size(); i++) {

            int cardHandPosition = i;
            Rectangle rectangle = new Rectangle(151, 98);
            ImagePattern backImage = new ImagePattern(new Image(cards.get(i).getBackPath()));
            ImagePattern frontImage = new ImagePattern(new Image(cards.get(i).getFrontPath()));
            rectangle.setFill(frontImage);
            playerCardsVisibleSide.add(i,Side.FRONT);
            rectangle.setLayoutX(layoutX);
            layoutX = layoutX + 151 + 30;

            rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED && mouseEvent.getButton() == MouseButton.SECONDARY) {
                        if (rectangle.getFill() == frontImage) {
                            rectangle.setFill(backImage);
                            playerCardsVisibleSide.set(cardHandPosition,Side.BACK);
                            return;
                        }
                        rectangle.setFill(frontImage);
                        playerCardsVisibleSide.set(cardHandPosition,Side.FRONT);

                    }
                    else if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED && mouseEvent.getButton() == MouseButton.PRIMARY){
                        selectCard(cardHandPosition);
                    }
                }

            });

            mainPlayerCardsPane.getChildren().add(rectangle);
        }

    }

    private void selectCard(int cardHandPosition){
        if(selectedCardHandPosition == -1){
            selectedCardHandPosition = cardHandPosition;
        }
        else if(selectedCardHandPosition == cardHandPosition){
            selectedCardHandPosition = -1;
        }
        else{
            selectedCardHandPosition = cardHandPosition;
        }
    }

    public void drawPlayground(ClientPlayground clientPlayground) {


        GUIPlayground guiPlayground = new GUIPlayground(WIDTH, HEIGHT);
        guiPlayground.setDimension(clientPlayground);
        //   playgroundPane.setPrefSize(guiPlayground.getPaneWidth(), guiPlayground.getPaneHeight());
        List<Rectangle> cardsAsRectangles = new ArrayList<>();
        // todo. code just to run the playground. It must be inserted in the loop
        Image img = null;
        try {
            img = new Image(
                    "gui/png/cards/010.png"
            );
        } catch (Exception e) {
            System.err.println("Image not found");
            System.exit(1);
        }

        //it's necessary to add available position after the occupied one

        for (Position pos : clientPlayground.getPositioningOrder()) {
            playgroundPane.getChildren().add(guiPlayground.getRectangle(pos, img));
        }

        for (Position pos : clientPlayground.getAvailablePositions()) {
            Rectangle rectangle = guiPlayground.getRectangleEmptyTile(pos);
            playgroundPane.getChildren().add(rectangle);
        }


    }


}
