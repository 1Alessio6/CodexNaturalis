package it.polimi.ingsw.model.Deck;

import it.polimi.ingsw.model.card.Back;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.EmptyDeckException;
import it.polimi.ingsw.model.card.Front;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions.*;

class DeckTest {
    private List<Card> createCardList(int numCards) {
        Card genericCard = new Card(new Front(), new Back());
        List<Card> genericCards = new ArrayList<>();
        for (int i = 0;  i < numCards; ++i) {
            genericCards.add(genericCard);
        }

        return genericCards;
    }

    @Test
    void draw() {
        int numCards = 1;
        List<Card> cards = createCardList(numCards);
        Deck<Card> deck = new Deck<>(cards);

        for (int i = 0; i < cards.size(); ++i) {
            int idx = i;
            Assertions.assertDoesNotThrow(() -> {
                Assertions.assertEquals(cards.get(idx), deck.draw());
            });
        }

        Assertions.assertThrows(EmptyDeckException.class, deck::draw);
    }

    @Test
    void add() {
        int numCards = 1;
        List<Card> cards = createCardList(numCards);
        Deck<Card> deck = new Deck<>(cards);
        Card card =  new Card(new Front(), new Back());

        deck.add(card);
        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertEquals(card, deck.draw());
        });
    }
}