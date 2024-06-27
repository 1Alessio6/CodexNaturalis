package it.polimi.ingsw.model.loader;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.jsondeserializer.DeserializationHandler;
import it.polimi.ingsw.model.Deck.Deck;
import it.polimi.ingsw.model.card.*;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads cards and give them to the model
 */
public class CardsLoader {
    public static final String BACK_CARDS_PATH = "/cards/backCards.json";
    public static final String GOLDEN_FRONT_CARDS_PATH = "/cards/goldenFrontCards.json";
    public static final String RESOURCE_FRONT_CARDS_PATH = "/cards/resourceFrontCards.json";
    public static final String STARTING_FRONT_CARDS_PATH = "/cards/startingFrontCards.json";
    public static final String STARTING_BACK_CARDS_PATH = "/cards/startingBackCards.json";
    public static final String OBJECTIVE_POSITION_FRONT_CARDS_PATH = "/cards/objectivePositionFrontCards.json";
    public static final String OBJECTIVE_RESOURCE_CARDS_PATH = "/cards/objectiveResourceFrontCards.json";

    private static List<Card> resourceCards;
    private static List<Card> goldenCards;
    private static List<Card> starterCards;
    private static List<ObjectiveCard> objectiveCards;

    static {
        resourceCards = new ArrayList<>();
        goldenCards = new ArrayList<>();
        starterCards = new ArrayList<>();
        objectiveCards = new ArrayList<>();
        try {
            resourceCards = createCardList(
                    new DeserializationHandler<Front>().jsonToList(RESOURCE_FRONT_CARDS_PATH, new TypeToken<>() {
                    }),
                    new DeserializationHandler<Back>().jsonToList(BACK_CARDS_PATH, new TypeToken<>() {
                    }));
            goldenCards =
                    createCardList(
                            frontFromGoldenList(GOLDEN_FRONT_CARDS_PATH),
                            new DeserializationHandler<Back>().jsonToList(BACK_CARDS_PATH, new TypeToken<>() {
                            })
                    );
            starterCards =
                    createCardList(
                            new DeserializationHandler<Front>().jsonToList(STARTING_FRONT_CARDS_PATH, new TypeToken<>() {
                            }),
                            new DeserializationHandler<Back>().jsonToList(STARTING_BACK_CARDS_PATH, new TypeToken<>() {
                            }));
            objectiveCards =
                    createObjectiveCardList(
                            new DeserializationHandler<ObjectivePositionCard>().jsonToList(OBJECTIVE_POSITION_FRONT_CARDS_PATH, new TypeToken<>() {
                            }),
                            new DeserializationHandler<ObjectiveResourceCard>().jsonToList(OBJECTIVE_RESOURCE_CARDS_PATH, new TypeToken<>() {
                            })
                    );
        } catch (FileNotFoundException e) {
            System.err.println("Error: "+ e.getMessage());
        }
    }

    private static List<Card> createCardList(List<Front> fronts, List<Back> backs) {
        assert fronts.size() == backs.size();

        List<Card> cards = new ArrayList<>();

        for (int i = 0; i < fronts.size(); ++i) {
            cards.add(new Card(fronts.get(i), backs.get(i)));
        }

        return cards;
    }

    private static List<ObjectiveCard> createObjectiveCardList(List<ObjectivePositionCard> objectivePositions, List<ObjectiveResourceCard> objectiveResources) {
        List<ObjectiveCard> objectiveCards = new ArrayList<>();

        objectiveCards.addAll(objectivePositions);

        objectiveCards.addAll(objectiveResources);

        return objectiveCards;
    }

    private static List<Front> frontFromGoldenList(String goldenFrontCardsPath) throws FileNotFoundException {
        List<GoldenFront> gFronts =
                new DeserializationHandler<GoldenFront>().jsonToList(goldenFrontCardsPath, new TypeToken<>() {
                });
        return new ArrayList<>(gFronts);
    }

    public static List<Card> getResourceCards() {
        return resourceCards;
    }

    public static List<Card> getGoldenCards() {
        return goldenCards;
    }

    public static List<Card> getStarterCards() {
        return starterCards;
    }

    public static List<ObjectiveCard> getObjectiveCards() {
        return objectiveCards;
    }
}
