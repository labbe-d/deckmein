package com.cardmein.drawroyale.game.service;

import com.cardmein.drawroyale.game.model.Deck;
import com.cardmein.drawroyale.game.model.Game;
import com.cardmein.drawroyale.game.model.Player;
import com.cardmein.drawroyale.game.persistence.DeckRepository;
import com.cardmein.drawroyale.game.persistence.GameRepository;
import com.cardmein.drawroyale.game.persistence.PlayerRepository;
import com.cardmein.drawroyale.game.service.exception.DuplicateDeckException;
import com.cardmein.drawroyale.game.service.exception.DuplicatePlayerException;

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

    public void addPlayer(Long gameId, Long playerId) throws DuplicatePlayerException {
        Game game = gameRepository.find(gameId);
        Player player = playerRepository.find(playerId);
        if (game.getPlayers().stream().anyMatch(p -> p.getId().equals(player.getId()))) {
            throw new DuplicatePlayerException();
        }

        game.addPlayer(player);
    }

}