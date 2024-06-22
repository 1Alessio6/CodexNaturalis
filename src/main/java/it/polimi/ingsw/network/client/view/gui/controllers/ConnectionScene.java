package it.polimi.ingsw.network.client.view.gui.controllers;


import it.polimi.ingsw.network.client.UnReachableServerException;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.view.gui.util.GUIUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.awt.event.MouseEvent;

import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.*;

/**
 * ConnectionScene is the controller concerning connection scene
 */
public class ConnectionScene extends SceneController {

    @FXML
    private Pane mainPane;
    @FXML
    private TextField ipField;

    @FXML
    private TextField portField;

    //@FXML
    //private Button exit;

    //@FXML
    //private Button connect;

    public ConnectionScene() {
    }

    //@FXML
    //private void enterTheIp() {
    //
    //}
    //
    //@FXML
    //private void enterThePort()

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeUsingGameInformation() {
        super.initializeUsingGameInformation();
        addButtonPane(mainPane, buttonPane, 352, 500);
    }

    @Override
    public void initialize() {
        Text welcomeMessage = new Text("Welcome");
        welcomeMessage.setFont(loadTitleFont(50));
        welcomeMessage.setLayoutX(414.0);
        welcomeMessage.setLayoutY(167.0);
        mainPane.getChildren().add(welcomeMessage);
        mainPane.setBackground(createMainBackground());
    }

    @FXML
    private void exitTheGame() {
        System.err.println("Exit from the gui");
        System.exit(0);
    }

    @FXML
    private void connect() {
        String ip = ipField.getText();
        String port = portField.getText();
        ClientController controller = gui.getController();
        try {
            controller.configureClient(gui, ip, Integer.parseInt(port));
            gui.showSelectUsername();
        } catch (UnReachableServerException e) {
            Alert serverAlert = new Alert(Alert.AlertType.ERROR);
            serverAlert.setTitle("Unable to connect to the server");
            serverAlert.setContentText("Exception: " + e.getMessage());
            serverAlert.show();
            ipField.setText("");
            portField.setText("");
        } catch (NumberFormatException e) {
            Alert serverAlert = new Alert(Alert.AlertType.ERROR);
            serverAlert.setTitle("Invalid IP + Port");
            serverAlert.setContentText("Exception: " + e.getMessage());
            serverAlert.show();
            ipField.setText("");
            portField.setText("");
        }
    }

    public double getSceneWindowWidth() {
        return connectionSceneWidth;
    }

    public double getSceneWindowHeight() {
        return connectionSceneHeight;
    }

}
