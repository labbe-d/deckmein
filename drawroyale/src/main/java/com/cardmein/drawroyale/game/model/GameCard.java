package com.cardmein.drawroyale.game.model;

/**
 * Enhanced version of a Card that contains game state and is unique to a game.
 */
public class GameCard {

    private Long id;
    private Card card;
    private PlayerGame owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public PlayerGame getOwner() {
        return owner;
    }

    public void setOwner(PlayerGame owner) {
        this.owner = owner;
    }
}