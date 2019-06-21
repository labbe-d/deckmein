package com.cardmein.drawroyale.game.web.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import com.cardmein.drawroyale.game.model.Deck;
import com.cardmein.drawroyale.game.service.DeckService;
import com.cardmein.drawroyale.game.service.GameService;
import com.cardmein.drawroyale.game.web.controller.exception.DeckNotFoundException;
import com.cardmein.drawroyale.game.web.model.Card;
import com.cardmein.drawroyale.game.web.model.DeckCreateResource;
import com.cardmein.drawroyale.game.web.model.DeckResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "decks", produces = MediaType.APPLICATION_JSON_VALUE)
public class DeckController {

    @Autowired
    private GameService gameService;

    @Autowired
    private DeckService deckService;

    @GetMapping("/{deckId}")
    public DeckResource getDeck(@PathVariable Long deckId) {
        Deck deck = deckService.getDeck(deckId);
        if (deck == null) {
            throw new DeckNotFoundException();
        }
        return convertToDeckResource(deck);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public DeckResource createDeck(@RequestBody DeckCreateResource deckCreate) {
        Long deckId = deckService.createDeck(deckCreate.getDeckType());
        Deck deck = deckService.getDeck(deckId);

        return convertToDeckResource(deck);
    }

    private DeckResource convertToDeckResource(Deck deck) {
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

