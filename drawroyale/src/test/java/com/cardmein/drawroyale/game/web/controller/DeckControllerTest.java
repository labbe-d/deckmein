package com.cardmein.drawroyale.game.web.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import com.cardmein.drawroyale.game.model.Card;
import com.cardmein.drawroyale.game.model.Deck;
import com.cardmein.drawroyale.game.model.DeckType;
import com.cardmein.drawroyale.game.service.DeckService;
import com.cardmein.drawroyale.game.web.model.DeckCreateResource;
import com.cardmein.drawroyale.game.web.model.DeckResource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeckControllerTest {

    @LocalServerPort
    private int port;
    
    @Autowired
    private DeckService deckService;

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    @Test
    public void getExistingDeckContainsAllCards() {
        Long deckId = deckService.createDeck(DeckType.STANDARD);
        Deck deck = deckService.getDeck(deckId);

        ResponseEntity<DeckResource> response =
            restTemplate.exchange(createURLWithPort("/decks/" + deckId), HttpMethod.GET, null, DeckResource.class);

        DeckResource deckResource = response.getBody();

        validateResourceEqualsModel(deckResource, deck);
        
    }

    @Test
    public void postStandardDeck() {
        DeckCreateResource body = new DeckCreateResource();
        body.setDeckType(DeckType.STANDARD);
        HttpEntity<DeckCreateResource> entity = new HttpEntity<DeckCreateResource>(body, headers);

        ResponseEntity<DeckResource> response =
            restTemplate.exchange(createURLWithPort("/decks/"), HttpMethod.POST, entity, DeckResource.class);

        DeckResource deckResource = response.getBody();
        assertThat(deckResource, notNullValue());
        assertThat(deckResource.getObjectId(), notNullValue());

        Deck deck = deckService.getDeck(deckResource.getObjectId());

        validateResourceEqualsModel(deckResource, deck);

    }

    private void validateResourceEqualsModel(DeckResource deckResource, Deck deck) {
        assertThat(deckResource.getCards().size(), is(deck.getCards().size()));

        deck.getCards().forEach(c -> {
            if (deckResource.getCards().stream().noneMatch(d -> cardAndResourceEqual(c, d))) {
                fail("Returned deck does not contain card suit " + c.getSuit().name() + " value " + c.getValue().name());
            }
        });

    }

    private boolean cardAndResourceEqual(Card card, com.cardmein.drawroyale.game.web.model.Card cardResource) {
        return card.getId().equals(cardResource.getObjectId()) &&
                card.getSuit().equals(cardResource.getSuit()) &&
                card.getValue().equals(cardResource.getValue());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}