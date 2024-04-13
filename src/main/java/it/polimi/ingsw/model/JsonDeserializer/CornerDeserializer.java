package it.polimi.ingsw.model.JsonDeserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.model.card.Corner;
import it.polimi.ingsw.model.card.Symbol;

import java.lang.reflect.Type;

public class CornerDeserializer implements JsonDeserializer<Corner> {
    @Override
    public Corner deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return new Corner(Symbol.valueOf(json.getAsString()));
    }
}
