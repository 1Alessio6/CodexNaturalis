package it.polimi.ingsw.model.card;

public class Corner {
    private boolean isCovered;
    private final Symbol symbol;

    public Corner() {
        this.isCovered = false;
        this.symbol = null;
    }

    public Corner(Symbol symbol) {
        this.isCovered = false;
        this.symbol = symbol;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public boolean isCovered() {
        return isCovered;
    }

    public void setCovered() {
        this.isCovered = true;
    }

    @Override
    public String toString() {
        if(this.isCovered){
            return " Covered ";
        }
        if(symbol == null){
            return " EmptyCorner";
        }
        return String.valueOf(symbol);
    }
}
