package com.cardmein.drawroyale.game.service;

import com.cardmein.drawroyale.game.model.Game;
import com.cardmein.drawroyale.game.persistence.GameRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    public Long createGame() {
        Game newGame = new Game();
        newGame = gameRepository.create(newGame);
        return newGame.getId();
    }

    public Game getGame(Long gameId) {
        return gameRepository.find(gameId);
    }

    public void deleteGame(Long gameId) {
        gameRepository.delete(gameId);
    }

}