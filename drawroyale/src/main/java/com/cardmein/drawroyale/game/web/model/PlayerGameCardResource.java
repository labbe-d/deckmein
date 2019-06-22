package com.cardmein.drawroyale.game.web.model;

import com.cardmein.drawroyale.game.model.CardSuit;
import com.cardmein.drawroyale.game.model.CardValue;

import org.springframework.hateoas.ResourceSupport;

/**
 * RESTful representation of a card in a player hand
 */
public class PlayerGameCardResource extends ResourceSupport {

    private Long objectId;
    private CardSuit suit;
    private CardValue value;

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public CardSuit getSuit() {
        return suit;
    }

    public void setSuit(CardSuit suit) {
        this.suit = suit;
    }

    public CardValue getValue() {
        return value;
    }

    public void setValue(CardValue value) {
        this.value = value;
    }
}
