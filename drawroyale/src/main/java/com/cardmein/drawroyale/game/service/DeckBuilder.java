package com.cardmein.drawroyale.game.service;

import java.util.List;

import com.cardmein.drawroyale.game.model.Card;

/**
 * Defines a way to create a deck based on specific rules.
 */
public interface DeckBuilder {

    /**
     * Creates a card deck based on a ruleset.
     */
    List<Card> buildDeck();
}