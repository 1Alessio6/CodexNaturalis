package it.polimi.ingsw.network.client.view.gui.controllers;

import it.polimi.ingsw.model.lobby.FullLobbyException;
import it.polimi.ingsw.model.lobby.InvalidUsernameException;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.client.controller.ClientController;
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

public class SelectUsernameScene {

    ClientController controller;

    VirtualView client;

    Stage primaryStage;

    @FXML
    TextField usernameCatcher;

    public SelectUsernameScene(){ //todo try to find a way to remove this constructor

    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public SelectUsernameScene(ClientController controller){
        this.controller = controller;
    }

    public void setClient(VirtualView client){
        this.client = client;
    }

    @FXML

    private void connect(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){

            try{
                loadScene("/gui/ConnectionScene.fxml");
            }catch (IOException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("IOException");
                alert.show();
            }


            try{
                controller.connect(client, usernameCatcher.getPromptText());
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

    private void loadScene(String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
        primaryStage.setTitle("Codex Naturalis");

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }



}
