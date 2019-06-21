package com.cardmein.drawroyale.game.web.controller;

import com.cardmein.drawroyale.game.model.Deck;
import com.cardmein.drawroyale.game.model.Game;
import com.cardmein.drawroyale.game.service.DeckService;
import com.cardmein.drawroyale.game.service.GameService;
import com.cardmein.drawroyale.game.web.controller.exception.DeckNotFoundException;
import com.cardmein.drawroyale.game.web.controller.exception.GameNotFoundException;
import com.cardmein.drawroyale.game.web.model.GameResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "game", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private DeckService deckService;

    @PostMapping
    public GameResource createGame() {
        Long gameId = gameService.createGame();
        Game game = gameService.getGame(gameId);
        return convertToGameResource(game);
    }

    @DeleteMapping("/{gameId}")
    public void deleteGame(@PathVariable Long gameId) {
        Game game = gameService.getGame(gameId);
        if (game == null) {
            throw new GameNotFoundException();
        }

        gameService.deleteGame(gameId);

    }

    @PostMapping("/{gameId}/decks/{deckId}")
    public GameResource addDeck(@PathVariable Long gameId, @PathVariable Long deckId) {
        Game game = gameService.getGame(gameId);
        if (game == null) {
            throw new GameNotFoundException();
        }

        Deck deck = deckService.getDeck(deckId);
        if (deck == null) {
            throw new DeckNotFoundException();
        }

        return null;
    }

    private GameResource convertToGameResource(Game game) {
        return null;
    }
}

