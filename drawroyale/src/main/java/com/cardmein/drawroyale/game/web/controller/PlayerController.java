package com.cardmein.drawroyale.game.web.controller;

import com.cardmein.drawroyale.game.model.Player;
import com.cardmein.drawroyale.game.service.PlayerService;
import com.cardmein.drawroyale.game.web.assembler.PlayerResourceAssembler;
import com.cardmein.drawroyale.game.web.controller.exception.DeckNotFoundException;
import com.cardmein.drawroyale.game.web.model.PlayerCreateResource;
import com.cardmein.drawroyale.game.web.model.PlayerResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles all REST endpoints related to a player
 */
@RestController
@RequestMapping(path = "players", produces = MediaType.APPLICATION_JSON_VALUE)
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerResourceAssembler playerResourceAssembler;

    /**
     * Retreive the state of a player based on its unique identifier
     * @param playerId Player ID
     * @return State of the player
     */
    @GetMapping("/{playerId}")
    public PlayerResource getPlayer(@PathVariable Long playerId) {
        Player player = playerService.getPlayer(playerId);
        if (player == null) {
            throw new DeckNotFoundException();
        }
        return playerResourceAssembler.convertToPlayerResource(player);
    }

    /**
     * Create a new player
     * @param playerCreate Initial state of the player
     * @return State of the newly created player
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public PlayerResource createDeck(@RequestBody PlayerCreateResource playerCreate) {
        Long playerId = playerService.createPlayer(playerCreate.getName());
        Player player = playerService.getPlayer(playerId);

        return playerResourceAssembler.convertToPlayerResource(player);
    }

}

