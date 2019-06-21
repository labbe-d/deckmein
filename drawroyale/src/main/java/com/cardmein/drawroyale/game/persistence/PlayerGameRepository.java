package com.cardmein.drawroyale.game.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.cardmein.drawroyale.game.model.PlayerGame;

import org.springframework.stereotype.Service;

/**
 * Handles persistence of player games in memory.
 */
@Service
public class PlayerGameRepository {

    private static Long PLAYER_GAME_ID_SEQ = 0L;

    private List<PlayerGame> playerGames = new ArrayList<>();

    public PlayerGame create(PlayerGame playerGame) {
        if (playerGame.getId() == null) {
            playerGame.setId(++PLAYER_GAME_ID_SEQ);
        }

        playerGames.add(playerGame);

        return playerGame;
    }

    public PlayerGame find(Long id) {
        return playerGames.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public PlayerGame findByGameAndPlayer(Long gameId, Long playerId) {
        return playerGames.stream()
                .filter(p -> p.getGame().getId().equals(gameId) && p.getPlayer().getId().equals(playerId))
                .findFirst()
                .orElse(null);
    }

    public List<PlayerGame> findAllByGame(Long gameId) {
        return playerGames.stream()
                .filter(p -> p.getGame().getId().equals(gameId))
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        playerGames.remove(find(id));
    }

}
