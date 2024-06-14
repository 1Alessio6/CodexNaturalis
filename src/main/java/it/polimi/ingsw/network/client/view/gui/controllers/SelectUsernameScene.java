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
                gui.getController().connect(usernameCatcher.getText());
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
                //alreadyTakenUsername.setContentText("Username already taken");
                alreadyTakenUsername.setContentText("Exception: " + e.getMessage());
                alreadyTakenUsername.show();
                usernameCatcher.setText("");
            }
        }
    }





}
