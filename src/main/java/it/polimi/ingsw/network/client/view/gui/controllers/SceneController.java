package it.polimi.ingsw.network.client.view.gui.controllers;

import it.polimi.ingsw.network.client.view.gui.ApplicationGUI;
import it.polimi.ingsw.network.client.view.gui.util.Icon;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.io.File;

import static it.polimi.ingsw.network.client.view.gui.util.GUIUtil.initializeIconImageView;

/**
 * SceneController is an interface for scene controllers
 */
public abstract class SceneController {
    protected ApplicationGUI gui;

    protected Button settings;

    protected Button rulebook;

    private boolean rulebookOpen;

    private static final double rulebookPageWidth = 561.6;

    private static final double rulebookPageHeight= 562.8;


    public void setGui(ApplicationGUI gui) {
        this.gui = gui;
    }

    /**
     * Method used to initialize the scene
     */
    public void initialize() {

        settings = new Button();
        settings.setPrefSize(40, 40);
        settings.setGraphic(initializeIconImageView(Icon.SETTINGS.getPath(), 30));

        settings.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

            }
        });


    }

    public void initializeSettings() {

    }

    public void initializeRulebook() {

        rulebook = new Button();
        rulebookOpen = false;
        rulebook.setPrefSize(40, 40);
        rulebook.setGraphic(initializeIconImageView(Icon.RULEBOOK.getPath(), 30));

        Pagination rulebookPagination = initializeRulebookPagination();
        Scene rulebookScene = new Scene(rulebookPagination, rulebookPageWidth, rulebookPageHeight);
        Stage rulebookStage = new Stage();
        rulebookStage.initOwner(gui.getPrimaryStage());
        rulebookStage.setTitle("Rulebook");
        rulebookStage.setScene(rulebookScene);
        rulebookStage.setResizable(true);

        rulebookStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                rulebookOpen = false;
            }
        });

        rulebook.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!rulebookOpen) {
                    rulebookStage.show();
                    rulebookOpen = true;
                } else {
                    rulebookStage.close();
                    rulebookOpen = false;
                }
            }
        });
    }

    private Pagination initializeRulebookPagination() {
        Pagination rulebookPagination = new Pagination(12);
        rulebookPagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                pageIndex++;
                return createRulebookPage(pageIndex);
            }
        });

        return rulebookPagination;
    }

    private ImageView createRulebookPage(int pageIndex) {
        assert (pageIndex >= 0 && pageIndex < 12);
        ImageView page = new ImageView("/gui/png/rulebook/CODEX_Rulebook_IT-" + pageIndex + ".png");
        page.setFitWidth(rulebookPageWidth);
        page.setFitHeight(rulebookPageHeight);

        return page;
    }

    /**
     * Method used to initialize game information
     */
    public void initializeUsingGameInformation() {
    }

}
