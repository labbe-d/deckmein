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

    private static Long DECK_ID_SEQ = 1L;

    private List<Deck> decks = new ArrayList<>();

    public Deck create(Deck deck) {
        if (deck.getId() == null) {
            deck.setId(DECK_ID_SEQ++);
        }

        decks.add(deck);

        return deck;
    }

    public Deck find(Long id) {
        return decks.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void delete(Long id) {
        Deck deck = find(id);
        if (deck != null) {
            decks.remove(deck);
        }
    }


}