package com.cardmein.drawroyale.game.web.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import com.cardmein.drawroyale.game.model.Game;
import com.cardmein.drawroyale.game.model.Shoe;
import com.cardmein.drawroyale.game.web.controller.GameController;
import com.cardmein.drawroyale.game.web.model.Card;
import com.cardmein.drawroyale.game.web.model.ShoeResource;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class ShoeResourceAssembler {

    public ShoeResource convertToShoeResource(Game game, Shoe shoe) {
        ShoeResource shoeResource = new ShoeResource();

        Link selfLink = linkTo(GameController.class).slash(game.getId()).slash("shoe").withSelfRel();
        shoeResource.add(selfLink);

        Link shuffleLink = linkTo(GameController.class).slash(game.getId()).slash("shoe").slash("state").withRel("shuffle");
        shoeResource.add(shuffleLink);

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