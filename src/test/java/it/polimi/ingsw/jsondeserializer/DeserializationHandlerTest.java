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

import java.io.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

class DeserializationHandlerTest {
    private final String expectedResPath = "src/test/java/it/polimi/ingsw/jsondeserializer/expected.json";
    private final String outResourcePath = "src/main/resources/cards/got.json";

    @Test
    void invalidPath_throwsException() {
        DeserializationHandler deserializationHandler = new DeserializationHandler();
        Assertions.assertThrows(FileNotFoundException.class, () -> deserializationHandler.jsonToList("/", new TypeToken<>(){}));
    }

    @Test
    void validPath_doesNotThrowsException() {
        DeserializationHandler deserializationHandler = new DeserializationHandler();
        Assertions.assertDoesNotThrow(() -> deserializationHandler.jsonToList(expectedResPath, new TypeToken<>(){}));
    }

    private <T> List<T> createList(int numItems, T item) {
        List<T> list = new ArrayList<>();

        for (int i = 0; i < numItems; i++) {
            list.add(item);
        }

        return list;
    }

    @Test
    void generateResourceCards() {
        List<GoldenFront> goldenFrontList = createList(
                2,
                new GoldenFront(
                        Color.BLUE,
                        new HashMap<>(),
                        10,
                        Condition.CORNERS,
                        new CalculateResources(),
                        new HashMap<>()
                ));

        DeserializationHandler<GoldenFront> deserializationHandler = new DeserializationHandler<GoldenFront>();

        TypeToken<List<GoldenFront>> listType = new TypeToken<List<GoldenFront>>() {
        };

        List<GoldenFront> gf = null;
        try {
            gf = deserializationHandler.jsonToList(expectedResPath, listType);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertEquals(goldenFrontList, gf);
    }

}