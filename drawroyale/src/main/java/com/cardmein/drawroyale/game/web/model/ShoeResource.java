package com.cardmein.drawroyale.game.web.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

public class ShoeResource extends ResourceSupport {

    private String state;
    private List<Card> cards = new ArrayList<Card>();

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

}
