package com.cardmein.drawroyale.game.service;

import com.cardmein.drawroyale.game.model.Deck;
import com.cardmein.drawroyale.game.model.DeckType;
import com.cardmein.drawroyale.game.persistence.DeckRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Manages decks.
 */
@Service
public class DeckService {

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private StandardDeckBuilder standardDeckBuilder;

    public Long createDeck(DeckType deckType) {
        Deck newDeck = new Deck();

        DeckBuilder deckBuilder = null;
        if (DeckType.STANDARD.equals(deckType)) {
            deckBuilder = standardDeckBuilder;
        }

        newDeck.addAllCards(deckBuilder.buildDeck());

        newDeck = deckRepository.create(newDeck);
        return newDeck.getId();
    }

    public Deck getDeck(Long deckId) {
        return deckRepository.find(deckId);
    }

}