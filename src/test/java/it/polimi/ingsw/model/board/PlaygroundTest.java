package it.polimi.ingsw.model.board;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.JsonDeserializer.CornerDeserializer;
import it.polimi.ingsw.model.card.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaygroundTest {

    @Test
    void contains() {
        Playground p = new Playground();

    }

    @Test
    void getAllPositions() {
        Playground p = new Playground();
        assertTrue(p.getAllPositions().contains(new Position(0, 0)));
    }

    @Test
    void getAvailablePositions() {
    }

    @Test
    void placeCard() {

        //test place back




    }

    private Deck<Card> createTestResourceCards(){
        Gson gson = new GsonBuilder().registerTypeAdapter(Corner.class, new CornerDeserializer()).create();
        Stack<Card> cards = new Stack<>();

        for (JsonElement j : getCardsFromJson(resourceCardsPath)){
            JsonObject jsonFront = j.getAsJsonObject().get("front").getAsJsonObject();
            JsonObject jsonBack = j.getAsJsonObject().get("back").getAsJsonObject();

            /* front logic */
            Color color = gson.fromJson(jsonFront.get("color"), Color.class);
            int score = gson.fromJson(jsonFront.get("score"), Integer.class);
            Map<CornerPosition, Corner> frontCorners = gson.fromJson(jsonFront.get("corners"), new TypeToken<>(){});

            /* back logic */
            Map<Symbol, Integer> backResources = gson.fromJson(jsonBack.get("resources"), new TypeToken<>(){});
            Map<CornerPosition, Corner> backCorners = new HashMap<>();
            Arrays.stream(CornerPosition.values()).forEach(cp -> backCorners.put(cp, new Corner()));

            Front front = new Front(color, frontCorners, score);
            Back back = new Back(color, backCorners, backResources);

            cards.add(new Card(front, back));
        }

        return new Deck<>(DeckType.RESOURCE, cards);
    }
}