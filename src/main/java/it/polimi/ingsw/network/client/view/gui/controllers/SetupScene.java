package it.polimi.ingsw.network.client.view.gui.controllers;

import it.polimi.ingsw.model.InvalidGamePhaseException;
import it.polimi.ingsw.model.SuspendedGameException;
import it.polimi.ingsw.model.card.Color.InvalidColorException;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;
import it.polimi.ingsw.network.client.view.gui.util.GUICircle;
import it.polimi.ingsw.network.client.view.gui.util.GUICards;
import it.polimi.ingsw.network.client.view.gui.util.GUIUtil;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.*;

/**
 * SetupScene is the controller concerning set-up scene
 */
public class SetupScene extends SceneController {

    @FXML
    private Pane mainPane;

    @FXML
    private Rectangle firstRectangle;

    @FXML
    private Rectangle secondRectangle;

    private Text text;

    @FXML
    private Text phaseTitle;
    private List<GUICircle> colors;

    private boolean isStarterSelected;

    static private final double xRefForCircleCenter = 418;
    static private final double yRefForCircleCenter = 554;
    static private final double radius = 35;

    private void initializeColors() {
        ClientController clientController = gui.getController();
        colors = new ArrayList<>();


        List<PlayerColor> availableColors = clientController.getAvailableColors();
        for (PlayerColor availableColor : availableColors) {
            GUICircle circle = new GUICircle(radius, availableColor);
            circle.getCircle().setOnMouseClicked(mouseEvent -> {
                if (GUIUtil.isClicked(mouseEvent, MouseButton.PRIMARY) && circle.getCircle().isVisible()) {
                    try {
                        gui.getController().chooseColor(availableColor);
                    } catch (InvalidColorException | InvalidGamePhaseException |
                             SuspendedGameException e) {
                        showError(e.getMessage());
                    }
                }
            });
            colors.add(circle);
        }
        centerColors();
        for (GUICircle circle : colors) {
            mainPane.getChildren().add(circle.getCircle());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        text = new Text();
        phaseTitle.setText("Setup Phase");
        phaseTitle.setFont(loadTitleFont(50));
        isStarterSelected = false;
        text.setFont(loadFontLiberationSerifRegular(30));
        text.setText("Choose your starter card");
        mainPane.setBackground(createMainBackground());
        updateTextLayoutX();
        text.setLayoutY(288);
        mainPane.getChildren().add(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeUsingGameInformation() {
        super.initializeUsingGameInformation();
        initializeStarterCards();
        initializeColors();
        addButtonPane(mainPane, buttonPane, 1028, 637);
    }

    @Override
    protected void removeUpdatePaneFromMainPane(StackPane errorPane) {
        mainPane.getChildren().remove(errorPane);
    }

    @Override
    public void showError(String details) {
        StackPane errorPane = generateError(details);
        errorPane.setLayoutX((getSceneWindowWidth() - errorPaneWidth) / 2);
        errorPane.setLayoutY(10);
        mainPane.getChildren().add(errorPane);
    }

    private void setStarterPlaceCommand(Rectangle face, Side starterSide) {
        face.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (isClicked(mouseEvent, MouseButton.PRIMARY) && !isStarterSelected) {
                    try {
                        gui.getController().placeStarter(starterSide);
                    } catch (SuspendedGameException | InvalidGamePhaseException e) {
                        showError("Place starter has generated an error\n" + e.getMessage());
                    }
                }
            }
        });
    }

    private void initializeStarterCards() {
        ClientPlayer player = gui.getController().getPlayer(gui.getController().getMainPlayerUsername());
        ClientCard starter = player.getStarterCard();
        firstRectangle.setFill(GUICards.pathToImage(starter.getFrontPath()));
        secondRectangle.setFill(GUICards.pathToImage(starter.getBackPath()));
        firstRectangle.setVisible(true);
        secondRectangle.setVisible(true);
        setStarterPlaceCommand(firstRectangle, Side.FRONT);
        setStarterPlaceCommand(secondRectangle, Side.BACK);
    }

    /**
     * Constructs a new <code>SetupScene</code> with no parameter provided
     */
    public SetupScene() {
    }

    /**
     * Method used to show the scene after placing the starter card
     *
     * @param username the username of the player who placed the card
     */
    public void updateAfterStarterPlace(String username) {
        if (gui.getController().getMainPlayer().getUsername().equals(username)) {
            isStarterSelected = true;
            text.setText("Choose your color");
            updateTextLayoutX();
            showColors();
            initializeObjectiveCards();
        }
    }

    private void showColors() {
        for (GUICircle circle : colors) {
            circle.setVisibility(true);
        }
    }

    private void initializeObjectiveCards() {
        List<ClientObjectiveCard> objectiveCards = gui.getController().getMainPlayer().getObjectiveCards();
        firstRectangle.setFill(GUICards.pathToImage(objectiveCards.getFirst().getPath()));
        setSelectObjectiveCardCommand(firstRectangle, 0);
        secondRectangle.setFill(GUICards.pathToImage(objectiveCards.getLast().getPath()));
        setSelectObjectiveCardCommand(secondRectangle, 1);
        firstRectangle.setVisible(false);
        secondRectangle.setVisible(false);
    }

    private void setSelectObjectiveCardCommand(Rectangle card, int objectiveCardId) {
        card.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (isClicked(mouseEvent, MouseButton.PRIMARY) && isStarterSelected && card.isVisible()) {
                    try {
                        gui.getController().placeObjectiveCard(objectiveCardId);
                    } catch (SuspendedGameException | InvalidGamePhaseException e) {
                        showError("Error in placing the objective\n" + e.getMessage());

                    }
                }
            }
        });
    }

    private void updateAfterColorSelection() {
        for (GUICircle circle : colors) {
            circle.setVisibility(false);
        }
        firstRectangle.setVisible(true);
        secondRectangle.setVisible(true);
        text.setText("Choose you secret objective card");
        updateTextLayoutX();
    }

    private void centerColors() {
        List<PlayerColor> availableColors = gui.getController().getAvailableColors();
        double x = xRefForCircleCenter + 2 * radius * (4 - availableColors.size());
        for (GUICircle c : colors) {
            c.setCoordinates(x, yRefForCircleCenter);
            x += 4 * radius;
        }
    }

    private void updateColorDisposition(PlayerColor color) {
        GUICircle circle = colors.stream().filter(c -> c.getColor().equals(color)).findFirst().get();
        circle.setVisibility(false);
        colors.remove(circle);
        centerColors();
    }

    /**
     * Method used to show the scene after choosing the token color
     *
     * @param username the username of the player chose the color
     */
    public void updateAfterColor(String username) {
        ClientController controller = gui.getController();
        ClientPlayer player = controller.getPlayer(username);
        if (player.getUsername().equals(controller.getMainPlayerUsername())) {
            updateAfterColorSelection();
        } else {
            updateColorDisposition(player.getColor());
        }
    }

    /**
     * Method used to show the scene after choosing the secret objective card
     */
    public void updateObjectiveCard() {
        firstRectangle.setVisible(false);
        secondRectangle.setVisible(false);
        text.setText("Waiting for the other players to complete their setup...");
        updateTextLayoutX();
    }

    /**
     * Method used to complete the set-up
     */
    public void initializeCompletedSetup() {
        for (GUICircle circle : colors) {
            circle.setVisibility(false);
        }
        updateObjectiveCard();
    }

    public double getSceneWindowWidth() {
        return startedGameSceneWidth;
    }

    public double getSceneWindowHeight() {
        return startedGameSceneHeight;
    }

    private void updateTextLayoutX(){
        text.setLayoutX((getSceneWindowWidth() - text.boundsInLocalProperty().get().getWidth())/2);
    }

}
