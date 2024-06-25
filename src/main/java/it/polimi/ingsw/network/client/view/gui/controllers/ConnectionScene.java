package it.polimi.ingsw.network.client.view.gui.controllers;


import it.polimi.ingsw.network.client.UnReachableServerException;
import it.polimi.ingsw.network.client.controller.ClientController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

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

    @FXML
    private Button connectButton;

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
        portField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    connect();
                }
            }
        });

        connectButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (isClicked(mouseEvent, MouseButton.PRIMARY)) {
                    connect();
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
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

    private void connect() {
        String ip = ipField.getText();
        String port = portField.getText();
        ClientController controller = gui.getController();
        try {
            controller.configureClient(gui, ip, Integer.parseInt(port));
            gui.showSelectUsername();
        } catch (UnReachableServerException e) {
            showError("Unable to connect to the server\nException: " + e.getMessage());
            ipField.setText("");
            portField.setText("");
        } catch (NumberFormatException e) {
            showError("Invalid IP/Port\nException:  " + e.getMessage());
            ipField.setText("");
            portField.setText("");
        }
    }

    @Override
    public void showError(String details) {
        StackPane errorPane = generateError(details);
        errorPane.setLayoutX((getSceneWindowWidth() - errorPaneWidth) / 2);
        errorPane.setLayoutY(10);
        mainPane.getChildren().add(errorPane);
    }

    @Override
    protected void removeErrorFromMainPane(StackPane errorPane) {
        mainPane.getChildren().remove(errorPane);
    }

    public double getSceneWindowWidth() {
        return connectionSceneWidth;
    }

    public double getSceneWindowHeight() {
        return connectionSceneHeight;
    }

}
