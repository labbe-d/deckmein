package com.cardmein.drawroyale.game.service;

import java.util.List;
import java.util.function.ToIntFunction;

import com.cardmein.drawroyale.game.model.CardValue;
import com.cardmein.drawroyale.game.model.Deck;
import com.cardmein.drawroyale.game.model.Game;
import com.cardmein.drawroyale.game.model.GameCard;
import com.cardmein.drawroyale.game.model.GameCardState;
import com.cardmein.drawroyale.game.model.Player;
import com.cardmein.drawroyale.game.model.PlayerGame;
import com.cardmein.drawroyale.game.model.Shoe;
import com.cardmein.drawroyale.game.persistence.DeckRepository;
import com.cardmein.drawroyale.game.persistence.GameCardRepository;
import com.cardmein.drawroyale.game.persistence.GameRepository;
import com.cardmein.drawroyale.game.persistence.PlayerGameRepository;
import com.cardmein.drawroyale.game.persistence.PlayerRepository;
import com.cardmein.drawroyale.game.service.exception.DuplicateDeckException;
import com.cardmein.drawroyale.game.service.exception.DuplicatePlayerException;
import com.cardmein.drawroyale.game.service.exception.EmptyShoeException;
import com.cardmein.drawroyale.game.service.exception.PlayerNotInGameException;
import com.cardmein.drawroyale.game.service.shuffle.ObjectShuffler;

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

    @Autowired
    private GameCardRepository gameCardRepository;

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

        // Add all cards into Shoe
        deck.getCards().forEach(c -> {
            GameCard gameCard = new GameCard();
            gameCard.setCard(c);
            gameCard.setState(GameCardState.IN_SHOE);

            gameCard = gameCardRepository.create(gameCard);

            game.getShoe().addCard(gameCard);
        });
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

    public Game shuffleShoe(Long gameId) {
        Game game = gameRepository.find(gameId);

        ObjectShuffler<GameCard> cardShuffler = new ObjectShuffler<>();
        game.getShoe().replaceCards(cardShuffler.shuffle(game.getShoe().getCards()));

        return game;
    }

    public PlayerGame drawPlayerCard(Long playerId) throws EmptyShoeException {
        PlayerGame playerGame = playerGameRepository.find(playerId);
        Shoe shoe = playerGame.getGame().getShoe();

        if (!shoe.hasCard()) {
            throw new EmptyShoeException();
        }

        GameCard gameCard = shoe.drawNextCard();
        gameCard.setOwner(playerGame);
        gameCard.setState(GameCardState.IN_PLAYER_HAND);
        playerGame.addCard(gameCard);

        return playerGame;
        
    }

    public ToIntFunction<GameCard> getCardScoringFunction(Long gameId) {
        // There are no game specific scoring rules for now
        return (c) -> {
            CardValue value = c.getCard().getValue();
            if (CardValue.TWO.equals(value)) {
                return 2;
            } else if (CardValue.THREE.equals(value)) {
                return 3;
            } else if (CardValue.FOUR.equals(value)) {
                return 4;
            } else if (CardValue.FIVE.equals(value)) {
                return 5;
            } else if (CardValue.SIX.equals(value)) {
                return 6;
            } else if (CardValue.SEVEN.equals(value)) {
                return 7;
            } else if (CardValue.EIGHT.equals(value)) {
                return 8;
            } else if (CardValue.NINE.equals(value)) {
                return 9;
            } else if (CardValue.TEN.equals(value)) {
                return 10;
            } else if (CardValue.JACK.equals(value)) {
                return 11;
            } else if (CardValue.QUEEN.equals(value)) {
                return 12;
            } else if (CardValue.KING.equals(value)) {
                return 13;
            } else if (CardValue.ACE.equals(value)) {
                return 1;
            }
            return 0;
        };
    }
}