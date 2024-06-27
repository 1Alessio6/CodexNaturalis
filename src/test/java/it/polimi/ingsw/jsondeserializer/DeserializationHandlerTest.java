package it.polimi.ingsw.jsondeserializer;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.loader.CardsLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test to check the correct operation of the deserialization handler
 */
class DeserializationHandlerTest {
    /**
     * Test to check that an invalid path throws a FileNotFoundException
     */
    @Test
    void invalidPath_throwsException() {
        DeserializationHandler<GoldenFront> deserializationHandler = new DeserializationHandler<>();
        Assertions.assertThrows(FileNotFoundException.class, () -> deserializationHandler.jsonToList("/a", new TypeToken<>() {
        }));
    }

    /**
     * Test to check that a valid path doesn't throw any exception
     */
    @Test
    void validPath_doesNotThrowsException() {
        DeserializationHandler<Front> deserializationHandler = new DeserializationHandler<>();
        Assertions.assertDoesNotThrow(() -> deserializationHandler.jsonToList(CardsLoader.RESOURCE_FRONT_CARDS_PATH, new TypeToken<>() {
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
        DeserializationHandler<GoldenFront> deserializationHandler = new DeserializationHandler<>();
        TypeToken<List<GoldenFront>> listType = new TypeToken<>() {
        };

        Assertions.assertDoesNotThrow(
                () -> {
                    deserializationHandler.jsonToList(CardsLoader.GOLDEN_FRONT_CARDS_PATH, listType);
                });

        List<GoldenFront> goldenFronts = deserializationHandler.jsonToList(CardsLoader.GOLDEN_FRONT_CARDS_PATH, listType);

        Assertions.assertEquals(40, goldenFronts.size());
    }

    /**
     * Test to check if the deserialization handler deserializes the resource front cards correctly
     *
     * @throws FileNotFoundException if an error occurs during the opening of the file
     */
    @Test
    void generateResourceFrontCards() throws FileNotFoundException {
        DeserializationHandler<Front> deserializationHandler = new DeserializationHandler<>();
        TypeToken<List<Front>> listType = new TypeToken<>() {
        };

        Assertions.assertDoesNotThrow(
                () -> {
                    deserializationHandler.jsonToList(CardsLoader.RESOURCE_FRONT_CARDS_PATH, listType);
                });

        List<Front> l = deserializationHandler.jsonToList(CardsLoader.RESOURCE_FRONT_CARDS_PATH, listType);

        Assertions.assertEquals(40, l.size());
    }

    /**
     * Test to check if the deserialization handler deserializes the starting front cards correctly
     *
     * @throws FileNotFoundException if an error occurs during the opening of the file
     */
    @Test
    void generateStartingFrontCards() throws FileNotFoundException {
        DeserializationHandler<Front> deserializationHandler = new DeserializationHandler<>();
        TypeToken<List<Front>> listType = new TypeToken<>() {
        };

        Assertions.assertDoesNotThrow(
                () -> {
                    deserializationHandler.jsonToList(CardsLoader.STARTING_FRONT_CARDS_PATH, listType);
                });

        List<Front> l = deserializationHandler.jsonToList(CardsLoader.STARTING_FRONT_CARDS_PATH, listType);

        Assertions.assertEquals(6, l.size());
    }

    /**
     * Test to check if the deserialization handler deserializes the objective position front cards correctly
     *
     * @throws FileNotFoundException if an error occurs during the opening of the file
     */
    @Test
    void generateObjectivePositionFrontCards() throws FileNotFoundException {
        DeserializationHandler<ObjectivePositionCard> deserializationHandler = new DeserializationHandler<>();
        TypeToken<List<ObjectivePositionCard>> listType = new TypeToken<>() {
        };

        Assertions.assertDoesNotThrow(
                () -> {
                    deserializationHandler.jsonToList(CardsLoader.OBJECTIVE_POSITION_FRONT_CARDS_PATH, listType);
                });

        List<ObjectivePositionCard> l = deserializationHandler.jsonToList(CardsLoader.OBJECTIVE_POSITION_FRONT_CARDS_PATH, listType);

        Assertions.assertEquals(8, l.size());
    }

    /**
     * Test to check if the deserialization handler deserializes the objectives resources front cards correctly
     *
     * @throws FileNotFoundException if an error occurs during the opening of the file
     */
    @Test
    void generateObjectiveResourceFrontCards() throws FileNotFoundException {
        DeserializationHandler<ObjectiveResourceCard> deserializationHandler = new DeserializationHandler<>();
        TypeToken<List<ObjectiveResourceCard>> listType = new TypeToken<>() {
        };

        Assertions.assertDoesNotThrow(
                () -> {
                    deserializationHandler.jsonToList(CardsLoader.OBJECTIVE_RESOURCE_CARDS_PATH, listType);
                });

        List<ObjectiveResourceCard> l = deserializationHandler.jsonToList(CardsLoader.OBJECTIVE_RESOURCE_CARDS_PATH, listType);

        Assertions.assertEquals(8, l.size());
    }

    /**
     * Test to check if the deserialization handler deserializes the back of the cards correctly
     *
     * @throws FileNotFoundException if an error occurs during the opening the file
     */
    @Test
    void generateBackCards() throws FileNotFoundException {
        DeserializationHandler<Back> deserializationHandler = new DeserializationHandler<>();
        TypeToken<List<Back>> listType = new TypeToken<>() {
        };

        Assertions.assertDoesNotThrow(
                () -> {
                    deserializationHandler.jsonToList(CardsLoader.BACK_CARDS_PATH, listType);
                });

        List<Back> l = deserializationHandler.jsonToList(CardsLoader.BACK_CARDS_PATH, listType);

        Assertions.assertEquals(40, l.size());
    }

    /**
     * Test to check if the deserialization handler deserializes the starting back cards correctly
     *
     * @throws FileNotFoundException if an error occurs during the opening of the file
     */
    @Test
    void generateStartingBackCards() throws FileNotFoundException {
        DeserializationHandler<Back> deserializationHandler = new DeserializationHandler<>();
        TypeToken<List<Back>> listType = new TypeToken<>() {
        };

        Assertions.assertDoesNotThrow(
                () -> {
                    deserializationHandler.jsonToList(CardsLoader.STARTING_BACK_CARDS_PATH, listType);
                });

        List<Back> l = deserializationHandler.jsonToList(CardsLoader.STARTING_BACK_CARDS_PATH, listType);

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
        TypeToken<List<Front>> objectivePositionType = new TypeToken<>() {
        };

        List<Front> fronts = frontDeserializationHandler.jsonToList(CardsLoader.RESOURCE_FRONT_CARDS_PATH, objectivePositionType);

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

        List<GoldenFront> fronts = goldenFronts.jsonToList(CardsLoader.GOLDEN_FRONT_CARDS_PATH, goldenFrontType);

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

        List<Back> fronts = backDeserializationHandler.jsonToList(CardsLoader.BACK_CARDS_PATH, backType);

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

        List<ObjectivePositionCard> objectivePositionCards = objectivePositionDeserializer.jsonToList(CardsLoader.OBJECTIVE_POSITION_FRONT_CARDS_PATH, objectivePositionType);
        List<ObjectiveResourceCard> objectiveResourceCards = objectiveResourceDeserializer.jsonToList(CardsLoader.OBJECTIVE_RESOURCE_CARDS_PATH, objectiveResourceType);

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