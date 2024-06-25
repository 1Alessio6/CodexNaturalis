package it.polimi.ingsw.network.client.view.gui.controllers;

import it.polimi.ingsw.network.client.view.gui.SceneType;
import it.polimi.ingsw.network.client.view.gui.util.Icon;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.*;
import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.connectionSceneHeight;

/**
 * CrashScene is the controller concerning crash scene
 */
public class CrashScene extends SceneController {

    @FXML
    private Text message;
    @FXML
    private Pane mainPane;

    @FXML
    private Button returnButton;

    @FXML
    private Button changeServer;

    public CrashScene() {
    }

    /**
     * {@inheritDoc}
     */
    public void initialize() {
        mainPane.setBackground(createMainBackground());
        ImageView returnImage = (new ImageView(Icon.RETURN.getPath()));
        returnImage.setFitHeight(30);
        returnImage.setFitWidth(30);
        returnButton.setGraphic(returnImage);
        returnButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (isClicked(mouseEvent, MouseButton.PRIMARY)) {
                    gui.loadScene(SceneType.SELECT_USERNAME);
                }
            }
        });

        ImageView changeServerImage = (new ImageView(Icon.SERVER.getPath()));
        changeServerImage.setFitHeight(25);
        changeServerImage.setFitWidth(25);

        changeServer.setGraphic(changeServerImage);
        changeServer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (isClicked(mouseEvent, MouseButton.PRIMARY)) {
                    gui.loadScene(SceneType.CONNECTION);
                }
            }
        });

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeUsingGameInformation() {
        super.initializeUsingGameInformation();
        addButtonPane(mainPane, buttonPane, 860, 650);
    }

    @Override
    protected void removeErrorFromMainPane(StackPane errorPane) {
        mainPane.getChildren().remove(errorPane);
    }

    @Override
    public void showError(String details) {
        StackPane errorPane = generateError(details);
        errorPane.setLayoutX((getSceneWindowWidth() - errorPaneWidth) / 2);
        errorPane.setLayoutY(10);
        mainPane.getChildren().add(errorPane);
    }

    public double getSceneWindowWidth() {
        return connectionSceneWidth;
    }

    public double getSceneWindowHeight() {
        return connectionSceneHeight;
    }

    /**
     * Sets the reason of the crash
     *
     * @param reason the detail reason
     */
    public void setReason(String reason) {
        message.setText(reason);
    }


}
