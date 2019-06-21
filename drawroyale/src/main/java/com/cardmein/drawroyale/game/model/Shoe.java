package com.cardmein.drawroyale.game.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Shoe {

    private List<GameCard> cards = new ArrayList<GameCard>();
    private ShoeState state = ShoeState.READY;

    public void addCard(GameCard card) {
        this.cards.add(card);
    }

    public List<GameCard> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public void replaceCards(List<GameCard> cards) {
        this.cards.clear();
        cards.forEach(this::addCard);
    }

    public ShoeState getState() {
        return state;
    }

    public void setState(ShoeState state) {
        this.state = state;
    }

    public boolean hasCard() {
        return cards.size() > 0;
    }

    public GameCard drawNextCard() {
        GameCard card = cards.get(0);
        cards.remove(card);
        return card;
    }
}