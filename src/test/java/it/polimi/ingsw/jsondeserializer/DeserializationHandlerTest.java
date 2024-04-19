package it.polimi.ingsw.jsondeserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.card.strategies.CalculatePoints;
import it.polimi.ingsw.model.card.strategies.CalculateResources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

class DeserializationHandlerTest {
    private final String expectedResPath = "src/test/java/it/polimi/ingsw/jsondeserializer/expected.json";
    private final String backCardsPath = "src/main/resources/cards/backCards.json";
    private final String goldenFrontCardsPath = "src/main/resources/cards/goldenFrontCards.json";
    private final String resourceFrontCardsPath = "src/main/resources/cards/resourceFrontCards.json";
    private final String startingFrontCardsPath = "src/main/resources/cards/startingFrontCards.json";
    private final String startingBackCardsPath = "src/main/resources/cards/startingBackCards.json";
    private final String objectivePositionFrontCardsPath = "src/main/resources/cards/objectivePositionFrontCards.json";
    private final String objectiveResourceCardsPath = "src/main/resources/cards/objectiveResourceFrontCards.json";

    @Test
    void invalidPath_throwsException() {
        DeserializationHandler deserializationHandler = new DeserializationHandler();
        Assertions.assertThrows(FileNotFoundException.class, () -> deserializationHandler.jsonToList("/", new TypeToken<>() {
        }));
    }

    @Test
    void validPath_doesNotThrowsException() {
        DeserializationHandler deserializationHandler = new DeserializationHandler();
        Assertions.assertDoesNotThrow(() -> deserializationHandler.jsonToList(expectedResPath, new TypeToken<>() {
        }));
    }

    private <T> List<T> createList(int numItems, T item) {
        List<T> list = new ArrayList<>();

        for (int i = 0; i < numItems; i++) {
            list.add(item);
        }

        return list;
    }

    @Test
    void generateGoldenFrontCards() throws FileNotFoundException {
        DeserializationHandler<GoldenFront> deserializationHandler = new DeserializationHandler<GoldenFront>();
        TypeToken<List<GoldenFront>> listType = new TypeToken<List<GoldenFront>>() {
        };


        Assertions.assertDoesNotThrow(
                () -> {
                    deserializationHandler.jsonToList(goldenFrontCardsPath, listType);
                });

        List<GoldenFront> goldenFronts = deserializationHandler.jsonToList(goldenFrontCardsPath, listType);

        Assertions.assertEquals(40, goldenFronts.size());
    }

    @Test
    void generateResourceFrontCards() throws FileNotFoundException {
        DeserializationHandler<Front> deserializationHandler = new DeserializationHandler<Front>();
        TypeToken<List<Front>> listType = new TypeToken<List<Front>>() {
        };

        Assertions.assertDoesNotThrow(
                () -> {
                    deserializationHandler.jsonToList(resourceFrontCardsPath, listType);
                });

        List<Front> l = deserializationHandler.jsonToList(resourceFrontCardsPath, listType);

        Assertions.assertEquals(40, l.size());
    }

    @Test
    void generateStartingFrontCards() throws FileNotFoundException {
        DeserializationHandler<Front> deserializationHandler = new DeserializationHandler<Front>();
        TypeToken<List<Front>> listType = new TypeToken<List<Front>>() {
        };

        Assertions.assertDoesNotThrow(
                () -> {
                    deserializationHandler.jsonToList(startingFrontCardsPath, listType);
                });

        List<Front> l = deserializationHandler.jsonToList(startingFrontCardsPath, listType);

        Assertions.assertEquals(6, l.size());
    }

    @Test
    void generateObjectivePositionFrontCards() throws FileNotFoundException {
        DeserializationHandler<ObjectivePositionCard> deserializationHandler = new DeserializationHandler<ObjectivePositionCard>();
        TypeToken<List<ObjectivePositionCard>> listType = new TypeToken<List<ObjectivePositionCard>>() {
        };

        Assertions.assertDoesNotThrow(
                () -> {
                    deserializationHandler.jsonToList(objectivePositionFrontCardsPath, listType);
                });

        List<ObjectivePositionCard> l = deserializationHandler.jsonToList(objectivePositionFrontCardsPath, listType);

        Assertions.assertEquals(8, l.size());
    }

    @Test
    void generateObjectiveResourceFrontCards() throws FileNotFoundException {
        DeserializationHandler<ObjectiveResourceCard> deserializationHandler = new DeserializationHandler<ObjectiveResourceCard>();
        TypeToken<List<ObjectiveResourceCard>> listType = new TypeToken<List<ObjectiveResourceCard>>() {
        };

        Assertions.assertDoesNotThrow(
                () -> {
                    deserializationHandler.jsonToList(objectiveResourceCardsPath, listType);
                });

        List<ObjectiveResourceCard> l = deserializationHandler.jsonToList(objectiveResourceCardsPath, listType);

        Assertions.assertEquals(8, l.size());
    }

    @Test
    void generateBackCards() throws FileNotFoundException {
        DeserializationHandler<Back> deserializationHandler = new DeserializationHandler<Back>();
        TypeToken<List<Back>> listType = new TypeToken<List<Back>>() {
        };

        Assertions.assertDoesNotThrow(
                () -> {
                    deserializationHandler.jsonToList(backCardsPath, listType);
                });

        List<Back> l = deserializationHandler.jsonToList(backCardsPath, listType);

        Assertions.assertEquals(40, l.size());
    }

    @Test
    void generateStartingBackCards() throws FileNotFoundException {
        DeserializationHandler<Back> deserializationHandler = new DeserializationHandler<Back>();
        TypeToken<List<Back>> listType = new TypeToken<List<Back>>() {
        };

        Assertions.assertDoesNotThrow(
                () -> {
                    deserializationHandler.jsonToList(startingBackCardsPath, listType);
                });

        List<Back> l = deserializationHandler.jsonToList(startingBackCardsPath, listType);

        Assertions.assertEquals(6, l.size());
    }

}