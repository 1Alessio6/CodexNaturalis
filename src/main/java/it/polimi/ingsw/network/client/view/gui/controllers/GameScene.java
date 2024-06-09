package it.polimi.ingsw.network.client.view.gui.controllers;

import it.polimi.ingsw.model.InvalidGamePhaseException;
import it.polimi.ingsw.model.SuspendedGameException;
import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.network.client.model.board.ClientPlayground;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientFace;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;
import it.polimi.ingsw.network.client.view.gui.GUIPlayground;
import it.polimi.ingsw.network.client.view.gui.util.BoardPane;
import it.polimi.ingsw.network.client.view.gui.util.GUICards;
import it.polimi.ingsw.network.client.view.gui.util.PlayerInfoPane;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.isClicked;
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

    private List<Rectangle> availablePositions;

    private String currentVisiblePlaygroundOwner;

    private int selectedCardHandPosition;


    public GameScene() {
    }

    public void initialize() {
        initializePlayerInfoBox();
        playerCardsVisibleSide = new ArrayList<>();
        availablePositions = new ArrayList<>();
        selectedCardHandPosition = -1;
        initializeMainPlayerCardPane();
        currentVisiblePlaygroundOwner = gui.getController().getMainPlayerUsername();

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

    private void initializeMainPlayerCardPane(){
        mainPlayerCardsPane.setPrefSize(1000,168);
        mainPlayerCardsPane.setLayoutX(300);
        mainPlayerCardsPane.setLayoutY(713);
        initializeMainPlayerObjectiveCard();
        initializeMainPlayerCards();
        mainPane.getChildren().add(mainPlayerCardsPane);
    }

    //<Pane layoutX="1126.0" layoutY="713.0" prefHeight="168.0" prefWidth="406.0" />

    private void initializeMainPlayerObjectiveCard(){
        double layoutX = 0.0;
        Rectangle rectangle = new Rectangle(GUICards.playerCardsWidth, GUICards.playerCardsHeight);
        rectangle.setLayoutX(layoutX);
        rectangle.setFill(GUICards.pathToImage(gui.getController().getMainPlayerObjectiveCard().getPath()));
        mainPlayerCardsPane.getChildren().add(rectangle);
    }



    private void initializeBoard(){
        BoardPane boardPane = new BoardPane(gui.getController().getBoard());
        mainPane.getChildren().add(boardPane.getBoardMainPane());
        initializeBoardCards(boardPane);
    }

    private ImagePattern getFacePath(String username, int cardHandPosition, Side side) {
        String path = gui.getController().getPlayer(username).getPlayerCard(cardHandPosition).getSidePath(side);
        return new ImagePattern(new Image(path));
    }

    //todo needs to be called after every place in order to have the correct association between cards and images
    private void initializeMainPlayerCards() {
        GUICards.initializePlayerCards(mainPlayerCardsPane, gui.getController().getMainPlayerCards(), 151, 98, 20, MouseButton.SECONDARY);

        double layoutX = 200;
        List<ClientCard> cards = gui.getController().getMainPlayerCards();

        for (int i = 0; i < gui.getController().getMainPlayerCards().size(); i++) {

            int cardHandPosition = i;
            Rectangle rectangle = new Rectangle(GUICards.playerCardsWidth, GUICards.playerCardsHeight);
            ImagePattern backImage = new ImagePattern(new Image(cards.get(i).getBackPath()));
            ImagePattern frontImage = new ImagePattern(new Image(cards.get(i).getFrontPath()));
            rectangle.setFill(frontImage);
            playerCardsVisibleSide.add(i, Side.FRONT);
            rectangle.setLayoutX(layoutX);
            layoutX = layoutX + GUICards.playerCardsWidth + 30;

            rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (isClicked(mouseEvent, MouseButton.SECONDARY)) {
                        if (rectangle.getFill() == frontImage) {
                            rectangle.setFill(backImage);
                            playerCardsVisibleSide.set(cardHandPosition, Side.BACK);
                            return;
                        }
                        rectangle.setFill(frontImage);
                        playerCardsVisibleSide.set(cardHandPosition, Side.FRONT);

                    } else if (isClicked(mouseEvent, MouseButton.PRIMARY)) {
                        if(!currentVisiblePlaygroundOwner.equals(gui.getController().getMainPlayerUsername())) {
                            changeVisiblePlayground(gui.getController().getMainPlayerUsername());
                        }
                        for (Rectangle availableTile : availablePositions) {
                            availableTile.setVisible(true);
                        }
                        selectCard(cardHandPosition);
                    }
                }

            });

            mainPlayerCardsPane.getChildren().add(rectangle);
        }

    }

    public void updatePlayground(String username){

        if(username.equals(gui.getController().getMainPlayerUsername())){
            drawPlayground(gui.getController().getMainPlayerPlayground());
        }
        else if(username.equals(currentVisiblePlaygroundOwner)){
            drawPlayground(gui.getController().getPlaygroundByUsername(username));
        }
    }

    private void selectCard(int cardHandPosition) {
        if (selectedCardHandPosition == -1) {
            selectedCardHandPosition = cardHandPosition;
        } else if (selectedCardHandPosition == cardHandPosition) {
            selectedCardHandPosition = -1;
        } else {
            selectedCardHandPosition = cardHandPosition;
        }
    }

    public void drawPlayground(ClientPlayground clientPlayground) {

        //do not remove
        availablePositions.clear();

        GUIPlayground guiPlayground = new GUIPlayground(WIDTH, HEIGHT);
        guiPlayground.setDimension(clientPlayground);
        playgroundPane.setPrefSize(guiPlayground.getPaneWidth(), guiPlayground.getPaneHeight());
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
            rectangle.setVisible(false);
            rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {

                    if (isClicked(mouseEvent, MouseButton.PRIMARY) && selectedCardHandPosition != -1) {

                        try{
                            gui.getController().placeCard(selectedCardHandPosition, playerCardsVisibleSide.get(selectedCardHandPosition), pos);
                        }catch (Playground.UnavailablePositionException | InvalidGamePhaseException | SuspendedGameException | Playground.NotEnoughResourcesException | RemoteException e){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle(e.getMessage());
                            alert.setContentText(e.getMessage());
                            alert.show();
                        }

                        selectedCardHandPosition = -1;

                        for (Rectangle availableTile : availablePositions) {
                            availableTile.setVisible(false);
                        }
                    }
                }

            });
            availablePositions.add(rectangle);
            playgroundPane.getChildren().add(rectangle);
        }
    }

    private void initializeBoardCards(BoardPane boardPane){
        setIdToDraw(boardPane.getGoldenDeckTopCard(), 4);
        setIdToDraw(boardPane.getResourceDeckTopCard(), 5);
        setIdToDraw(boardPane.getGoldenFaceUp().get(0), 2);
        setIdToDraw(boardPane.getGoldenFaceUp().get(1), 3);
        setIdToDraw(boardPane.getResourceFaceUp().get(0), 0);
        setIdToDraw(boardPane.getResourceFaceUp().get(1), 1);
    }

    private void setIdToDraw(Rectangle card, int idToDraw){

        card.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(isClicked(mouseEvent,MouseButton.PRIMARY) && gui.getController().getGamePhase() == GamePhase.DrawNormal){
                    try {
                        gui.getController().draw(idToDraw);
                    }catch (Exception e){
                        //todo complete error handling
                        System.out.println(e.getMessage());
                    }
                }
            }
        });


    }




    public void changeVisiblePlayground(String username){
        drawPlayground(gui.getController().getPlaygroundByUsername(username));
    }


}
