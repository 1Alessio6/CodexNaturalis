package it.polimi.ingsw.network.client.view.gui.controllers;

import it.polimi.ingsw.model.lobby.FullLobbyException;
import it.polimi.ingsw.model.lobby.InvalidUsernameException;
import it.polimi.ingsw.network.VirtualView;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;

import java.rmi.RemoteException;

public class SelectUsernameScene extends SceneController{

    @FXML
    TextField usernameCatcher;

    public SelectUsernameScene(){}

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
