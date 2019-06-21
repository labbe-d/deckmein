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

    private static Long GAME_ID_SEQ = 1L;

    private List<Game> games = new ArrayList<>();

    public Game create(Game game) {
        if (game.getId() == null) {
            game.setId(GAME_ID_SEQ++);
        }

        games.add(game);

        return game;
    }

    public Game find(Long id) {
        return games.stream()
                .filter(g -> g.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void delete(Long id) {
        Game game = find(id);
        if (game != null) {
            games.remove(game);
        }
    }

}