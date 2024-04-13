package it.polimi.ingsw.model.JsonDeserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.model.board.Position;

import java.lang.reflect.Type;

/**
 * Custom deserializer for Position:
 * assigns string values to x and y fields
 */
public class PositionDeserializer implements JsonDeserializer<Position> {
    @Override
    public Position deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        String[] positionString = json.getAsString().split(",");

        return new Position(Integer.parseInt(positionString[0]), Integer.parseInt(positionString[1]));
    }
}
