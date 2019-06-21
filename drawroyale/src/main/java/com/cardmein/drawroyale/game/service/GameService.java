package com.cardmein.drawroyale.game.service;

import java.util.List;

import com.cardmein.drawroyale.game.model.Deck;
import com.cardmein.drawroyale.game.model.Game;
import com.cardmein.drawroyale.game.model.Player;
import com.cardmein.drawroyale.game.model.PlayerGame;
import com.cardmein.drawroyale.game.persistence.DeckRepository;
import com.cardmein.drawroyale.game.persistence.GameRepository;
import com.cardmein.drawroyale.game.persistence.PlayerGameRepository;
import com.cardmein.drawroyale.game.persistence.PlayerRepository;
import com.cardmein.drawroyale.game.service.exception.DuplicateDeckException;
import com.cardmein.drawroyale.game.service.exception.DuplicatePlayerException;
import com.cardmein.drawroyale.game.service.exception.PlayerNotInGameException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerGameRepository playerGameRepository;

    public Long createGame(String name) {
        Game newGame = new Game(name);
        newGame = gameRepository.create(newGame);
        return newGame.getId();
    }

    public Game getGame(Long gameId) {
        return gameRepository.find(gameId);
    }

    public void deleteGame(Long gameId) {
        gameRepository.delete(gameId);
    }

    /**
     * Adds a deck into a game's shoe.
     * @param gameId Target game's id
     * @param deckId Deck's id to add
     * @throws DuplicateDeckException Thrown when a deck is already present in a game
     */
    public void addDeck(Long gameId, Long deckId) throws DuplicateDeckException {
        Game game = gameRepository.find(gameId);
        Deck deck = deckRepository.find(deckId);
        if (game.getDecks().stream().anyMatch(d -> d.getId().equals(deck.getId()))) {
            throw new DuplicateDeckException();
        }

        game.addDeck(deck);
    }

    public PlayerGame getPlayerGame(Long playerGameId) {
        return playerGameRepository.find(playerGameId);
    }

    public PlayerGame addPlayer(Long gameId, Long playerId) throws DuplicatePlayerException {
        PlayerGame playerGame = playerGameRepository.findByGameAndPlayer(gameId, playerId);
        if (playerGame != null) {
            throw new DuplicatePlayerException();
        }

        Game game = gameRepository.find(gameId);
        Player player = playerRepository.find(playerId);

        playerGame = new PlayerGame();
        playerGame.setGame(game);
        playerGame.setPlayer(player);
        playerGame = playerGameRepository.create(playerGame);

        return playerGame;

    }

    public void removePlayer(Long gameId, Long playerId) throws PlayerNotInGameException {
        PlayerGame playerGame = playerGameRepository.findByGameAndPlayer(gameId, playerId);
        if (playerGame == null) {
            throw new PlayerNotInGameException();
        }

        playerGameRepository.delete(playerGame.getId());

    }

    public List<PlayerGame> getAllPlayers(Long gameId) {
        return playerGameRepository.findAllByGame(gameId);
    }

}