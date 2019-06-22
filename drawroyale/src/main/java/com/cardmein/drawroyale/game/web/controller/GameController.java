package com.cardmein.drawroyale.game.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

import com.cardmein.drawroyale.game.model.CardSuit;
import com.cardmein.drawroyale.game.model.CardValue;
import com.cardmein.drawroyale.game.model.Deck;
import com.cardmein.drawroyale.game.model.Game;
import com.cardmein.drawroyale.game.model.GameCard;
import com.cardmein.drawroyale.game.model.Player;
import com.cardmein.drawroyale.game.model.PlayerGame;
import com.cardmein.drawroyale.game.model.PlayerGameState;
import com.cardmein.drawroyale.game.model.Shoe;
import com.cardmein.drawroyale.game.model.ShoeState;
import com.cardmein.drawroyale.game.service.DeckService;
import com.cardmein.drawroyale.game.service.GameService;
import com.cardmein.drawroyale.game.service.PlayerService;
import com.cardmein.drawroyale.game.service.exception.DuplicateDeckException;
import com.cardmein.drawroyale.game.service.exception.DuplicatePlayerException;
import com.cardmein.drawroyale.game.service.exception.EmptyShoeException;
import com.cardmein.drawroyale.game.service.exception.PlayerNotInGameException;
import com.cardmein.drawroyale.game.web.assembler.GameLeaderboardResourceAssembler;
import com.cardmein.drawroyale.game.web.assembler.GameResourceAssembler;
import com.cardmein.drawroyale.game.web.assembler.PlayerGameResourceAssembler;
import com.cardmein.drawroyale.game.web.assembler.ShoeResourceAssembler;
import com.cardmein.drawroyale.game.web.controller.exception.DeckNotFoundException;
import com.cardmein.drawroyale.game.web.controller.exception.GameNotFoundException;
import com.cardmein.drawroyale.game.web.controller.exception.InvalidDeckException;
import com.cardmein.drawroyale.game.web.controller.exception.InvalidPlayerException;
import com.cardmein.drawroyale.game.web.controller.exception.InvalidShoeStateException;
import com.cardmein.drawroyale.game.web.controller.exception.PlayerNotFoundException;
import com.cardmein.drawroyale.game.web.model.Card;
import com.cardmein.drawroyale.game.web.model.GameAddDeckResource;
import com.cardmein.drawroyale.game.web.model.GameAddPlayerResource;
import com.cardmein.drawroyale.game.web.model.GameCreateResource;
import com.cardmein.drawroyale.game.web.model.GameLeaderboardResource;
import com.cardmein.drawroyale.game.web.model.GameResource;
import com.cardmein.drawroyale.game.web.model.PlayerGameResource;
import com.cardmein.drawroyale.game.web.model.ShoeCardCount;
import com.cardmein.drawroyale.game.web.model.ShoeResource;
import com.cardmein.drawroyale.game.web.model.ShoeSuitCount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles all REST endpoints related to a game and gameplay
 */
@RestController
@RequestMapping(path = "games", produces = MediaType.APPLICATION_JSON_VALUE)
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private DeckService deckService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameResourceAssembler gameResourceAssembler;

    @Autowired
    private PlayerGameResourceAssembler playerGameAssembler;

    @Autowired
    private ShoeResourceAssembler shoeAssembler;

    @Autowired
    private GameLeaderboardResourceAssembler leaderboardAssembler;

    /**
     * Retreive the state of a game based on its unique identifier
     * @param gameId Game ID
     * @return State of the game
     */
    @GetMapping("/{gameId}")
    public GameResource getGame(@PathVariable Long gameId) {
        Game game = gameService.getGame(gameId);
        if (game == null) {
            throw new GameNotFoundException();
        }

        List<PlayerGame> playerGames = gameService.getAllPlayers(gameId);
        return gameResourceAssembler.convertToGameResource(game, playerGames);
    }

    /**
     * Create a new game
     * @param createResource Initial game state
     * @return State of the created game
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public GameResource createGame(@RequestBody GameCreateResource createResource) {
        Long gameId = gameService.createGame(createResource.getName());
        Game game = gameService.getGame(gameId);

        List<PlayerGame> playerGames = gameService.getAllPlayers(gameId);
        return gameResourceAssembler.convertToGameResource(game, playerGames);
    }

    /**
     * Delete a game based on its unique identifier
     * @param gameId Game ID
     */
    @DeleteMapping("/{gameId}")
    public void deleteGame(@PathVariable Long gameId) {
        Game game = gameService.getGame(gameId);
        if (game == null) {
            throw new GameNotFoundException();
        }

        gameService.deleteGame(gameId);

    }

    /**
     * Add a deck to a game
     * @param gameId Game ID
     * @param addDeckResource Deck information to add to the game
     * @return State of the game with the new deck
     */
    @PostMapping(path = "/{gameId}/decks", consumes = MediaType.APPLICATION_JSON_VALUE)
    public GameResource addDeck(@PathVariable Long gameId, @RequestBody GameAddDeckResource addDeckResource) {
        if (addDeckResource == null || addDeckResource.getObjectId() == null) {
            throw new InvalidDeckException("No deck provided");
        }

        Game game = gameService.getGame(gameId);
        if (game == null) {
            throw new GameNotFoundException();
        }

        Deck deck = deckService.getDeck(addDeckResource.getObjectId());
        if (deck == null) {
            throw new DeckNotFoundException();
        }

        try {
            gameService.addDeck(game.getId(), deck.getId());

        } catch (DuplicateDeckException e) {
            throw new InvalidDeckException("Deck already exists in game");
        }

        List<PlayerGame> playerGames = gameService.getAllPlayers(gameId);
        return gameResourceAssembler.convertToGameResource(game, playerGames);
    }

    /**
     * Add a player game participation to a game
     * @param gameId Game ID
     * @param addPlayerResource Player information to add to the game
     * @return State of the game with the added player
     */
    @PostMapping(path = "/{gameId}/players", consumes = MediaType.APPLICATION_JSON_VALUE)
    public PlayerGameResource addPlayer(@PathVariable Long gameId, @RequestBody GameAddPlayerResource addPlayerResource) {
        if (addPlayerResource == null || addPlayerResource.getPlayerId() == null) {
            throw new InvalidPlayerException("No player provided");
        }

        Game game = gameService.getGame(gameId);
        if (game == null) {
            throw new GameNotFoundException();
        }

        Player player = playerService.getPlayer(addPlayerResource.getPlayerId());
        if (player == null) {
            throw new PlayerNotFoundException();
        }

        try {
            PlayerGame playerGame = gameService.addPlayer(game.getId(), player.getId());
            return playerGameAssembler.convertToPlayerResource(playerGame);

        } catch (DuplicatePlayerException e) {
            throw new InvalidPlayerException("Player already exists in game");
        }

    }

    /**
     * Remove a player participation from a game based on their unique identifiers
     * @param gameId Game ID
     * @param playerGameId Player game participation ID
     * @return State of the game with the player removed
     */
    @DeleteMapping(path = "/{gameId}/players/{playerGameId}")
    public GameResource removePlayer(@PathVariable Long gameId, @PathVariable Long playerGameId) {
        PlayerGame playerGame = gameService.getPlayerGame(playerGameId);
        if (playerGame == null) {
            throw new PlayerNotFoundException();
        }

        if (!playerGame.getGame().getId().equals(gameId)) {
            throw new GameNotFoundException();
        }

        try {
            gameService.removePlayer(playerGame.getGame().getId(), playerGame.getPlayer().getId());

        } catch (PlayerNotInGameException e) {
            throw new PlayerNotFoundException();
        }

        Game game = gameService.getGame(gameId);
        List<PlayerGame> playerGames = gameService.getAllPlayers(gameId);
        return gameResourceAssembler.convertToGameResource(game, playerGames);

    }

    /**
     * Change the player game participation state to initiate actions
     * @param gameId Game ID
     * @param playerGameId Player game participation ID
     * @param newState State corresponding to the action to take
     * @return State of the player game participation after executing the action
     */
    @PostMapping(path = "/{gameId}/players/{playerGameId}/state", consumes = MediaType.TEXT_PLAIN_VALUE)
    public PlayerGameResource changePlayerState(@PathVariable Long gameId, @PathVariable Long playerGameId, @RequestBody String newState) {
        if (!PlayerGameState.DRAWING_CARD.name().equals(newState)) {
            throw new InvalidShoeStateException("Invalid player state: " + newState);
        }

        PlayerGame playerGame = gameService.getPlayerGame(playerGameId);
        if (playerGame == null) {
            throw new PlayerNotFoundException();
        }

        if (!playerGame.getGame().getId().equals(gameId)) {
            throw new GameNotFoundException();
        }

        try {
            playerGame = gameService.drawPlayerCard(playerGameId);

        } catch (EmptyShoeException e) {
            throw new InvalidShoeStateException("Unable to draw from empty shoe");
        }

        return playerGameAssembler.convertToPlayerResource(playerGame);
    }

    /**
     * Retreive the hand of a player in a game
     * @param gameId Game ID
     * @param playerGameId Player ID
     * @return State of the player hand in the game
     */
    @GetMapping(path = "/{gameId}/players/{playerGameId}/hand")
    public List<Card> getPlayerCards(@PathVariable Long gameId, @PathVariable Long playerGameId) {
        PlayerGame playerGame = gameService.getPlayerGame(playerGameId);
        if (playerGame == null) {
            throw new PlayerNotFoundException();
        }

        if (!playerGame.getGame().getId().equals(gameId)) {
            throw new GameNotFoundException();
        }

        List<Card> cardList = new ArrayList<>();

        playerGame.getHand().forEach(c -> {
            Card card = new Card();
            card.setObjectId(c.getId());
            card.setSuit(c.getCard().getSuit());
            card.setValue(c.getCard().getValue());
            cardList.add(card);
        });

        return cardList;
    }
    
    /**
     * Change the game shoe state to initiate actions
     * @param gameId Game ID
     * @param newState State corresponding to the action to take
     * @return State of the shoe after executing the action
     */
    @PostMapping(path = "/{gameId}/shoe/state", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ShoeResource changeShoeState(@PathVariable Long gameId, @RequestBody String newState) {
        if (!ShoeState.SHUFFLING.name().equals(newState)) {
            throw new InvalidShoeStateException("Invalid shoe state: " + newState);
        }

        Game game = gameService.getGame(gameId);
        if (game == null) {
            throw new GameNotFoundException();
        }

        game = gameService.shuffleShoe(gameId);

        return shoeAssembler.convertToShoeResource(game, game.getShoe());
    }

    /**
     * Retreive the leaderboard of the game sorted by descending hand score
     * @param gameId Game ID
     * @return State of the game's leaderboard
     */
    @GetMapping(path = "/{gameId}/leaderboard")
    public GameLeaderboardResource getLeaderboard(@PathVariable Long gameId) {
        Game game = gameService.getGame(gameId);
        if (game == null) {
            throw new GameNotFoundException();
        }

        List<PlayerGame> playerGames = gameService.getAllPlayers(gameId);

        ToIntFunction<GameCard> scoringFn = gameService.getCardScoringFunction(gameId);

        return leaderboardAssembler.convertToGameResource(game, playerGames, scoringFn);

    }

    /**
     * Retrieve remaining card count based on suits
     * @param gameId Game ID
     * @return Remaining cards per suit
     */
    @GetMapping(path = "/{gameId}/shoe/stats/suits")
    public List<ShoeSuitCount> getShoeSuitsStats(@PathVariable Long gameId) {
        Game game = gameService.getGame(gameId);
        if (game == null) {
            throw new GameNotFoundException();
        }

        Shoe shoe = game.getShoe();
        List<ShoeSuitCount> suitCounts = new ArrayList<>();

        for (CardSuit suit : CardSuit.values()) {
            ShoeSuitCount suitCount = new ShoeSuitCount();
            suitCount.setSuit(suit);
            long count = shoe.getCards().stream()
                    .filter(c -> c.getCard().getSuit().equals(suit))
                    .count();
            suitCount.setCount((int)count);

            suitCounts.add(suitCount);
        }

        return suitCounts;

    }

    /**
     * Retreive remaining card count based on suit and face value
     * @param gameId Game ID
     * @return Remaining cards per suit and face value
     */
    @GetMapping(path = "/{gameId}/shoe/stats/cards")
    public List<ShoeCardCount> getShoeCardsStats(@PathVariable Long gameId) {
        Game game = gameService.getGame(gameId);
        if (game == null) {
            throw new GameNotFoundException();
        }

        Shoe shoe = game.getShoe();
        List<ShoeCardCount> cardCounts = new ArrayList<>();

        for (CardSuit suit : CardSuit.values()) {
            for (CardValue value : CardValue.values()) {
                long count = shoe.getCards().stream()
                        .filter(c -> c.getCard().getSuit().equals(suit) && c.getCard().getValue().equals(value))
                        .count();

                if (count > 0) {
                    ShoeCardCount cardCount = new ShoeCardCount();
                    cardCount.setSuit(suit);
                    cardCount.setValue(value);
                    cardCount.setCount((int)count);
    
                    cardCounts.add(cardCount);
    
                }
            }
        }

        cardCounts.sort((c1, c2) -> {
            int c1Suit = getShoeCardStatSuitOrderValue(c1.getSuit());
            int c2Suit = getShoeCardStatSuitOrderValue(c2.getSuit());
            if (c1Suit != c2Suit) {
                return Integer.compare(c1Suit, c2Suit);
            }

            int c1Value = getShoeCardStatValueOrderValue(c1.getValue());
            int c2Value = getShoeCardStatValueOrderValue(c2.getValue());
            return Integer.compare(c2Value, c1Value);
        });

        return cardCounts;

    }

    private int getShoeCardStatSuitOrderValue(CardSuit suit) {
        if (CardSuit.HEART.equals(suit)) {
            return 1;
        } else if (CardSuit.SPADE.equals(suit)) {
            return 2;
        } else if (CardSuit.CLUB.equals(suit)) {
            return 3;
        } else if (CardSuit.DIAMOND.equals(suit)) {
            return 4;
        }
        return 5;
    }

    private int getShoeCardStatValueOrderValue(CardValue value) {
        if (CardValue.ACE.equals(value)) {
            return 1;
        } else if (CardValue.TWO.equals(value)) {
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
        }
        return 14;
    }

}

