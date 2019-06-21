package com.cardmein.drawroyale.game.model;

public class GameCard {

    private Long id;
    private Card card;
    private GameCardState state;
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

    public GameCardState getState() {
        return state;
    }

    public void setState(GameCardState state) {
        this.state = state;
    }

    public PlayerGame getOwner() {
        return owner;
    }

    public void setOwner(PlayerGame owner) {
        this.owner = owner;
    }
}