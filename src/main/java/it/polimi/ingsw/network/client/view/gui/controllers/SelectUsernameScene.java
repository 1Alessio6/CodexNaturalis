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

            gui.loadScene(SceneType.CONNECTION);

            try{
                VirtualView client = gui.getClient();
                String name = usernameCatcher.getText();
                gui.getController().connect(gui.getClient(), usernameCatcher.getText());
                client.setName(name);
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
