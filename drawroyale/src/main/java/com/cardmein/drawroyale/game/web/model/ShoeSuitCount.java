package com.cardmein.drawroyale.game.web.model;

import com.cardmein.drawroyale.game.model.CardSuit;

public class ShoeSuitCount {

    private CardSuit suit;
    private int count;

    public CardSuit getSuit() {
        return suit;
    }

    public void setSuit(CardSuit suit) {
        this.suit = suit;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}