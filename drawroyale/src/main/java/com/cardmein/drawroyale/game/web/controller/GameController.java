package com.cardmein.drawroyale.game.web.controller;

import java.util.List;

import com.cardmein.drawroyale.game.model.Deck;
import com.cardmein.drawroyale.game.model.Game;
import com.cardmein.drawroyale.game.model.Player;
import com.cardmein.drawroyale.game.model.PlayerGame;
import com.cardmein.drawroyale.game.model.PlayerGameState;
import com.cardmein.drawroyale.game.model.ShoeState;
import com.cardmein.drawroyale.game.service.DeckService;
import com.cardmein.drawroyale.game.service.GameService;
import com.cardmein.drawroyale.game.service.PlayerService;
import com.cardmein.drawroyale.game.service.exception.DuplicateDeckException;
import com.cardmein.drawroyale.game.service.exception.DuplicatePlayerException;
import com.cardmein.drawroyale.game.service.exception.EmptyShoeException;
import com.cardmein.drawroyale.game.service.exception.PlayerNotInGameException;
import com.cardmein.drawroyale.game.web.assembler.GameResourceAssembler;
import com.cardmein.drawroyale.game.web.assembler.PlayerGameResourceAssembler;
import com.cardmein.drawroyale.game.web.assembler.ShoeResourceAssembler;
import com.cardmein.drawroyale.game.web.controller.exception.DeckNotFoundException;
import com.cardmein.drawroyale.game.web.controller.exception.GameNotFoundException;
import com.cardmein.drawroyale.game.web.controller.exception.InvalidDeckException;
import com.cardmein.drawroyale.game.web.controller.exception.InvalidPlayerException;
import com.cardmein.drawroyale.game.web.controller.exception.InvalidShoeStateException;
import com.cardmein.drawroyale.game.web.controller.exception.PlayerNotFoundException;
import com.cardmein.drawroyale.game.web.model.GameAddDeckResource;
import com.cardmein.drawroyale.game.web.model.GameAddPlayerResource;
import com.cardmein.drawroyale.game.web.model.GameCreateResource;
import com.cardmein.drawroyale.game.web.model.GameResource;
import com.cardmein.drawroyale.game.web.model.PlayerGameResource;
import com.cardmein.drawroyale.game.web.model.ShoeResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{gameId}")
    public GameResource getGame(@PathVariable Long gameId) {
        Game game = gameService.getGame(gameId);
        if (game == null) {
            throw new GameNotFoundException();
        }

        List<PlayerGame> playerGames = gameService.getAllPlayers(gameId);
        return gameResourceAssembler.convertToGameResource(game, playerGames);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public GameResource createGame(@RequestBody GameCreateResource createResource) {
        Long gameId = gameService.createGame(createResource.getName());
        Game game = gameService.getGame(gameId);

        List<PlayerGame> playerGames = gameService.getAllPlayers(gameId);
        return gameResourceAssembler.convertToGameResource(game, playerGames);
    }

    @DeleteMapping("/{gameId}")
    public void deleteGame(@PathVariable Long gameId) {
        Game game = gameService.getGame(gameId);
        if (game == null) {
            throw new GameNotFoundException();
        }

        gameService.deleteGame(gameId);

    }

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

}

