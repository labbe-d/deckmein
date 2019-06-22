package com.cardmein.drawroyale.game.web.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

/**
 * RESTful representation of a player game participation
 */
public class PlayerGameResource extends ResourceSupport {

    private Long objectId;
    private Long playerId;
    private String name;
    private String state;
    private List<Card> cards = new ArrayList<Card>();

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public List<Card> getHand() {
        return Collections.unmodifiableList(cards);
    }

}
