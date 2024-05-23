package it.polimi.ingsw.network.client.view.gui.controllers;

import it.polimi.ingsw.network.client.view.gui.ClientGUI;

public abstract class SceneController {
    protected ClientGUI gui;

    public void setGui(ClientGUI gui) {
        this.gui = gui;
    }

    public void initialize(){

    }

}
