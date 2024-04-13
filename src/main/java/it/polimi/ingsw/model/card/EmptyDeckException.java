package it.polimi.ingsw.model.card;

public class EmptyDeckException extends Exception {
    EmptyDeckException(String message) {
        super(message);
    }
}
