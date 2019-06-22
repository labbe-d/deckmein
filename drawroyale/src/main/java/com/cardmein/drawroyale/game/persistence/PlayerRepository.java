package com.cardmein.drawroyale.game.persistence;

import java.util.ArrayList;
import java.util.List;

import com.cardmein.drawroyale.game.model.Player;

import org.springframework.stereotype.Service;

/**
 * Handles persistence of players in memory.
 */
@Service
public class PlayerRepository {

    private static Long PLAYER_ID_SEQ = 0L;

    private List<Player> players = new ArrayList<>();

    /**
     * Persist a player in memoty
     * @param player Player to persist
     * @return Persisted player
     */
    public Player create(Player player) {
        if (player.getId() == null) {
            player.setId(++PLAYER_ID_SEQ);
        }

        players.add(player);

        return player;
    }

    /**
     * Find a player based on its unique identifier
     * @param id Player ID
     * @return Player matching the ID
     */
    public Player find(Long id) {
        return players.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

}