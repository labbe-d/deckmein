package com.cardmein.drawroyale.game.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Deck {

    private Long id;
    private List<Card> cards = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addAllCards(Collection<Card> cards) {
        this.cards.addAll(cards);
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

}