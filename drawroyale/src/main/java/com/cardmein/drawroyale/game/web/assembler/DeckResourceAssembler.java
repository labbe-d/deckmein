package com.cardmein.drawroyale.game.web.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import com.cardmein.drawroyale.game.model.Deck;
import com.cardmein.drawroyale.game.web.controller.DeckController;
import com.cardmein.drawroyale.game.web.model.Card;
import com.cardmein.drawroyale.game.web.model.DeckResource;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class DeckResourceAssembler {
  
    public DeckResource convertToDeckResource(Deck deck) {
        DeckResource deckResource = new DeckResource();

        Link selfLink = linkTo(DeckController.class).slash(deck.getId()).withSelfRel();
        deckResource.add(selfLink);

        deckResource.setObjectId(deck.getId());

        deck.getCards().forEach(c -> {
            Card card = new Card();
            card.setObjectId(c.getId());
            card.setSuit(c.getSuit());
            card.setValue(c.getValue());
            deckResource.addCard(card);
        });

        return deckResource;
    }


}