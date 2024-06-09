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
import it.polimi.ingsw.network.client.view.gui.circle.GUICircle;
import it.polimi.ingsw.network.client.view.gui.util.GUICards;
import it.polimi.ingsw.network.client.view.gui.util.GUIUtil;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.isClicked;

public class SetupScene extends SceneController {

    @FXML
    Pane mainPane;
    @FXML
    Rectangle firstRectangle;

    @FXML
    Rectangle secondRectangle;

    @FXML
    TextField text;

    private List<GUICircle> colors;

    private boolean isStarterSelected;

    private int numColorsLeft;

    static private final double xRefForCircleCenter = 418;
    static private final double yRefForCircleCenter = 554;
    static private final double radius = 35;

    private void initializeColors() {
        ClientController clientController = gui.getController();
        colors = new ArrayList<>();


        List<PlayerColor> availableColors = clientController.getAvailableColors();
        double x = xRefForCircleCenter + 2 * radius * (4 - availableColors.size());
        for (PlayerColor availableColor : availableColors) {
            GUICircle circle = new GUICircle(x, yRefForCircleCenter, radius, availableColor);
            circle.getCircle().setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (GUIUtil.isClicked(mouseEvent, MouseButton.PRIMARY) && circle.getCircle().isVisible()) {
                        try {
                            gui.getController().chooseColor(availableColor);
                            // todo. change with specific report error
                        } catch (InvalidColorException | RemoteException | InvalidGamePhaseException |
                                 SuspendedGameException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
            colors.add(circle);
            x += 2 * radius;
        }
        for(GUICircle circle : colors){
            mainPane.getChildren().add(circle.getCircle());
        }
    }

    @Override
    public void initialize() {
        System.out.println("Initializing setup scene");
        ClientController clientController = gui.getController();
        initializeStarterCards();
        isStarterSelected = false;
        text.setText("Choose your starter");
        initializeColors();
    }

    private void setStarterPlaceCommand(Rectangle face, Side starterSide){
        face.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(isClicked(mouseEvent, MouseButton.PRIMARY) && !isStarterSelected){
                    try {
                        gui.getController().placeStarter(starterSide);
                    } catch (SuspendedGameException | RemoteException | InvalidGamePhaseException e) {
                        //todo update
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("place starter has generated an error");
                        error.show();
                    }
                }
            }
        });
    }

    private void initializeStarterCards(){
        System.out.println("initializing starter");
        ClientPlayer player = gui.getController().getPlayer(gui.getController().getMainPlayerUsername());
        ClientCard starter = player.getStarterCard();
        firstRectangle.setFill(GUICards.pathToImage(starter.getFrontPath()));
        secondRectangle.setFill(GUICards.pathToImage(starter.getBackPath()));
        firstRectangle.setVisible(true);
        secondRectangle.setVisible(true);
        setStarterPlaceCommand(firstRectangle, Side.FRONT);
        setStarterPlaceCommand(secondRectangle, Side.BACK);
    }

    public SetupScene() {
        System.out.println("Constructing the setup scene");
    }

    public void updateAfterStarterPlace(){
        isStarterSelected = true;
        initializeObjectiveCards();
        for(GUICircle circle : colors){
            circle.setVisibility(true);
        }

        text.setText("Select color");
        showColors();
    }

    private void showColors() {
        for (GUICircle circle : colors) {
            circle.setVisibility(true);
        }
    }

    private void initializeObjectiveCards(){
        List<ClientObjectiveCard> objectiveCards = gui.getController().getObjectiveCards();
        firstRectangle.setFill(GUICards.pathToImage(objectiveCards.getFirst().getPath()));
        setSelectObjectiveCardCommand(firstRectangle, 0);
        secondRectangle.setFill(GUICards.pathToImage(objectiveCards.getLast().getPath()));
        setSelectObjectiveCardCommand(secondRectangle, 1);
        secondRectangle.setVisible(false);
        secondRectangle.setVisible(false);
    }

    private void setSelectObjectiveCardCommand(Rectangle card, int objectiveCardId){
        card.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(isClicked(mouseEvent, MouseButton.PRIMARY) && isStarterSelected && card.isVisible()){
                    try {
                        gui.getController().placeObjectiveCard(objectiveCardId);
                    } catch (SuspendedGameException | RemoteException | InvalidGamePhaseException e) {
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Error in placing the objective");
                        error.show();
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
    }

    private void updateColorDisposition() {
        colors.getLast().setVisibility(false);
        colors.removeLast();
        for (GUICircle c : colors) {
            c.setCoordinates(c.getCircle().getCenterX() + 2*radius, c.getCircle().getCenterY());
        }
    }

    public void updateAfterColor(String username) {
        ClientController controller = gui.getController();
        ClientPlayer player = controller.getPlayer(username);
        if (player.getUsername().equals(controller.getMainPlayerUsername())) {
            updateAfterColorSelection();
        } else {
            updateColorDisposition();
        }
    }

    public void updateObjectiveCard() {
        firstRectangle.setVisible(false);
        secondRectangle.setVisible(false);
        text.setText("Wait for the other players to complete their setup");
    }
}
