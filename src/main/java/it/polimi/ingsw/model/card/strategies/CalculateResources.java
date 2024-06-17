package it.polimi.ingsw.model.card.strategies;

import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Condition;
import it.polimi.ingsw.model.card.GoldenFront;
import it.polimi.ingsw.model.card.Symbol;

import java.util.EnumMap;

import static it.polimi.ingsw.model.card.strategies.CalculateResources.ConditionSymbolMapper.enumerationMap;

/**
 * Strategy used for golden cards which add points based on the number of visible resources present in the playground
 */
public class CalculateResources implements CalculatePoints {
    /**
     * Calculates the points in the golden cards with resources as condition case.
     *
     * @param pos        in the playground.
     * @param playground on which the calculation is to be carried .
     * @return the calculated points.
     */
    public int calculatePoints(Position pos, Playground playground) {
        Condition calculatorCondition = playground.getTile(pos).getFace().getCondition();
        int multiplier = playground.getTile(pos).getFace().getScore();
        int result = 0;

        if (playground.getResources().containsKey(conditionSymbolConversion(calculatorCondition))) {
            return playground.getResources().get(conditionSymbolConversion(calculatorCondition)) * multiplier;
        }

        return result;
    }

    /**
     * Condition-Symbol Mapper class creates a symmetrical map between Condition and Symbol enumerations.
     */
    public static class ConditionSymbolMapper {

        /**
         * Represents the Condition-Symbol conversion map.
         */
        public static final EnumMap<Condition, Symbol> enumerationMap;

        static {
            enumerationMap = new EnumMap<>(Condition.class);
            enumerationMap.put(Condition.NUM_INKWELL, Symbol.INKWELL);
            enumerationMap.put(Condition.NUM_QUILL, Symbol.QUILL);
            enumerationMap.put(Condition.NUM_MANUSCRIPT, Symbol.MANUSCRIPT);
        }
    }


    /**
     * Converts a specific Symbol constant into a Condition constant.
     *
     * @param condition of the card.
     * @return the corresponding symbol given a particular condition.
     */
    public static Symbol conditionSymbolConversion(Condition condition) {
        return enumerationMap.get(condition);
    }

    /**
     * Checks if two objects are equal.
     *
     * @param o the object to be compared
     * @return true if this object is equal to <code>o</code>, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return true;
    }

}
