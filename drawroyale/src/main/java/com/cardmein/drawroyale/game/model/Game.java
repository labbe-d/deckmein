package com.cardmein.drawroyale.game.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

    private Long id;
    private String name;
    private List<Deck> decks = new ArrayList<>();
    private Shoe shoe = new Shoe();

    public Game(String name) {
        this.name = name;
    }

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

    public void addDeck(Deck deck) {
        decks.add(deck);
    }

    public List<Deck> getDecks() {
        return Collections.unmodifiableList(decks);
    }

    public Shoe getShoe() {
        return shoe;
    }

}