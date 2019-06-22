package com.cardmein.drawroyale.game.web.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

/**
 * RESTful representation of a player's hand
 */
public class PlayerHandResource extends ResourceSupport {

    private Long objectId;
    private List<Card> cards = new ArrayList<Card>();

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
        return Collections.unmodifiableList(cards);
    }

    public void setCards(List<Card> cards) {
        this.cards.clear();
        this.cards.addAll(cards);
    }

}