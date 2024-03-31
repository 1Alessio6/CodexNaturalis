package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Playground;

import java.util.Map;

public class Front extends Face{
    private int score;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public Map<Symbol, Integer> requiredResources() {
        return null;
    }

    public int calcPoints(Playground p){
        return 0;
    }
}
