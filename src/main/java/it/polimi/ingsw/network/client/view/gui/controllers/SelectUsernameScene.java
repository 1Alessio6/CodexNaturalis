package it.polimi.ingsw.network.client.view.gui.controllers;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.createMainBackground;

/**
 * SelectUsernameScene is the controller concerning selectUsername scene
 */
public class SelectUsernameScene extends SceneController{

    @FXML
    private Pane mainPane;

    @FXML
    private TextField usernameCatcher;

    public SelectUsernameScene(){}

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
       mainPane.setBackground(createMainBackground());
    }

    @FXML
    private void connect(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){
            System.out.println("Trying to connect");
            gui.getController().connect(usernameCatcher.getText());
            usernameCatcher.setText("");
        }
    }





}
