package com.cardmein.drawroyale.game.web.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import com.cardmein.drawroyale.game.model.Deck;
import com.cardmein.drawroyale.game.web.controller.DeckController;
import com.cardmein.drawroyale.game.web.model.Card;
import com.cardmein.drawroyale.game.web.model.GameDeckResource;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class GameDeckResourceAssembler {
  
    public GameDeckResource convertToDeckResource(Deck deck) {
        GameDeckResource deckResource = new GameDeckResource();

        Link selfLink = linkTo(DeckController.class).slash(deck.getId()).withSelfRel();
        deckResource.add(selfLink);

        deckResource.setObjectId(deck.getId());

        return deckResource;
    }


}