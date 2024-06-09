package it.polimi.ingsw.network.client.view.gui.controllers;

import it.polimi.ingsw.model.InvalidGamePhaseException;
import it.polimi.ingsw.model.SuspendedGameException;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.gamePhase.GamePhase;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.card.ClientObjectiveCard;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;
import it.polimi.ingsw.network.client.view.gui.circle.GUICircle;
import it.polimi.ingsw.network.client.view.gui.util.GUICards;
import it.polimi.ingsw.network.client.view.gui.util.GUIUtil;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.isClicked;

public class SetupScene extends SceneController {
    @FXML
    private Rectangle firstRectangle;

    @FXML
    private Rectangle secondRectangle;

    private List<GUICircle> colors;

    private boolean isStarterSelected;

    private int numColorsLeft;

    static private final double xRefForCircleCenter = 418;
    static private final double yRefForCircleCenter = 554;
    static private final double radius = 35;

    public SetupScene() {

    }

    @Override
    public void initialize() {
        ClientController clientController = gui.getController();
        initializeStarterCards();
        isStarterSelected = false;
        numColorsLeft = clientController.getAvailableColors().size();

        colors = new ArrayList<>();
        double x = xRefForCircleCenter + 2 * radius * numColorsLeft;
        for (int i = 0; i < numColorsLeft; ++i) {
            colors.add(new GUICircle(x, yRefForCircleCenter, radius));
            x += 2 * radius;
        }

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
                        error.show();
                    }
                }
            }
        });
    }

    private void initializeStarterCards(){
        ClientPlayer player = gui.getController().getPlayer(gui.getController().getMainPlayerUsername());
        ClientCard starter = player.getStarterCard();
        firstRectangle.setFill(GUICards.pathToImage(starter.getFrontPath()));
        secondRectangle.setFill(GUICards.pathToImage(starter.getBackPath()));
        firstRectangle.setVisible(true);
        secondRectangle.setVisible(true);
        setStarterPlaceCommand(firstRectangle, Side.FRONT);
        setStarterPlaceCommand(secondRectangle, Side.BACK);
    }



    private void setSelectObjectiveCardCommand(Rectangle card, int objectiveCardId){
        card.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(isClicked(mouseEvent, MouseButton.PRIMARY) && isStarterSelected){
                    try {
                        gui.getController().placeObjectiveCard(objectiveCardId);
                    } catch (SuspendedGameException | RemoteException | InvalidGamePhaseException e) {
                        //todo update
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.show();
                    }
                }
            }
        });
    }


}
