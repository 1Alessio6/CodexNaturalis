package it.polimi.ingsw.model.player.action;

/**
 * Enumeration representing the states of the player's actions
 */
public enum PlayerState {
    /**
     * Choose Starter refers to the state in which the player chooses the starter card side and its placement
     */
    ChooseStarter,

    /**
     * Choose Color refers to the state in which the player chooses its color
     */
    ChooseColor,

    /**
     * Choose Objective refers to the state in which the player chooses its secret objective card
     */
    ChooseObjective,

    /**
     * Place refers to the state in which the player places a card
     */
    Place,

    /**
     * Draw refers to the state in which the player draws a card
     */
    Draw
}
