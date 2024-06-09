package it.polimi.ingsw.network.client.view.gui.controllers;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.model.card.ClientCard;
import it.polimi.ingsw.network.client.model.player.ClientPlayer;
import it.polimi.ingsw.network.client.view.gui.circle.GUICircle;
import it.polimi.ingsw.network.client.view.gui.util.GUICards;
import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class SetupScene extends SceneController {
    @FXML
    private Rectangle firstRectangle;

    @FXML
    private Rectangle secondRectangle;

    private List<GUICircle> colors;

    private int numColorsLeft;

    static private final double xRefForCircleCenter = 418;
    static private final double yRefForCircleCenter = 554;
    static private final double radius = 35;

    @Override
    public void initialize() {
        ClientController clientController = gui.getController();
        ClientPlayer player = clientController.getPlayer(clientController.getMainPlayerUsername());
        ClientCard starter = player.getStarterCard();
        firstRectangle.setFill(GUICards.pathToImage(starter.getFrontPath()));
        secondRectangle.setFill(GUICards.pathToImage(starter.getBackPath()));
        firstRectangle.setVisible(true);
        secondRectangle.setVisible(true);

        numColorsLeft = clientController.getAvailableColors().size();

        colors = new ArrayList<>();
        double x = xRefForCircleCenter + 2 * radius * numColorsLeft;
        for (int i = 0; i < numColorsLeft; ++i) {
            colors.add(new GUICircle(x, yRefForCircleCenter, radius));
            x += 2 * radius;
        }
    }

    public SetupScene() {

    }



}
