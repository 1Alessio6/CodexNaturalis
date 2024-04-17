package it.polimi.ingsw.model.card.strategies;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Condition;
import it.polimi.ingsw.model.card.GoldenFront;
import it.polimi.ingsw.model.card.Symbol;
import java.util.EnumMap;
import static it.polimi.ingsw.model.card.strategies.CalculateResources.ConditionSymbolMapper.enumerationMap;

public class CalculateResources implements CalculatePoints {

    private GoldenFront gInstance;
    public CalculateResources(GoldenFront gInstance){
        this.gInstance=gInstance;
    }
    public CalculateResources(){
    }
    public int calculatePoints (Position pos, Playground playground) {

        int multiplier=playground.getTile(pos).getFace().getScore();
        Condition condition = gInstance.getPointsCondition();
        int result=0;

        if(playground.getResources().containsKey(conditionSymbolConversion(condition))){
            return playground.getResources().get(conditionSymbolConversion(condition))*multiplier;
        }

        return result;
    }

    public static class ConditionSymbolMapper{
        public static final EnumMap<Condition,Symbol> enumerationMap;
        static{
            enumerationMap = new EnumMap<>(Condition.class);
            enumerationMap.put(Condition.NUM_INKWELL,Symbol.INKWELL);
            enumerationMap.put(Condition.NUM_QUILL,Symbol.QUILL);
            enumerationMap.put(Condition.NUM_MANUSCRIPT,Symbol.MANUSCRIPT);
        }
    }
    public static Symbol conditionSymbolConversion(Condition condition){
        return enumerationMap.get(condition);
    }


}
