package it.polimi.ingsw.model.card;


/**
 * Enumeration representing the symbols that may occur.
 */
public enum Symbol {
    /**
     * Plant, refers to the resource that belong to the Plant kingdom.
     */
    PLANT("\uD83C\uDF43", "/gui/png/symbols/PLANT.png"),

    /**
     * Fungi, refers to the resource that belong to the Fungi kingdom.
     */
    FUNGI("\uD83C\uDF44", "/gui/png/symbols/FUNGI.png"),

    /**
     * Animal, refers to the resource that belong to the Animal kingdom.
     */
    ANIMAL("\uD83D\uDC3A", "/gui/png/symbols/ANIMAL.png"),

    /**
     * Insect, refers to the resource that belong to the Insect kingdom.
     */
    INSECT("\uD83E\uDD8B","/gui/png/symbols/INSECT.png"),

    /**
     * Quill, refers to the Quill object.
     */
    QUILL("\uD83E\uDEB6","/gui/png/symbols/QUILL.png"),

    /**
     * Inkwell, refers to the Inkwell object.
     */
    INKWELL("\uD83E\uDED9","/gui/png/symbols/INKWELL.png"),

    /**
     * Manuscript, refers to the Manuscript object.
     */
    MANUSCRIPT("\uD83D\uDCDC", "/gui/png/symbols/MANUSCRIPT.png");

    final String icon;
    final String pngPath;

    /**
     * Constructs a <code>Symbol</code> with the <code>icon</code> and the <code>pngPath</code> provided
     *
     * @param icon    the icon encoding
     * @param pngPath the icon path
     */
    Symbol(String icon, String pngPath) {
        this.icon = icon;
        this.pngPath = pngPath;
    }

    public String getIcon() {
        return this.icon;
    }

    public String getPath() {
        return pngPath;
    }
}
