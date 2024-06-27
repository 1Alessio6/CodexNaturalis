package it.polimi.ingsw.network.client.view.gui.controllers;

import it.polimi.ingsw.model.card.Color.PlayerColor;
import it.polimi.ingsw.network.client.view.gui.util.Icon;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.*;

/**
 * EndScene is the controller concerning the end scene
 */
public class EndScene extends SceneController {

    @FXML
    private Pane mainPane;

    @FXML
    private TextArea winnersTextArea;

    public EndScene() {
    }

    /**
     * {@inheritDoc}
     */
    public void initialize() {
        winnersTextArea.setEditable(false);
        winnersTextArea.setFont(loadFontLiberationSerifBold(15.5));
        Text winnersTitle = new Text("End Phase");
        winnersTitle.setLayoutY(94);
        winnersTitle.setLayoutX(585);
        Text winners = new Text("Winners");
        winners.setLayoutX(531);
        winners.setLayoutY(245);
        winners.setFont(loadFontLiberationSerifBold(25));
        winnersTitle.setFont(loadTitleFont(50));
        Text exitMessage = new Text("The game is finished, close the application and\n                  restart it to play again");
        exitMessage.setFont(loadFontLiberationSerifRegular(15.5));
        exitMessage.setLayoutX(531);
        exitMessage.setLayoutY(575);
        Text pointsMessage = new Text(("""
                                 The winner (or winners in case of a tie) is
                determined based on their score, including the extra points earned from
                     the objective cards and the number of objective cards completed"""));
        pointsMessage.setFont(loadFontLiberationSerifRegular(15.5));
        pointsMessage.setLayoutX(480);
        pointsMessage.setLayoutY(160);
        mainPane.getChildren().add(exitMessage);
        mainPane.getChildren().add(pointsMessage);
        mainPane.getChildren().add(winnersTitle);
        mainPane.getChildren().add(winners);
        mainPane.setBackground(createMainBackground());
        initializeExitButton();
    }

    /**
     * {@inheritDoc}
     */
    public void initializeUsingGameInformation() {
        super.initializeUsingGameInformation();
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

    /**
     * Method used to show the winners of the game
     *
     * @param winners the winners' list
     */
    public void showWinners(List<String> winners) {

        double distance = 38;
        double layoutY = 294.5;

        for (String username : winners) {
            ImageView crownImage = initializeIconImageView(Icon.CROWN.getPath(), 20);
            crownImage.setLayoutY(layoutY);
            crownImage.setLayoutX(545);
            mainPane.getChildren().add(crownImage);
            winnersTextArea.appendText("\n        " + username);

            if (username.equals(gui.getController().getMainPlayerUsername())) {
                winnersTextArea.appendText(" (You)");
            }

            winnersTextArea.appendText("\n");

            layoutY = layoutY + distance;

        }
    }

    private void initializeExitButton() {
        Button exitButton = new Button();
        exitButton.setPrefSize(160, 40);
        exitButton.setText("Exit");
        exitButton.setFont(loadFontLiberationSerifRegular(15.5));
        ImageView logoutImage = initializeIconImageView(Icon.LOGOUT.getPath(), 20);
        exitButton.setGraphic(logoutImage);
        exitButton.setLayoutX(590);
        exitButton.setLayoutY(615);
        exitButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (isClicked(mouseEvent, MouseButton.PRIMARY)) {
                    System.exit(0);
                }
            }
        });

        mainPane.getChildren().add(exitButton);
    }

    public double getSceneWindowWidth() {
        return startedGameSceneWidth;
    }

    public double getSceneWindowHeight() {
        return startedGameSceneHeight;
    }
}
