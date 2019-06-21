package com.cardmein.drawroyale.game.web.controller;

import com.cardmein.drawroyale.game.model.Deck;
import com.cardmein.drawroyale.game.model.Game;
import com.cardmein.drawroyale.game.model.Player;
import com.cardmein.drawroyale.game.service.DeckService;
import com.cardmein.drawroyale.game.service.GameService;
import com.cardmein.drawroyale.game.service.PlayerService;
import com.cardmein.drawroyale.game.service.exception.DuplicateDeckException;
import com.cardmein.drawroyale.game.service.exception.DuplicatePlayerException;
import com.cardmein.drawroyale.game.web.assembler.GameResourceAssembler;
import com.cardmein.drawroyale.game.web.controller.exception.DeckNotFoundException;
import com.cardmein.drawroyale.game.web.controller.exception.GameNotFoundException;
import com.cardmein.drawroyale.game.web.controller.exception.InvalidDeckException;
import com.cardmein.drawroyale.game.web.controller.exception.InvalidPlayerException;
import com.cardmein.drawroyale.game.web.controller.exception.PlayerNotFoundException;
import com.cardmein.drawroyale.game.web.model.GameAddDeckResource;
import com.cardmein.drawroyale.game.web.model.GameAddPlayerResource;
import com.cardmein.drawroyale.game.web.model.GameCreateResource;
import com.cardmein.drawroyale.game.web.model.GameResource;

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

    @GetMapping("/{gameId}")
    public GameResource getGame(@PathVariable Long gameId) {
        Game game = gameService.getGame(gameId);
        if (game == null) {
            throw new GameNotFoundException();
        }

        return gameResourceAssembler.convertToGameResource(game);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public GameResource createGame(@RequestBody GameCreateResource createResource) {
        Long gameId = gameService.createGame(createResource.getName());
        Game game = gameService.getGame(gameId);
        return gameResourceAssembler.convertToGameResource(game);
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

        return gameResourceAssembler.convertToGameResource(game);
    }

    @PostMapping(path = "/{gameId}/players", consumes = MediaType.APPLICATION_JSON_VALUE)
    public GameResource addDeck(@PathVariable Long gameId, @RequestBody GameAddPlayerResource addPlayerResource) {
        if (addPlayerResource == null || addPlayerResource.getObjectId() == null) {
            throw new InvalidPlayerException("No player provided");
        }

        Game game = gameService.getGame(gameId);
        if (game == null) {
            throw new GameNotFoundException();
        }

        Player player = playerService.getPlayer(addPlayerResource.getObjectId());
        if (player == null) {
            throw new PlayerNotFoundException();
        }

        try {
            gameService.addPlayer(game.getId(), player.getId());

        } catch (DuplicatePlayerException e) {
            throw new InvalidPlayerException("Player already exists in game");
        }

        return gameResourceAssembler.convertToGameResource(game);
    }

}

