package com.cardmein.drawroyale.game.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerGame {

    private Long id;
    private Player player;
    private Game game;
    private List<Card> hand = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public List<Card> getHand() {
        return Collections.unmodifiableList(hand);
    }


}