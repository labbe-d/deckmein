package com.cardmein.drawroyale.game.web.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

/**
 * RESTful representation of a deck of card
 */
public class DeckResource extends ResourceSupport {

    private Long objectId;
    private List<Card> cards = new ArrayList<>();

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public List<Card> getCards() {
        return cards;
    }
}