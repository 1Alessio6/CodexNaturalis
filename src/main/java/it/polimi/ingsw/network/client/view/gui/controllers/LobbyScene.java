package it.polimi.ingsw.network.client.view.gui.controllers;

import it.polimi.ingsw.model.lobby.InvalidPlayersNumberException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import java.rmi.RemoteException;
import java.util.List;

import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.createMainBackground;

public class LobbyScene extends SceneController {

    @FXML
    private Pane mainPane;

    @FXML
    private TextArea connectedPlayers;

    @FXML
    private TextArea setNumberRequest;

    @FXML
    private ComboBox<Integer> numberPlayerCatcher;

    @FXML
    private Text requiredPlayer;


    public LobbyScene() {}

    @Override
    public void initialize() {
        numberPlayerCatcher.getItems().addAll(2, 3, 4);
        //connectedPlayers.setText("1 - " + gui.getController().getMainPlayerUsername());
        numberPlayerCatcher.setVisible(false);
        setNumberRequest.setVisible(false);
        mainPane.setBackground(createMainBackground());

        //todo fix when fullScreen
    }


    public void showRequiredPlayers(){
        //todo requiredPlayer.setText("Player required to play: " + gui.getController().getRequiredPlayer);
        requiredPlayer.setVisible(true);
    }

    public void initializeCreatorScene() {
        numberPlayerCatcher.setVisible(true);
        setNumberRequest.setVisible(true);
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
        }

        numberPlayerCatcher.setVisible(false);
        setNumberRequest.setVisible(false);
        requiredPlayer.setText("Player required to play: " + numberPlayerCatcher.getValue());
        requiredPlayer.setVisible(true);

    }

}
