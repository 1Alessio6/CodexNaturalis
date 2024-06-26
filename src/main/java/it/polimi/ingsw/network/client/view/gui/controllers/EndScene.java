package it.polimi.ingsw.network.client.view.gui.controllers;

import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.network.client.controller.ClientController;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.List;

import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.*;

public class EndScene extends SceneController {

    @FXML
    private Pane mainPane;

    @FXML
    private VBox winnersPane;

    @FXML
    private Button buttonToExit;

    public EndScene() {
    }

    public void initialize() {
        mainPane.setBackground(createMainBackground());
        winnersPane.setAlignment(Pos.TOP_CENTER);
    }

    public void showWinners(List<String> winners) {
        for (String w : winners) {
            Label winnerLabel = new Label();
            winnerLabel.setWrapText(true);
            winnerLabel.setText(w);
            PlayerColor color = gui.getController().getPlayer(w).getColor();
            if (color != null) {
                winnerLabel.setStyle("-fx-text-fill:" + PlayerColor.conversionToCssStyle.get(color));
            }
            winnersPane.getChildren().add(winnerLabel);
        }
    }

    @FXML
    private void exitFromTheGame(MouseEvent mouseEvent) {
        if (isClicked(mouseEvent, MouseButton.PRIMARY)) {
            ClientController controller = gui.getController();
            controller.disconnect(controller.getMainPlayerUsername());
            System.exit(0);
        }
    }
}
