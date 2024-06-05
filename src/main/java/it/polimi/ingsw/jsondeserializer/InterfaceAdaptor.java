package it.polimi.ingsw.jsondeserializer;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Custom interface adapter used for serializing and deserializing
 */
public class InterfaceAdaptor<T> implements JsonSerializer<T>, JsonDeserializer<T> {

    private static final String CLASS_IDENTIFIER = "concreteType";

    private static final String ATTRIBUTES = "attributes";

    /**
     * {@inheritDoc}
     */
    public T deserialize(JsonElement jsonElement, Type type,
                         JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        // find the json entry mapped to CLASS_IDENTIFIER  and get it as string
        String className = jsonObject.get(CLASS_IDENTIFIER).getAsString();
        Class objectClass = getObjectClass(className); // get the concrete type
        return jsonDeserializationContext.deserialize(jsonObject.get(ATTRIBUTES), objectClass);
    }

    /**
     * {@inheritDoc}
     */
    public JsonElement serialize(T objToSerialize, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(CLASS_IDENTIFIER, objToSerialize.getClass().getName()); // get the class name not including the package
        jsonObject.add(ATTRIBUTES, jsonSerializationContext.serialize(objToSerialize)); // jsonSerializationContext calls the serialization method
        return jsonObject;
    }

    private Class getObjectClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e.getMessage());
        }
    }
}

