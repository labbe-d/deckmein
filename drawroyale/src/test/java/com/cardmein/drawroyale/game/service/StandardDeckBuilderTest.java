package com.cardmein.drawroyale.game.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.List;

import com.cardmein.drawroyale.game.model.Card;
import com.cardmein.drawroyale.game.model.CardSuit;
import com.cardmein.drawroyale.game.model.CardValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StandardDeckBuilderTest {

    @Autowired
    private StandardDeckBuilder standardDeckBuilder;

    @Test
    public void generatedDeckContainsAllSuitsAndFaces() {
        List<Card> cardList = standardDeckBuilder.buildDeck();
        assertThat(cardList.size(), is(52));

        for (CardSuit suit : CardSuit.values()) {
            for (CardValue value : CardValue.values()) {
                boolean foundCard = cardList.stream().anyMatch(c -> c.getSuit().equals(suit) && c.getValue().equals(value));
                if (!foundCard) {
                    fail("Card with suit " + suit.name() + " and value " + value.name() + " not found in generated deck");
                }
            }
        }
    }
}