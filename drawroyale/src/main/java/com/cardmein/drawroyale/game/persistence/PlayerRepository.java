package com.cardmein.drawroyale.game.persistence;

import java.util.ArrayList;
import java.util.List;

import com.cardmein.drawroyale.game.model.Player;

import org.springframework.stereotype.Service;

/**
 * Handles persistence of decks in memory.
 */
@Service
public class PlayerRepository {

    private static Long DECK_ID_SEQ = 0L;

    private List<Player> players = new ArrayList<>();

    public Player create(Player player) {
        if (player.getId() == null) {
            player.setId(++DECK_ID_SEQ);
        }

        players.add(player);

        return player;
    }

    public Player find(Long id) {
        return players.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

}