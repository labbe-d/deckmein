package com.cardmein.drawroyale.game.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Registration of a Player in a given game. Contains data related to the game state.
 */
public class PlayerGame {

    private Long id;
    private PlayerGameState state = PlayerGameState.READY;
    private Player player;
    private Game game;
    private List<GameCard> hand = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlayerGameState getState() {
        return state;
    }

    public void setState(PlayerGameState state) {
        this.state = state;
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

    public void addCard(GameCard card) {
        hand.add(card);
    }

    public List<GameCard> getHand() {
        return Collections.unmodifiableList(hand);
    }


}