package com.cardmein.drawroyale.game.model;

public class Card {

    private static Long CARD_ID_SEQ = 0L;

    private Long id;
    private CardSuit suit;
    private CardValue value;

    public Card(CardSuit suit, CardValue value) {
        this.suit = suit;
        this.value = value;
        id = ++CARD_ID_SEQ;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CardSuit getSuit() {
        return suit;
    }

    public CardValue getValue() {
        return value;
    }

}