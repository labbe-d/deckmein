package com.cardmein.drawroyale.game.web.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

public class GameResource extends ResourceSupport {

    private Long objectId;
    private String name;
    private List<GameDeckResource> decks = new ArrayList<GameDeckResource>();
    private List<PlayerGameResource> players = new ArrayList<PlayerGameResource>();
    private ShoeResource shoe;

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addDeck(GameDeckResource deck) {
        decks.add(deck);
    }

    public List<GameDeckResource> getDecks() {
        return Collections.unmodifiableList(decks);
    }

    public void addPlayer(PlayerGameResource player) {
        players.add(player);
    }

    public List<PlayerGameResource> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public ShoeResource getShoe() {
        return shoe;
    }

    public void setShoe(ShoeResource shoe) {
        this.shoe = shoe;
    }

}