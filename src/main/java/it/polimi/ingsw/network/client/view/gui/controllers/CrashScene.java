package it.polimi.ingsw.network.client.view.gui.controllers;

import it.polimi.ingsw.model.lobby.FullLobbyException;
import it.polimi.ingsw.model.lobby.InvalidUsernameException;
import it.polimi.ingsw.network.client.view.gui.SceneType;
import it.polimi.ingsw.network.client.view.gui.util.GUICards;
import it.polimi.ingsw.network.client.view.gui.util.GUIUtil;
import it.polimi.ingsw.network.client.view.gui.util.Icon;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.rmi.RemoteException;

import static it.polimi.ingsw.network.client.view.gui.util.GUICards.pathToImage;
import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.createMainBackground;
import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.isClicked;

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

    public void setLobbyFullMessage(){
        message.setText("LOBBY ALREADY FULL");
    }


}
