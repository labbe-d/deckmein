package com.cardmein.drawroyale.game.web.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import com.cardmein.drawroyale.game.model.Game;
import com.cardmein.drawroyale.game.web.controller.GameController;
import com.cardmein.drawroyale.game.web.model.GameResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class GameResourceAssembler {

    @Autowired
    private DeckResourceAssembler deckAssembler;

    public GameResource convertToGameResource(Game game) {
        GameResource gameResource = new GameResource();

        Link selfLink = linkTo(GameController.class).slash(game.getId()).withSelfRel();
        gameResource.add(selfLink);

        Link addDeckLink = linkTo(GameController.class).slash(game.getId()).slash("decks").withRel("add_deck");
        gameResource.add(addDeckLink);

        Link addPlayerLink = linkTo(GameController.class).slash(game.getId()).slash("players").withRel("add_player");
        gameResource.add(addPlayerLink);

        gameResource.setObjectId(game.getId());
        gameResource.setName(game.getName());

        game.getDecks().forEach(d -> {
            gameResource.addDeck(deckAssembler.convertToDeckResource(d));
        });

        return gameResource;
    }

}