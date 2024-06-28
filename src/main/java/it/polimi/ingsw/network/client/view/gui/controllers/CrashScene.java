package it.polimi.ingsw.network.client.view.gui.controllers;

import it.polimi.ingsw.network.client.view.gui.util.Icon;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.*;
import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.connectionSceneHeight;

/**
 * CrashScene is the controller concerning crash scene
 */
public class CrashScene extends SceneController {

    private Text message;

    @FXML
    private Pane mainPane;

    @FXML
    private Button exitButton;

    @FXML
    private Pane textPane;

    public CrashScene() {
    }

    /**
     * {@inheritDoc}
     */
    public void initialize() {
        mainPane.setBackground(createMainBackground());
        message = new Text("SERVER CRASHED");
        message.setFont(loadFontLiberationSerifBold(24));
        message.setFill(Color.web("#dd2d2a"));
        message.setLayoutX(59);
        message.setLayoutY(55);
        Label connectionLostMessage = new Label();
        connectionLostMessage.setFont(loadFontLiberationSansRegular(14));
        connectionLostMessage.setText("Connection lost, please close the application\n                          and try again");
        connectionLostMessage.setLayoutY(99);
        connectionLostMessage.setLayoutX(35);
        textPane.getChildren().add(connectionLostMessage);
        textPane.getChildren().add(message);

        exitButton.setFont(loadFontLiberationSerifRegular(15.5));
        ImageView logoutImage = (new ImageView(Icon.LOGOUT.getPath()));
        logoutImage.setFitHeight(20);
        logoutImage.setFitWidth(20);
        exitButton.setGraphic(logoutImage);

        exitButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (isClicked(mouseEvent, MouseButton.PRIMARY)) {
                    System.exit(0);
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void removeUpdatePaneFromMainPane(StackPane errorPane) {
        mainPane.getChildren().remove(errorPane);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showError(String details) {
        StackPane errorPane = generateError(details);
        errorPane.setLayoutX((getSceneWindowWidth() - errorPaneWidth) / 2);
        errorPane.setLayoutY(10);
        mainPane.getChildren().add(errorPane);
    }

    /**
     * {@inheritDoc}
     */
    public double getSceneWindowWidth() {
        return connectionSceneWidth;
    }

    /**
     * {@inheritDoc}
     */
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
        message.setStyle("-fx-text-fill: #dd2d2a");
    }


}
