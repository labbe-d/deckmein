package com.cardmein.drawroyale.game.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

    private Long id;
    private String name;
    private List<Player> players = new ArrayList<>();

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

    public void addPlayer(Player player) {
        players.add(player);
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

}