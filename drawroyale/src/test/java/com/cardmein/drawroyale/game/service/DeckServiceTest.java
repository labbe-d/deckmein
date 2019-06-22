package com.cardmein.drawroyale.game.service;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.List;

import com.cardmein.drawroyale.game.model.Card;
import com.cardmein.drawroyale.game.model.Deck;
import com.cardmein.drawroyale.game.model.DeckType;
import com.cardmein.drawroyale.game.persistence.DeckRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeckServiceTest {

    @Autowired
    private DeckService deckService;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private StandardDeckBuilder standardDeckBuilder;

    @Test
    public void createdDeckPersistedInRepository() {
        Long deckId = deckService.createDeck(DeckType.STANDARD);
        Deck deck = deckRepository.find(deckId);
        assertThat(deck, notNullValue());
        assertThat(deck.getId(), is(deckId));
    }

    @Test
    public void standardGeneratorParameter() {
        Long deckId = deckService.createDeck(DeckType.STANDARD);
        Deck deck = deckRepository.find(deckId);
        List<Card> cardList = standardDeckBuilder.buildDeck();

        assertThat(deck.getCards().size(), is(cardList.size()));

        cardList.forEach(c -> {
            if (deck.getCards().stream().noneMatch(d -> c.getSuit().equals(d.getSuit()) && c.getValue().equals(d.getValue()) )) {
                fail("Generated deck does not contain card suit " + c.getSuit().name() + " value " + c.getValue().name());
            }
        });

    }

    @Test
    public void createdDeckIsRetrievable() {
        Long deckId = deckService.createDeck(DeckType.STANDARD);
        Deck deck = deckService.getDeck(deckId);

        assertThat(deck, notNullValue());
        assertThat(deck.getCards().size(), is(52));
    }

}