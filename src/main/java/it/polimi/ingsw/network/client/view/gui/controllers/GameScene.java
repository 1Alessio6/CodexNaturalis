package it.polimi.ingsw.network.client.view.gui.controllers;

import it.polimi.ingsw.model.InvalidGamePhaseException;
import it.polimi.ingsw.model.SuspendedGameException;
import it.polimi.ingsw.model.board.Availability;
import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.chat.message.InvalidMessageException;
import it.polimi.ingsw.model.chat.message.Message;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.model.board.ClientPlayground;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;
import it.polimi.ingsw.network.client.view.gui.util.GUIPlayground;
import it.polimi.ingsw.network.client.view.gui.util.BoardPane;
import it.polimi.ingsw.network.client.view.gui.util.PlayerInfoPane;
import it.polimi.ingsw.network.client.view.gui.util.PlaygroundInfoPane;
import it.polimi.ingsw.network.client.view.gui.util.chat.Chat;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.rmi.RemoteException;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.network.client.view.gui.util.GUICards.*;
import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.*;

public class GameScene extends SceneController {

    @FXML
    private Pane mainPane;

    @FXML
    private Pane playgroundPane;

    @FXML
    private TextFlow sentMessages;

    @FXML
    private ComboBox<String> recipients;

    @FXML
    private TextField text;

    @FXML
    private ScrollPane chatPane;

    private Pane mainPlayerCardsPane;

    private PlaygroundInfoPane playgroundInfoPane;

    private BoardPane boardPane;

    private List<PlayerInfoPane> playerInfoPanes;

    private List<Side> playerCardsVisibleSide;

    private List<Rectangle> mainPlayerCards;

    private List<Rectangle> availablePositions;

    private String currentVisiblePlaygroundOwner;

    private int selectedCardHandPosition;

    private Chat chat;

    public GameScene() {
    }

    public void initialize() {

        playerCardsVisibleSide = new ArrayList<>();
        availablePositions = new ArrayList<>();
        mainPlayerCards = new ArrayList<>();
        selectedCardHandPosition = -1;
        sentMessages.minWidthProperty().bind(chatPane.widthProperty().subtract(8));
    }

    private boolean isReferringToMe(Message m) {
        String myName = gui.getController().getMainPlayerUsername();
        return
                m.getRecipient().equals(myName)
                        ||
                        m.getSender().equals(myName)
                        ||
                        m.isBroadcast();
    }

    private void appendMessage(Message m) {
        ClientController controller = gui.getController();
        String senderName = m.getSender();
        Text sender = new Text(m.getSender());
        sender.setStyle("-fx-fill: "
                +
                PlayerColor.conversionToCssStyle.get(controller.getPlayer(senderName).getColor())
        );
        Text recipient = new Text(m.getRecipient());
        String recipientName = m.getRecipient();
        if (!recipientName.equals("Everyone")) {
            recipient.setStyle("-fx-fill:"
                    +
                    PlayerColor.conversionToCssStyle.get(controller.getPlayer(recipientName).getColor())
            );
        }
        Text content = new Text(": " + m.getContent() + "\n\n");
        sentMessages.getChildren().addAll(sender, new Text(" to "), recipient, content);
    }

    private void initializeChat() {
        recipients.getItems().addAll(gui.getController().getUsernames());
        recipients.getItems().add("Everyone");
        chat = new Chat(gui.getController().getMainPlayerUsername());
        for (Message m : gui.getController().getMessages()) {
            if (isReferringToMe(m)) {
                appendMessage(m);
            }
        }
    }

    @Override
    public void initializeUsingGameInformation() {
        currentVisiblePlaygroundOwner = gui.getController().getMainPlayerUsername();
        initializePlayerInfoBox();
        initializeMainPlayerCardPane();
        drawPlayground(gui.getController().getMainPlayerPlayground());
        initializeBoard();
        initializeChat();
        initializePlaygroundInfoPane();
        //setPlaygroundFrameColor();
    }

    private void initializePlaygroundInfoPane() {
        playgroundInfoPane = new PlaygroundInfoPane();
        playgroundInfoPane.setPlaygroundInfo(gui.getController().getMainPlayer(), true);
        mainPane.getChildren().add(playgroundInfoPane.getMainPane());
        initializeReturnToMainPlayground();
    }

    private void initializeReturnToMainPlayground() {

        ImageView returnToMainPlayground = playgroundInfoPane.getReturnToMainPlayground();
        returnToMainPlayground.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (returnToMainPlayground.isVisible() && isClicked(mouseEvent, MouseButton.PRIMARY)) {
                    changeVisiblePlayground(gui.getController().getMainPlayerUsername());
                }
            }
        });
    }


    private void initializePlayerInfoBox() {

        playerInfoPanes = new ArrayList<PlayerInfoPane>();


        int distance = 20;
        int layoutX = 30;
        int layoutY = 14;

        for (ClientPlayer player : gui.getController().getPlayers()) {
            if (!player.getUsername().equals(gui.getController().getMainPlayerUsername())) {
                PlayerInfoPane playerInfoPane = new PlayerInfoPane(player);
                Pane pane = playerInfoPane.getPlayerMainPane();
                pane.setLayoutX(layoutX);
                pane.setLayoutY(layoutY);
                mainPane.getChildren().add(pane);
                playerInfoPanes.add(playerInfoPane);
                initializeSwitchPlayground(playerInfoPane);
                layoutX = layoutX + 361 + distance;
            }

        }

    }

    private void initializeMainPlayerCardPane() {
        mainPlayerCardsPane = new Pane();
        mainPlayerCardsPane.setPrefSize(700, 90);
        mainPlayerCardsPane.setLayoutX(334);
        mainPlayerCardsPane.setLayoutY(630);
        initializeMainPlayerObjectiveCard();
        initializeMainPlayerCards();
        mainPane.getChildren().add(mainPlayerCardsPane);
    }


    private void initializeMainPlayerObjectiveCard() {
        double layoutX = 0.0;
        Rectangle rectangle = new Rectangle(playerCardsWidth, playerCardsHeight);
        rectangle.setLayoutX(layoutX);
        rectangle.setFill(pathToImage(gui.getController().getMainPlayerObjectiveCard().getPath()));
        mainPlayerCardsPane.getChildren().add(rectangle);
        mainPlayerCardsPane.setBackground(setBackgroundColor("EEE5BC"));
    }


    //todo when a card is placed even if the player hasn't draw it should be not visible

    private void initializeBoard() {
        boardPane = new BoardPane(gui.getController().getBoard());
        initializeBoardCards(boardPane);
        mainPane.getChildren().add(boardPane.getBoardMainPane());

    }

    private ImagePattern getFacePath(String username, int cardHandPosition, Side side) {
        String path = gui.getController().getPlayer(username).getPlayerCard(cardHandPosition).getSidePath(side);
        return new ImagePattern(new Image(path));
    }

    //todo needs to be called after every place in order to have the correct association between cards and images
    private void initializeMainPlayerCards() {

        for (Rectangle card : mainPlayerCards) {
            mainPlayerCardsPane.getChildren().remove(card);
        }
        mainPlayerCards.clear();

        //todo you should remove from main pain too

        double layoutX = 200;
        List<ClientCard> cards = gui.getController().getMainPlayerCards();

        for (int i = 0; i < cards.size(); i++) {

            int cardHandPosition = i;
            Rectangle rectangle = new Rectangle(playerCardsWidth, playerCardsHeight);
            ImagePattern backImage = new ImagePattern(new Image(cards.get(i).getBackPath()));
            ImagePattern frontImage = new ImagePattern(new Image(cards.get(i).getFrontPath()));
            rectangle.setFill(frontImage);
            playerCardsVisibleSide.add(i, Side.FRONT);
            rectangle.setLayoutX(layoutX);
            layoutX = layoutX + playerCardsWidth + 30;

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
                        if (!currentVisiblePlaygroundOwner.equals(gui.getController().getMainPlayerUsername())) {
                            changeVisiblePlayground(gui.getController().getMainPlayerUsername());
                        }
                        selectCard(cardHandPosition);
                    }
                }

            });

            mainPlayerCardsPane.getChildren().add(rectangle);
            mainPlayerCards.add(rectangle);
        }

    }

    private void initializeSwitchPlayground(PlayerInfoPane playerInfoPane) {

        ImageView switchPlayground = playerInfoPane.getSwitchPlayground();
        switchPlayground.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (selectedCardHandPosition == -1 && isClicked(mouseEvent, MouseButton.PRIMARY)) {
                    changeVisiblePlayground(playerInfoPane.getPlayerUsername());
                }
            }
        });

    }

    private void selectCard(int cardHandPosition) {

        boolean availableTilesVisibility;

        //case the card I click is already selected
        if (selectedCardHandPosition == cardHandPosition) {
            selectedCardHandPosition = -1;
            availableTilesVisibility = false;
        }
        //case there aren't cards selected or the card selected was one clicked in the past
        else {
            selectedCardHandPosition = cardHandPosition;
            availableTilesVisibility = true;
        }

        for (Rectangle availableTile : availablePositions) {
            availableTile.setVisible(availableTilesVisibility);
        }

    }

    public void drawPlayground(ClientPlayground clientPlayground) {

        //do not remove
        availablePositions.clear();

        GUIPlayground guiPlayground = new GUIPlayground(playerCardsWidth, playerCardsHeight);
        guiPlayground.setDimension(clientPlayground);
        playgroundPane.setPrefSize(guiPlayground.getPaneWidth(), guiPlayground.getPaneHeight());
        List<Rectangle> cardsAsRectangles = new ArrayList<>();


        // clean the pane
        playgroundPane.getChildren().clear();

        //it's necessary to add available position after the occupied one

        for (Position pos : clientPlayground.getPositioningOrder()) {
            if (!clientPlayground.getTile(pos).sameAvailability(Availability.EMPTY)) {
                playgroundPane.getChildren().add(guiPlayground.getRectangle(pos, pathToImage(clientPlayground.getTile(pos).getFace().getPath())));
            } else {
                playgroundPane.getChildren().add(guiPlayground.getRectangleEmptyTile(pos));
            }
        }

        for (Position pos : clientPlayground.getAvailablePositions()) {
            Rectangle rectangle = guiPlayground.getRectangleEmptyTile(pos);
            rectangle.setVisible(false);
            rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {

                    if (isClicked(mouseEvent, MouseButton.PRIMARY) && selectedCardHandPosition != -1) {

                        try {
                            gui.getController().placeCard(selectedCardHandPosition, playerCardsVisibleSide.get(selectedCardHandPosition), pos);
                        } catch (Playground.UnavailablePositionException | InvalidGamePhaseException |
                                 SuspendedGameException | Playground.NotEnoughResourcesException | RemoteException e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle(e.getMessage());
                            alert.setContentText(e.getMessage());
                            alert.show();
                            selectedCardHandPosition = -1;
                        }

                        //todo check if this line moved in showUpdateAfterPlace causes trouble
                        //selectedCardHandPosition = -1;

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

    private void initializeBoardCards(BoardPane boardPane) {
        setIdToDraw(boardPane.getGoldenDeckTopCard(), 4);
        setIdToDraw(boardPane.getResourceDeckTopCard(), 5);
        setIdToDraw(boardPane.getGoldenFaceUp().get(0), 2);
        setIdToDraw(boardPane.getGoldenFaceUp().get(1), 3);
        setIdToDraw(boardPane.getResourceFaceUp().get(0), 0);
        setIdToDraw(boardPane.getResourceFaceUp().get(1), 1);
    }

    private void setIdToDraw(Rectangle card, int idToDraw) {

        card.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (isClicked(mouseEvent, MouseButton.PRIMARY) && gui.getController().getGamePhase() == GamePhase.DrawNormal) {
                    try {
                        gui.getController().draw(idToDraw);
                    } catch (Exception e) {
                        //todo complete error handling
                        System.out.println(e.getMessage());
                    }
                }
            }
        });
    }

    //todo add Images/ empty rectangles for deck and faceUp empty
    public void updateAfterDraw(String username) {
        if (username.equals(gui.getController().getMainPlayerUsername())) {
            initializeMainPlayerCards();
        } else {
            PlayerInfoPane playerInfoPane = getPlayerInfoPane(username);
            assert playerInfoPane != null;
            playerInfoPane.updatePlayerCards(gui.getController().getPlayer(username).getPlayerCards());
        }

        boardPane.boardUpdate(gui.getController().getBoard());

    }


    //todo check if creates concurrency problems with update after place (java fx should be single thread so there shouldn't be problems)
    private void changeVisiblePlayground(String username) {
        currentVisiblePlaygroundOwner = username;
        updatePlayground(username);

    }

    private void updatePlayground(String username) {
        drawPlayground(gui.getController().getPlaygroundByUsername(username));
        playgroundInfoPane.setPlaygroundInfo(gui.getController().getPlayer(username), username.equals(gui.getController().getMainPlayerUsername()));
    }


    public void updateAfterPlace(String username) {
        if (username.equals(currentVisiblePlaygroundOwner)) {
            updatePlayground(username);
        } else if (username.equals(gui.getController().getMainPlayerUsername())) {
            changeVisiblePlayground(gui.getController().getMainPlayerUsername());
        }

        if (!username.equals(gui.getController().getMainPlayerUsername())) {
            Objects.requireNonNull(getPlayerInfoPane(username)).updateResources(gui.getController().getPlaygroundByUsername(username).getResources());
        } else {
            mainPlayerCards.get(selectedCardHandPosition).setVisible(false);
            selectedCardHandPosition = -1;
        }
    }

    /*
    private void setPlaygroundFrameColor(){
        ClientPlayer player = gui.getController().getPlayer(currentVisiblePlaygroundOwner);
        framePane.setBackground(setBackgroundColor("#FF0000"));
        framePane.setVisible(true);
    }

     */

    private PlayerInfoPane getPlayerInfoPane(String username) {
        for (PlayerInfoPane playerInfoPane : playerInfoPanes) {
            if (playerInfoPane.getPlayerUsername().equals(username)) {
                return playerInfoPane;
            }
        }

        return null;
    }

    @FXML
    private void selectRecipient() {
        chat.selectRecipient(recipients.getValue());
    }

    @FXML
    private void sendMessage(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            chat.insertText(text.getText());
            try {
                chat.sendMessage(gui.getController());

            } catch (InvalidMessageException e) {
                System.err.println("error: " + e.getMessage());
                //could be changed with an error scene
                Alert invalidMessage = new Alert(Alert.AlertType.ERROR);
                invalidMessage.setTitle("Invalid message");
                invalidMessage.setContentText("Exception: " + e.getMessage());
                invalidMessage.show();
            } catch (RemoteException e) {
                // todo. add notification for server crashed
            }
            text.setText("");
        }
    }

    public void receiveMessage(Message message) {
        if (isReferringToMe(message)) {
            appendMessage(message);
        }
    }

}
