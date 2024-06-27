package it.polimi.ingsw.network.client.view.gui.controllers;


import it.polimi.ingsw.network.client.UnReachableServerException;
import it.polimi.ingsw.network.client.controller.ClientController;
import it.polimi.ingsw.network.client.view.gui.util.Icon;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
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

    @FXML
    private Label ipMessage;

    @FXML
    private Label portMessage;

    @FXML
    private Pane textPane;

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
        addButtonPane(mainPane, buttonPane, 850, 630);
        portField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    connect();
                }
            }
        });

        Button connectButton = new Button("Connect");
        connectButton.setLayoutY(228);
        connectButton.setLayoutX(93);
        ImageView connectImage = initializeIconImageView(Icon.CONNECT.getPath(),30);
        connectImage.setFitHeight(30);
        connectImage.setFitWidth(30);
        connectButton.setFont(loadFontLiberationSerifRegular(15.5));
        connectButton.setGraphic(connectImage);
        connectButton.setPrefSize(160, 40);
        textPane.getChildren().add(connectButton);



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
        welcomeMessage.setLayoutX(460.0);
        welcomeMessage.setLayoutY(130.0);
        mainPane.getChildren().add(welcomeMessage);
        mainPane.setBackground(createMainBackground());
        ipMessage.setFont(loadFontLiberationSerifRegular(16.5));
        portMessage.setFont(loadFontLiberationSerifRegular(16.5));

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
    protected void removeUpdatePaneFromMainPane(StackPane errorPane) {
        mainPane.getChildren().remove(errorPane);
    }

    public double getSceneWindowWidth() {
        return connectionSceneWidth;
    }

    public double getSceneWindowHeight() {
        return connectionSceneHeight;
    }

}
