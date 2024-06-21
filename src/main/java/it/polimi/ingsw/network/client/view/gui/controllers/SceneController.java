package it.polimi.ingsw.network.client.view.gui.controllers;

import it.polimi.ingsw.network.client.view.gui.ApplicationGUI;

/**
 * SceneController is an interface for scene controllers
 */
public abstract class SceneController {
    protected ApplicationGUI gui;

    public void setGui(ApplicationGUI gui) {
        this.gui = gui;
    }

    /**
     * Method used to initialize the scene
     */
    public void initialize(){

    }

    /**
     * Method used to initialize game information
     */
    public void initializeUsingGameInformation(){
    }

}
