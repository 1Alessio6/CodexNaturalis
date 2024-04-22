package it.polimi.ingsw.model.jsondeserializer;

import com.google.gson.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;


// TODO: rename in case of use for generic interface deserialization

/**
 * Mainly used for CalculatePoints
 * @param <T> the interface
 */
public class CalculatorDeserializer<T> implements JsonSerializer<T>, JsonDeserializer<T> {

    /**
     * Deserializes the name of the calculator in json and creates the strategy from it
     * @param jsonElement The Json data being deserialized
     * @param type The type of the Object to deserialize to
     * @param jsonDeserializationContext ?
     * @return the Calculator
     * @throws JsonParseException if the Calculator in json isn't a defined one in strategies
     */
    public T deserialize(JsonElement jsonElement, Type type,
                         JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String calculatorPackageString = type.getTypeName().substring(0, type.getTypeName().lastIndexOf('.') + 1).concat(jsonElement.getAsString());

        Class<T> mytype = getObjectClass(calculatorPackageString);
        try {
            return mytype.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public JsonElement serialize(T jsonElement, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(jsonElement.getClass().getSimpleName());
    }

    /**
     * Helper method to get the className of the object to be deserialized
     **/
    public Class<T> getObjectClass(String className) {
        try {
            // Json should provide only our defined calculators
            return (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            throw new JsonParseException(e.getMessage());
        }
    }
}
