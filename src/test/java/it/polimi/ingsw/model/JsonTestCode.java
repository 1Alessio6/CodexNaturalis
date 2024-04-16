package it.polimi.ingsw.model;

import com.google.gson.*;
import it.polimi.ingsw.model.card.strategies.CalculatePoints;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public class JsonTestCode<T> implements JsonSerializer<T>, JsonDeserializer<T> {

    public T deserialize(JsonElement jsonElement, Type type,
                                       JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String calculatorPackageString = type.getTypeName().substring(0, type.getTypeName().lastIndexOf('.') + 1).concat(jsonElement.getAsString());

        Class<CalculatePoints> mytype = getObjectClass(calculatorPackageString);
        try {
            return (T) mytype.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public JsonElement serialize(T jsonElement, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(jsonElement.getClass().getSimpleName());
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
