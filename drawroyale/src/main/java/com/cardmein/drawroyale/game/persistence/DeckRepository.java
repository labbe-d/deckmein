package com.cardmein.drawroyale.game.persistence;

import java.util.ArrayList;
import java.util.List;

import com.cardmein.drawroyale.game.model.Deck;

import org.springframework.stereotype.Service;

/**
 * Handles persistence of decks in memory.
 */
@Service
public class DeckRepository {

    private static Long DECK_ID_SEQ = 0L;

    private List<Deck> decks = new ArrayList<>();

    /**
     * Persist a deck in memory
     * @param deck Deck to persist
     * @return Persisted deck
     */
    public Deck create(Deck deck) {
        if (deck.getId() == null) {
            deck.setId(++DECK_ID_SEQ);
        }

        decks.add(deck);

        return deck;
    }

    /**
     * Find a deck based on its unique identifier
     * @param id Deck ID to find
     * @return Deck matching the ID
     */
    public Deck find(Long id) {
        return decks.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

}