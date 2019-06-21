package com.cardmein.drawroyale.game.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {

    private Long id;
    private String name;
    private List<Card> hand = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public List<Card> getHand() {
        return Collections.unmodifiableList(hand);
    }

}