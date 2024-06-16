package it.polimi.ingsw.network.client.view.tui.drawplayground;

public class InvalidCardDimensionException extends UndrawablePlaygroundException{
    public InvalidCardDimensionException() {
        super("Invalid card dimension");
    }
}
