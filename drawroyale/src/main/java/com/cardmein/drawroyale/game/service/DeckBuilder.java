package com.cardmein.drawroyale.game.service;

import java.util.List;

import com.cardmein.drawroyale.game.model.Card;

public interface DeckBuilder {

    /**
     * Creates a card deck based on a ruleset.
     */
    List<Card> buildDeck();
}