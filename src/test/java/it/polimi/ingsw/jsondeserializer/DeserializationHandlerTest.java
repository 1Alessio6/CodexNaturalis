package it.polimi.ingsw.jsondeserializer;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Side;
import it.polimi.ingsw.model.card.strategies.CalculateResources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeserializationHandlerTest {
    private final String resourcePath = "src/main/resources/cards/expected.json";
    private final String outResourcePath = "src/main/resources/cards/got.json";
    private DeserializationHandler<Card> deserializationHandler;

    @BeforeEach
    void setUp() {
        deserializationHandler = new DeserializationHandler<>();
    }

    @Test
    void invalidPath_throwsException() {
        Assertions.assertThrows(FileNotFoundException.class, () -> deserializationHandler.jsonToList("/"));
    }

    @Test
    void validPath_doesNotThrowsException() {
        Assertions.assertDoesNotThrow(() -> deserializationHandler.jsonToList(resourcePath));
    }

    @Test
    void generateResourceCards() {
        deserializationHandler.registerAdapter(CalculateResources.class);

        List<Card> cardList = new ArrayList<>();

        try {
            cardList = deserializationHandler.jsonToList(resourcePath);
        } catch (Exception e) {
            assert false;
        }

        System.out.println(cardList.getFirst().getFace(Side.FRONT).getScore());

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outResourcePath));
            writer.write(deserializationHandler.listToJson(cardList));

            writer.close();

            File jsonHandMade = new File(resourcePath);
            File jsonHandler = new File(outResourcePath);

            Assertions.assertEquals(0, jsonHandMade.compareTo(jsonHandler));

        } catch (IOException e) {
            assert false;
        }

    }

}