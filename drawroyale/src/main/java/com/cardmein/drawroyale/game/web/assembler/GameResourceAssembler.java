package com.cardmein.drawroyale.game.web.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.List;

import com.cardmein.drawroyale.game.model.Game;
import com.cardmein.drawroyale.game.model.PlayerGame;
import com.cardmein.drawroyale.game.web.controller.GameController;
import com.cardmein.drawroyale.game.web.model.GameResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class GameResourceAssembler {

    @Autowired
    private GameDeckResourceAssembler gameDeckAssembler;

    @Autowired
    private PlayerGameResourceAssembler playerGameAssembler;

    @Autowired
    private ShoeResourceAssembler shoeAssembler;

    public GameResource convertToGameResource(Game game, List<PlayerGame> playerGames) {
        GameResource gameResource = new GameResource();

        Link selfLink = linkTo(GameController.class).slash(game.getId()).withSelfRel();
        gameResource.add(selfLink);

        Link addDeckLink = linkTo(GameController.class).slash(game.getId()).slash("decks").withRel("add_deck");
        gameResource.add(addDeckLink);

        Link addPlayerLink = linkTo(GameController.class).slash(game.getId()).slash("players").withRel("add_player");
        gameResource.add(addPlayerLink);

        Link viewLeaderboardLink = linkTo(GameController.class).slash(game.getId()).slash("leaderboard").withRel("view_leaderboard");
        gameResource.add(viewLeaderboardLink);

        gameResource.setObjectId(game.getId());
        gameResource.setName(game.getName());

        game.getDecks().forEach(d -> {
            gameResource.addDeck(gameDeckAssembler.convertToDeckResource(d));
        });

        playerGames.forEach(p -> {
            gameResource.addPlayer(playerGameAssembler.convertToPlayerResource(p));
        });

        gameResource.setShoe(shoeAssembler.convertToShoeResource(game, game.getShoe()));

        return gameResource;
    }

}