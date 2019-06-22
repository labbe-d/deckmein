package com.cardmein.drawroyale.game.web.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import com.cardmein.drawroyale.game.model.Game;
import com.cardmein.drawroyale.game.model.Shoe;
import com.cardmein.drawroyale.game.web.controller.GameController;
import com.cardmein.drawroyale.game.web.model.Card;
import com.cardmein.drawroyale.game.web.model.ShoeResource;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

/**
 * Converts a game shoe into a resource
 */
@Component
public class ShoeResourceAssembler {

    /**
     * Converts a game shoe into a resource
     * @param game Game
     * @param shoe Shoe
     * @return Converted shoe
     */
    public ShoeResource convertToShoeResource(Game game, Shoe shoe) {
        ShoeResource shoeResource = new ShoeResource();

        Link selfLink = linkTo(GameController.class).slash(game.getId()).slash("shoe").withSelfRel();
        shoeResource.add(selfLink);

        Link shuffleLink = linkTo(GameController.class).slash(game.getId()).slash("shoe").slash("state").withRel("shuffle");
        shoeResource.add(shuffleLink);

        Link suitStatsLink = linkTo(GameController.class).slash(game.getId()).slash("shoe").slash("stats").slash("suits").withRel("view_suit_statistics");
        shoeResource.add(suitStatsLink);

        Link cardStatsLink = linkTo(GameController.class).slash(game.getId()).slash("shoe").slash("stats").slash("cards").withRel("view_card_statistics");
        shoeResource.add(cardStatsLink);

        shoeResource.setState(shoe.getState().name());

        shoe.getCards().forEach(c -> {
            Card card = new Card();
            card.setObjectId(c.getId());
            card.setSuit(c.getCard().getSuit());
            card.setValue(c.getCard().getValue());
            shoeResource.addCard(card);
        });

        return shoeResource;
    }

}