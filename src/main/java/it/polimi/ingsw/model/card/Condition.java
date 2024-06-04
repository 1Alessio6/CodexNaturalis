package it.polimi.ingsw.model.card;

/**
 * Enumeration representing the requisite conditions for obtaining the points indicated on the card
 */
public enum Condition {
    /**
     * Num_Quill, number of the quills required in the condition
     */
    NUM_QUILL,
    /**
     * Num_Manuscripts, number of manuscripts required in the condition
     */
    NUM_MANUSCRIPT,
    /**
     * Num_Inkwell, number of inkwells required in the condition
     */
    NUM_INKWELL,
    /**
     * Corners, if the condition in order to obtain points is to cover a corner
     */
    CORNERS
}
