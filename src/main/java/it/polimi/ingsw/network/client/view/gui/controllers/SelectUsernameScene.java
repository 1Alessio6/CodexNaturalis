package it.polimi.ingsw.network.client.view.gui.controllers;

import it.polimi.ingsw.model.lobby.FullLobbyException;
import it.polimi.ingsw.model.lobby.InvalidUsernameException;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.rmi.RemoteException;

import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.createMainBackground;

public class SelectUsernameScene extends SceneController{

    @FXML
    private Pane mainPane;

    @FXML
    private TextField usernameCatcher;

    public SelectUsernameScene(){}

    @Override
    public void initialize() {
       mainPane.setBackground(createMainBackground());
    }

    @FXML
    private void connect(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){
            try{
                System.out.println("Trying to connect");
                gui.getController().connect(usernameCatcher.getText());
                usernameCatcher.setText("");
            }catch (RemoteException e){
                //todo add a screen
                System.err.println("RemoteException: " + e.getMessage());
            }
        }
    }





}
