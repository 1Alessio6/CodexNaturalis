package it.polimi.ingsw.network.client.view.gui.controllers;

import it.polimi.ingsw.model.lobby.FullLobbyException;
import it.polimi.ingsw.model.lobby.InvalidUsernameException;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.view.gui.ClientGUI;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Objects;

public class SelectUsernameScene extends SceneController{

    @FXML
    TextField usernameCatcher;

    public SelectUsernameScene(){}

    @FXML

    private void connect(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){


            gui.loadScene("/gui/ConnectionScene.fxml");



            try{
                gui.getController().connect(gui.getClient(), usernameCatcher.getPromptText());
            }catch (RemoteException e){
                //todo add a screen
            }
            catch (FullLobbyException e){
                //todo launch a full lobby screen and then restart from the first one
            }
            catch (InvalidUsernameException e){
                //could be changed with an error scene
                Alert alreadyTakenUsername = new Alert(Alert.AlertType.ERROR);
                alreadyTakenUsername.setTitle("Invalid Username");
                alreadyTakenUsername.setContentText("Username already taken");
                alreadyTakenUsername.show();
                usernameCatcher.setText("");
            }
        }
    }





}
