package it.polimi.ingsw.network.client.view.gui.controllers;

import it.polimi.ingsw.network.client.view.gui.ApplicationGUI;

public abstract class SceneController {
    protected ApplicationGUI gui;

    public void setGui(ApplicationGUI gui) {
        this.gui = gui;
    }

    public void initialize(){

    }

}
