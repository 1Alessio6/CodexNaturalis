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

/**
 * Test to check the correct operation of the deserialization handler
 */
class DeserializationHandlerTest {
    private final String expectedResPath = "src/test/java/it/polimi/ingsw/jsondeserializer/expected.json";
    private final String backCardsPath = "src/main/resources/cards/backCards.json";
    private final String goldenFrontCardsPath = "src/main/resources/cards/goldenFrontCards.json";
    private final String resourceFrontCardsPath = "src/main/resources/cards/resourceFrontCards.json";
    private final String startingFrontCardsPath = "src/main/resources/cards/startingFrontCards.json";
    private final String startingBackCardsPath = "src/main/resources/cards/startingBackCards.json";
    private final String objectivePositionFrontCardsPath = "src/main/resources/cards/objectivePositionFrontCards.json";
    private final String objectiveResourceCardsPath = "src/main/resources/cards/objectiveResourceFrontCards.json";

    /**
     * Test to check that an invalid path throws a FileNotFoundException
     */
    @Test
    void invalidPath_throwsException() {
        DeserializationHandler deserializationHandler = new DeserializationHandler();
        Assertions.assertThrows(FileNotFoundException.class, () -> deserializationHandler.jsonToList("/", new TypeToken<>() {
        }));
    }

    /**
     * Test to check that a valid path doesn't throw any exception
     */
    @Test
    void validPath_doesNotThrowsException() {
        DeserializationHandler deserializationHandler = new DeserializationHandler();
        Assertions.assertDoesNotThrow(() -> deserializationHandler.jsonToList(expectedResPath, new TypeToken<>() {
        }));
    }

    /**
     * Method used to create a list of <code>T</code> items
     *
     * @param numItems the number of items of the list
     * @param item     the item
     * @param <T>      the kind of the item
     * @return a list of <code>T</code> items
     */
    private <T> List<T> createList(int numItems, T item) {
        List<T> list = new ArrayList<>();

        for (int i = 0; i < numItems; i++) {
            list.add(item);
        }

        return list;
    }

    /**
     * Test to check if the deserialization handler deserializes the golden front cards correctly
     *
     * @throws FileNotFoundException if an error occurs during the opening of the file
     */
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

    /**
     * Test to check if the deserialization handler deserializes the resource front cards correctly
     *
     * @throws FileNotFoundException if an error occurs during the opening of the file
     */
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

    /**
     * Test to check if the deserialization handler deserializes the starting front cards correctly
     *
     * @throws FileNotFoundException if an error occurs during the opening of the file
     */
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

    /**
     * Test to check if the deserialization handler deserializes the objective position front cards correctly
     *
     * @throws FileNotFoundException if an error occurs during the opening of the file
     */
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

    /**
     * Test to check if the deserialization handler deserializes the objectives resources front cards correctly
     *
     * @throws FileNotFoundException if an error occurs during the opening of the file
     */
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

    /**
     * Test to check if the deserialization handler deserializes the back of the cards correctly
     *
     * @throws FileNotFoundException if an error occurs during the opening the file
     */
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

    /**
     * Test to check if the deserialization handler deserializes the starting back cards correctly
     *
     * @throws FileNotFoundException if an error occurs during the opening of the file
     */
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

    /**
     * Tests that every face has a different ID
     *
     * @param faces the faces to test
     */
    private void testUniqueIdsForFaces(List<Face> faces) {
        for (int i = 0; i < faces.size(); ++i) {
            for (int j = 0; j < faces.size(); ++j) {
                if (i==j) continue;
                Assertions.assertNotEquals(faces.get(i).getId(), faces.get(j).getId());
            }
        }
    }

    /**
     * Tests that every front has a different ID
     *
     * @throws FileNotFoundException if an error occurs during the opening of the file
     */
    @Test
    void testUniqueFrontId() throws FileNotFoundException {
        DeserializationHandler<Front> frontDeserializationHandler = new DeserializationHandler<>();
        TypeToken<List<Front>> objectivePositionType = new TypeToken<List<Front>>() {
        };

        List<Front> fronts = frontDeserializationHandler.jsonToList(resourceFrontCardsPath, objectivePositionType);

        testUniqueIdsForFaces(new ArrayList<>(fronts));
    }

    /**
     * Tests that every golden front has a different ID
     *
     * @throws FileNotFoundException if an error occurs during the opening of the file
     */
    @Test
    void testUniqueGoldenFrontId() throws FileNotFoundException {
        DeserializationHandler<GoldenFront> goldenFronts = new DeserializationHandler<>();
        TypeToken<List<GoldenFront>> goldenFrontType = new TypeToken<List<GoldenFront>>() {
        };

        List<GoldenFront> fronts = goldenFronts.jsonToList(goldenFrontCardsPath, goldenFrontType);

        testUniqueIdsForFaces(new ArrayList<>(fronts));
    }

    /**
     * Tests that every back has a different ID
     *
     * @throws FileNotFoundException if an error occurs during the opening of the file
     */
    @Test
    void testUniqueBackId() throws FileNotFoundException {
        DeserializationHandler<Back> backDeserializationHandler = new DeserializationHandler<>();
        TypeToken<List<Back>> backType = new TypeToken<List<Back>>() {
        };

        List<Back> fronts = backDeserializationHandler.jsonToList(backCardsPath, backType);

        testUniqueIdsForFaces(new ArrayList<>(fronts));
    }

    /**
     * Tests that every objective card has a different ID
     *
     * @throws FileNotFoundException if an error occurs during the opening of the file
     */
    @Test
    void testUniqueObjectiveCardId() throws FileNotFoundException {
        DeserializationHandler<ObjectivePositionCard> objectivePositionDeserializer = new DeserializationHandler<ObjectivePositionCard>();
        TypeToken<List<ObjectivePositionCard>> objectivePositionType = new TypeToken<List<ObjectivePositionCard>>() {
        };

        DeserializationHandler<ObjectiveResourceCard> objectiveResourceDeserializer = new DeserializationHandler<ObjectiveResourceCard>();
        TypeToken<List<ObjectiveResourceCard>> objectiveResourceType = new TypeToken<List<ObjectiveResourceCard>>() {
        };

        List<ObjectivePositionCard> objectivePositionCards = objectivePositionDeserializer.jsonToList(objectivePositionFrontCardsPath, objectivePositionType);
        List<ObjectiveResourceCard> objectiveResourceCards = objectiveResourceDeserializer.jsonToList(objectiveResourceCardsPath, objectiveResourceType);

        // unique ids between objective position
        for (int i = 0; i < objectivePositionCards.size(); ++i) {
            for (int j = 0; j < objectivePositionCards.size(); ++j) {
                if (i == j) continue;
                Assertions.assertNotEquals(objectivePositionCards.get(i), objectivePositionCards.get(j));
            }
        }

        // unique ids between objective resource
        for (int i = 0; i < objectiveResourceCards.size(); ++i) {
            for (int j = 0; j < objectiveResourceCards.size(); ++j) {
                if (i == j) continue;
                Assertions.assertNotEquals(objectiveResourceCards.get(i), objectiveResourceCards.get(j));
            }
        }

        // unique ids between objective position and resource cards
        for (int i = 0; i < objectivePositionCards.size(); ++i) {
            for (int j = 0; j < objectiveResourceCards.size(); ++j) {
                Assertions.assertNotEquals(objectivePositionCards.get(i), objectiveResourceCards.get(j));
            }
        }
    }
}