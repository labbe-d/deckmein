package com.cardmein.drawroyale.game.service;

import com.cardmein.drawroyale.game.model.Player;
import com.cardmein.drawroyale.game.persistence.PlayerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Manages players.
 */
@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public Long createPlayer(String name) {
        Player newPlayer = new Player(name);

        newPlayer = playerRepository.create(newPlayer);
        return newPlayer.getId();
    }

    public Player getPlayer(Long playerId) {
        return playerRepository.find(playerId);
    }

}