package it.polimi.ingsw.model.JsonDeserializer;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.strategies.CalculateCorners;
import it.polimi.ingsw.model.card.strategies.CalculateNoCondition;
import it.polimi.ingsw.model.card.strategies.CalculatePoints;
import it.polimi.ingsw.model.card.strategies.CalculateResources;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * Class made for static methods that work for both tests and game
 */
public class CustomJsonDeserializer {
    private final static String goldenCardsPath = "/cards/goldenCards.json";
    private final static String resourceCardsPath = "/cards/resourceCards.json";
    private final static String startingCardsPath = "/cards/startingCards.json";
    private final static String objectivePositionCardsPath = "/cards/objectivePositionCards.json";
    private final static String objectiveResourceCardsPath = "/cards/objectiveResourceCards.json";

    /**
     * Converts the file to a parsable json array
     * @param resourcePath the resource to deserialize
     * @return the entire file as json array
     */
    private static JsonArray getCardsFromJson(String resourcePath) throws NullPointerException {
        /* json as streams, so even after jar build it can retrieve the correct file */
        InputStream resourceAsStream = CustomJsonDeserializer.class
                .getResourceAsStream(resourcePath);
        if (resourceAsStream == null)
            throw new NullPointerException("Empty resource!");

        Reader cardReader = new BufferedReader(new InputStreamReader(resourceAsStream));

        return JsonParser.parseReader(cardReader).getAsJsonArray();
    }

    // TODO: few code repetition could be avoided
    public static List<Card> createGoldenCards(){
        Gson gson = new GsonBuilder().registerTypeAdapter(Corner.class, new CornerDeserializer()).create();
        List<Card> cards = new Stack<>();

        for (JsonElement j : getCardsFromJson(goldenCardsPath)){
            JsonObject jsonFront = j.getAsJsonObject().get("front").getAsJsonObject();
            JsonObject jsonBack = j.getAsJsonObject().get("back").getAsJsonObject();

            /* front logic */
            Color color = gson.fromJson(jsonFront.get("color"), Color.class);
            int score = gson.fromJson(jsonFront.get("score"), Integer.class);
            Condition pointsCondition = gson.fromJson(jsonFront.get("pointsCondition"), Condition.class);
            Map<CornerPosition, Corner> frontCorners = gson.fromJson(jsonFront.get("corners"), new TypeToken<>(){});
            Map<Symbol, Integer> requirements = gson.fromJson(jsonFront.get("requirements"), new TypeToken<>(){});

            /* create calculator */
            CalculatePoints calculator = switch (pointsCondition) {
                case CORNERS -> new CalculateCorners();
                case NUM_MANUSCRIPT, NUM_INKWKELL, NUM_QUILL -> new CalculateResources();
                case null -> new CalculateNoCondition();
            };

            /* back logic */
            Map<Symbol, Integer> backResources = gson.fromJson(jsonBack.get("resources"), new TypeToken<>(){});
            Map<CornerPosition, Corner> backCorners = new HashMap<>();
            Arrays.stream(CornerPosition.values()).forEach(cp -> backCorners.put(cp, new Corner()));

            Front front = new GoldenFront(color, frontCorners, score, pointsCondition, calculator, requirements);
            Back back = new Back(color, backCorners, backResources);

            cards.add(new Card(front, back));
        }

        return cards;
    }

    public static List<Card> createResourceCards(){
        Gson gson = new GsonBuilder().registerTypeAdapter(Corner.class, new CornerDeserializer()).create();
        List<Card> cards = new Stack<>();

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

        return cards;
    }

    public static List<Card> createStartingCards() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Corner.class, new CornerDeserializer()).create();
        List<Card> cards = new Stack<>();

        for (JsonElement j : getCardsFromJson(startingCardsPath)){
            JsonObject jsonFront = j.getAsJsonObject().get("front").getAsJsonObject();
            JsonObject jsonBack = j.getAsJsonObject().get("back").getAsJsonObject();

            /* front logic */
            Map<CornerPosition, Corner> frontCorners = gson.fromJson(jsonFront.get("corners"), new TypeToken<>(){});

            /* back logic */
            Map<Symbol, Integer> backResources = gson.fromJson(jsonBack.get("resources"), new TypeToken<>(){});
            /* starting cards are the only ones to have resources in back corners */
            Map<CornerPosition, Corner> backCorners = gson.fromJson(jsonBack.get("corners"), new TypeToken<>(){});

            Front front = new Front(frontCorners);
            Back back = new Back(null, backCorners, backResources);

            cards.add(new Card(front, back));
        }

        return cards;
    }

    public static List<ObjectiveCard> createObjectiveCards() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Position.class, new PositionDeserializer()).create();
        Stack<ObjectiveCard> cards = new Stack<>();

        for (JsonElement j : getCardsFromJson(objectivePositionCardsPath)){
            Map<Position, Color> condition = gson.fromJson(j.getAsJsonObject().get("condition"), new TypeToken<>(){});
            int multiplier = gson.fromJson(j.getAsJsonObject().get("multiplier"), Integer.class);

            cards.add(new ObjectivePositionCard(condition, multiplier));
        }

        for (JsonElement j : getCardsFromJson(objectiveResourceCardsPath)){
            Map<Symbol, Integer> condition = gson.fromJson(j.getAsJsonObject().get("condition"), new TypeToken<>(){});
            int multiplier = gson.fromJson(j.getAsJsonObject().get("multiplier"), Integer.class);

            cards.add(new ObjectiveResourceCard(condition, multiplier));
        }

        return cards;
    }
}
