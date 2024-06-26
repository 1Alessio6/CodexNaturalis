package it.polimi.ingsw.network.client.view.gui.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.*;

/**
 * SelectUsernameScene is the controller concerning selectUsername scene
 */
public class SelectUsernameScene extends SceneController {

    @FXML
    private Pane mainPane;

    @FXML
    private TextField usernameCatcher;

    private boolean reachedMaximumCharacters;

    public SelectUsernameScene(){}

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        mainPane.setBackground(createMainBackground());
        reachedMaximumCharacters = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeUsingGameInformation() {
        super.initializeUsingGameInformation();
        addButtonPane(mainPane, buttonPane, 860, 650);
        usernameCatcher.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    if(!reachedMaximumCharacters) {
                        System.out.println("Trying to connect");
                        gui.getController().connect(usernameCatcher.getText());
                        usernameCatcher.setText("");
                    }
                    else{
                        showError("Your username is too long\nyou exceeded the maximum characters");
                    }
                }

            }
        });

        usernameCatcher.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(usernameCatcher.getText().length() > 12){
                    usernameCatcher.setStyle("-fx-text-fill: #FF0000;");
                    reachedMaximumCharacters = true;
                }

                if(reachedMaximumCharacters && usernameCatcher.getText().length() <= 12){
                    usernameCatcher.setStyle("-fx-text-fill: #000000;");
                    reachedMaximumCharacters = false;
                }
            }
        });
    }

    @Override
    protected void removeUpdatePaneFromMainPane(StackPane errorPane) {
        mainPane.getChildren().remove(errorPane);
    }

    @Override
    public void showError(String details) {
        StackPane errorPane = generateError(details);
        errorPane.setLayoutX((getSceneWindowWidth() - errorPaneWidth)/2);
        errorPane.setLayoutY(10);
        mainPane.getChildren().add(errorPane);
    }

    public double getSceneWindowWidth() {
        return connectionSceneWidth;
    }

    public double getSceneWindowHeight() {
        return connectionSceneHeight;
    }

}
