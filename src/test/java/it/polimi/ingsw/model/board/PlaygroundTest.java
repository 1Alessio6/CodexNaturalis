package it.polimi.ingsw.model.board;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.card.Card;
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

    private List<Card> createTestCards(String relativePath){
        /* json as streams, so even after jar build it can retrieve the correct file */
        InputStream cardStream = this.getClass()
                .getResourceAsStream(relativePath);
        //todo: handle null streams
        Reader cardReader = new BufferedReader(new InputStreamReader(cardStream));

        Gson gson = new Gson();

        List<Card> cards = gson.fromJson(cardReader,
                new TypeToken<List<Card>>() {
                }.getType());

        return cards;
    }
}