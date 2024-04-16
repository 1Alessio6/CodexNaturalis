package it.polimi.ingsw.model;

import com.google.gson.*;
import it.polimi.ingsw.model.card.strategies.CalculatePoints;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public class JsonTestCode<CalculatePoints> implements JsonSerializer<CalculatePoints>, JsonDeserializer<CalculatePoints> {

    private static final String CLASSNAME = "GINO";

    private static final String DATA = "DATA";

    public CalculatePoints deserialize(JsonElement jsonElement, Type type,
                                       JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        Class<CalculatePoints> mytype = getObjectClass(jsonElement.getAsString());
        try {
            return mytype.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public JsonElement serialize(CalculatePoints jsonElement, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(jsonElement.getClass().getName());
    }

    /** Helper method to get the className of the object to be deserialized **/
    public Class getObjectClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            throw new JsonParseException(e.getMessage());
        }


    }
}
