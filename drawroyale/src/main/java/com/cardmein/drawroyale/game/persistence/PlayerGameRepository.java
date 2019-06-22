package com.cardmein.drawroyale.game.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.cardmein.drawroyale.game.model.PlayerGame;

import org.springframework.stereotype.Service;

/**
 * Handles persistence of player game participations in memory.
 */
@Service
public class PlayerGameRepository {

    private static Long PLAYER_GAME_ID_SEQ = 0L;

    private List<PlayerGame> playerGames = new ArrayList<>();

    /**
     * Persist a player game participation in memory
     * @param playerGame Player game participation to persist
     * @return Persisted player game participation
     */
    public PlayerGame create(PlayerGame playerGame) {
        if (playerGame.getId() == null) {
            playerGame.setId(++PLAYER_GAME_ID_SEQ);
        }

        playerGames.add(playerGame);

        return playerGame;
    }

    /**
     * Find a player game participation based on its unique identifier
     * @param id Player game participation ID
     * @return Player game participation matching the ID
     */
    public PlayerGame find(Long id) {
        return playerGames.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Find a player game participation based on a game ID and a player ID
     * @param gameId Game ID
     * @param playerId Player ID
     * @return Player game participation matching both IDs
     */
    public PlayerGame findByGameAndPlayer(Long gameId, Long playerId) {
        return playerGames.stream()
                .filter(p -> p.getGame().getId().equals(gameId) && p.getPlayer().getId().equals(playerId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Find all player game participations of a given game ID
     * @param gameId Game ID
     * @return All players game participations matching the game ID
     */
    public List<PlayerGame> findAllByGame(Long gameId) {
        return playerGames.stream()
                .filter(p -> p.getGame().getId().equals(gameId))
                .collect(Collectors.toList());
    }

    /**
     * Delete a player game participation based on its unique identifier
     * @param id Player game participation ID
     */
    public void delete(Long id) {
        playerGames.remove(find(id));
    }

}
