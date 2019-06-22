package com.cardmein.drawroyale.game.web.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import com.cardmein.drawroyale.game.model.Deck;
import com.cardmein.drawroyale.game.web.controller.DeckController;
import com.cardmein.drawroyale.game.web.model.GameDeckResource;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

/**
 * Assembler for game deck resources
 */
@Component
public class GameDeckResourceAssembler {
  
    /**
     * Convert a deck into a game deck resource
     * @param deck Deck to convert
     * @return Converted resource
     */
    public GameDeckResource convertToDeckResource(Deck deck) {
        GameDeckResource deckResource = new GameDeckResource();

        Link selfLink = linkTo(DeckController.class).slash(deck.getId()).withSelfRel();
        deckResource.add(selfLink);

        deckResource.setObjectId(deck.getId());

        return deckResource;
    }


}