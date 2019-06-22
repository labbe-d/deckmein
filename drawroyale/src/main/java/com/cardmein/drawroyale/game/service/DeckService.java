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

    /**
     * Create a new deck based on a pattern type
     * @param deckType Pattern type to use
     * @return New deck with cards based on the pattern type
     */
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

    /**
     * Retreive a deck based on its unique identifier
     * @param deckId Deck ID
     * @return Deck matching the ID
     */
    public Deck getDeck(Long deckId) {
        return deckRepository.find(deckId);
    }

}