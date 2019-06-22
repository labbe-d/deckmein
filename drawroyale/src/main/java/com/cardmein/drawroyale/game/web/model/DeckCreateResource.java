package com.cardmein.drawroyale.game.web.model;

import com.cardmein.drawroyale.game.model.DeckType;

/**
 * Configuration options used when creating a new deck
 */
public class DeckCreateResource {

    private DeckType deckType;

    public DeckType getDeckType() {
        return deckType;
    }

}