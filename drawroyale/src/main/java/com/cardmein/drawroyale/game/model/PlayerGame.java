package com.cardmein.drawroyale.game.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerGame {

    private Player player;
    private Game game;
    private List<Card> hand = new ArrayList<>();

    public void addCard(Card card) {
        hand.add(card);
    }

    public List<Card> getHand() {
        return Collections.unmodifiableList(hand);
    }


}