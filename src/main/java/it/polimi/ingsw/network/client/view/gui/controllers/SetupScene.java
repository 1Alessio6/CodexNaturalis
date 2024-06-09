package it.polimi.ingsw.network.client.view.gui.controllers;

import it.polimi.ingsw.model.InvalidGamePhaseException;
import it.polimi.ingsw.model.SuspendedGameException;
import it.polimi.ingsw.model.card.Color.InvalidColorException;
import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;
import it.polimi.ingsw.network.client.view.gui.circle.GUICircle;
import it.polimi.ingsw.network.client.view.gui.util.GUICards;
import it.polimi.ingsw.network.client.view.gui.util.GUIUtil;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class SetupScene extends SceneController {
    @FXML
    private Rectangle firstRectangle;

    @FXML
    private Rectangle secondRectangle;
    @FXML
    private TextField text;

    private List<GUICircle> colors;

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
                    if (GUIUtil.isClicked(mouseEvent, MouseButton.PRIMARY)) {
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
    }

    @Override
    public void initialize() {
        ClientController clientController = gui.getController();
        ClientPlayer player = clientController.getPlayer(clientController.getMainPlayerUsername());
        ClientCard starter = player.getStarterCard();
        firstRectangle.setFill(GUICards.pathToImage(starter.getFrontPath()));
        secondRectangle.setFill(GUICards.pathToImage(starter.getBackPath()));
        firstRectangle.setVisible(true);
        secondRectangle.setVisible(true);
        text.setText("Choose your starter");
        initializeColors();
    }

    public SetupScene() {
    }

    private void showColors() {
        for (GUICircle circle : colors) {
            circle.setVisibility(true);
        }
    }

    private void updateColorDisposition() {
        colors.getLast().setVisibility(false);
        colors.removeLast();
        for (GUICircle c : colors) {
            c.setCoordinates(c.getCircle().getCenterX() + 2*radius, c.getCircle().getCenterY());
        }
    }

    private void updateAfterColorChoiceHasBeenAccepted() {
        for (GUICircle circle : colors) {
            circle.setVisibility(false);
        }
        firstRectangle.setVisible(true);
        secondRectangle.setVisible(true);
    }

}
