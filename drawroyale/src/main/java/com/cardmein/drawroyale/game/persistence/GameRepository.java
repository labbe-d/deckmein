package com.cardmein.drawroyale.game.persistence;

import java.util.ArrayList;
import java.util.List;

import com.cardmein.drawroyale.game.model.Game;

import org.springframework.stereotype.Service;

/**
 * Handles persistence of games in memory.
 */
@Service
public class GameRepository {

    private static Long GAME_ID_SEQ = 0L;

    private List<Game> games = new ArrayList<>();

    /**
     * Persists a game in memory
     * @param game Game to persist
     * @return Persisted game
     */
    public Game create(Game game) {
        if (game.getId() == null) {
            game.setId(++GAME_ID_SEQ);
        }

        games.add(game);

        return game;
    }

    /**
     * Find a game based on its unique indentifier
     * @param id Game ID to find
     * @return Game matching the ID 
     */
    public Game find(Long id) {
        return games.stream()
                .filter(g -> g.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Delete a game based on its unique identifier
     * @param id Game ID to delete
     */
    public void delete(Long id) {
        Game game = find(id);
        if (game != null) {
            games.remove(game);
        }
    }

}