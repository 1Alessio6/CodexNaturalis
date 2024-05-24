package it.polimi.ingsw.model.card;


/**
 * Enumeration representing the symbols that may occur.
 */
public enum Symbol {
    /**
     * Plant, refers to the resource that belong to the Plant kingdom.
     */
    PLANT("\uD83C\uDF43"),

    /**
     * Fungi, refers to the resource that belong to the Fungi kingdom.
     */
    FUNGI("\uD83C\uDF44"),

    /**
     * Animal, refers to the resource that belong to the Animal kingdom.
     */
    ANIMAL("\uD83D\uDC3A"),

    /**
     * Insect, refers to the resource that belong to the Insect kingdom.
     */
    INSECT("\uD83E\uDD8B"),

    /**
     * Quill, refers to the Quill object.
     */
    QUILL("\uD83E\uDEB6"),

    /**
     * Inkwell, refers to the Inkwell object.
     */
    INKWELL("\uD83E\uDED9"),

    /**
     * Manuscript, refers to the Manuscript object.
     */
    MANUSCRIPT("\uD83D\uDCDC");

    final String icon;

    Symbol(String icon) {
        this.icon = icon;
    }
}
