package it.polimi.ingsw.network.client.view.gui.controllers;

import it.polimi.ingsw.model.lobby.InvalidPlayersNumberException;
import it.polimi.ingsw.network.client.view.gui.ClientGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;


import java.rmi.RemoteException;
import java.util.List;

public class LobbyScene extends SceneController {

    @FXML
    private TextArea connectedPlayers;

    @FXML
    private TextArea setNumberRequest;

    @FXML
    private ComboBox<Integer> numberPlayerCatcher;

    @FXML
    private Text requiredPlayer;


    public LobbyScene() {
    }

    public void initializeScene() {
        numberPlayerCatcher.getItems().addAll(2, 3, 4);
        connectedPlayers.setText("1 - " + gui.getController().getMainPlayerUsername());
    }

    public void setPlayerConnected(List<String> usernames){
        connectedPlayers.clear();
        for(int i = 0; i < usernames.size(); i++){
            connectedPlayers.setText(i + " - " + usernames.get(i));

            if(usernames.get(i).equals(gui.getController().getMainPlayerUsername())){
                connectedPlayers.setText(" LEADER");
            }

            connectedPlayers.setText("\n");
        }
    }

    @FXML
    private void setPlayersNumber() {

        try{
            gui.getController().setPlayersNumber(numberPlayerCatcher.getValue());
        }catch (InvalidPlayersNumberException ignored) {
        } catch (RemoteException e) {
            Alert remoteException = new Alert(Alert.AlertType.ERROR);
            remoteException.setTitle("Server Crashed");
            remoteException.setContentText("Remote Exception");
            remoteException.show();
        }

        numberPlayerCatcher.setVisible(false);
        setNumberRequest.setVisible(false);
        requiredPlayer.setText("Player required to play: " + numberPlayerCatcher.getValue());
        requiredPlayer.setVisible(true);

    }

}
