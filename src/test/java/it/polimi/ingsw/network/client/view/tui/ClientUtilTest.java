package it.polimi.ingsw.network.client.view.tui;

import com.google.gson.Gson;
import it.polimi.ingsw.jsondeserializer.InterfaceAdaptor;
import it.polimi.ingsw.model.board.Playground;
import it.polimi.ingsw.model.board.Position;
import it.polimi.ingsw.model.card.Back;
import it.polimi.ingsw.model.card.Face;
import it.polimi.ingsw.model.card.strategies.CalculatePoints;
import it.polimi.ingsw.network.client.model.board.ClientPlayground;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.google.gson.GsonBuilder;

class ClientUtilTest {
    @Test
    void printPlaygroundWithCardContainingNotAvailablePos_doesNotThrow() {
        Gson gson;
        GsonBuilder builder = new GsonBuilder().enableComplexMapKeySerialization();
        builder.registerTypeAdapter(CalculatePoints.class, new InterfaceAdaptor<>());
        gson = builder.create();

        String starterJson =
                """
                        {"resources":{"FUNGI":1,"ANIMAL":1,"PLANT":1},"calculator":{"concreteType":"it.polimi.ingsw.model.card.strategies.CalculateBackPoints","attributes":{}},"corners":{"TOP_RIGHT":{"isCovered":false},"TOP_LEFT":{"isCovered":false}}}
                        """;
        Face starterBack = gson.fromJson(starterJson, Back.class);
        Playground playground = new Playground();
        Assertions.assertDoesNotThrow(() -> {
            playground.placeCard(starterBack, new Position(0, 0));
        });
        ClientPlayground clientPlayground = new ClientPlayground(playground);
        Assertions.assertDoesNotThrow(() -> {
            ClientUtil.printPlayground(clientPlayground, new Position(0,0));
        });
    }

}