package com.cardmein.drawroyale.game.web.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import com.cardmein.drawroyale.game.model.PlayerGame;
import com.cardmein.drawroyale.game.web.controller.GameController;
import com.cardmein.drawroyale.game.web.controller.PlayerController;
import com.cardmein.drawroyale.game.web.model.Card;
import com.cardmein.drawroyale.game.web.model.PlayerGameResource;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class PlayerGameResourceAssembler {

    public PlayerGameResource convertToPlayerResource(PlayerGame playerGame) {
        PlayerGameResource playerGameResource = new PlayerGameResource();

        Link selfLink = linkTo(GameController.class).slash(playerGame.getGame().getId()).slash("players").slash(playerGame.getId()).withSelfRel();
        playerGameResource.add(selfLink);

        Link viewPlayerLink = linkTo(PlayerController.class).slash(playerGame.getPlayer().getId()).withRel("view_player");
        playerGameResource.add(viewPlayerLink);

        Link removePlayerGameLink = linkTo(GameController.class).slash(playerGame.getGame().getId()).slash("players").slash(playerGame.getId()).withRel("remove_player_game");
        playerGameResource.add(removePlayerGameLink);

        Link viewHandLink = linkTo(GameController.class).slash(playerGame.getGame().getId()).slash("players").slash(playerGame.getId()).slash("hand").withRel("view_hand");
        playerGameResource.add(viewHandLink);

        Link drawCardLink = linkTo(GameController.class).slash(playerGame.getGame().getId()).slash("players").slash(playerGame.getId()).slash("state").withRel("draw_card");
        playerGameResource.add(drawCardLink);

        playerGameResource.setObjectId(playerGame.getId());
        playerGameResource.setPlayerId(playerGame.getPlayer().getId());
        playerGameResource.setName(playerGame.getPlayer().getName());
        playerGameResource.setState(playerGame.getState().name());

        playerGame.getHand().forEach(c -> {
            Card card = new Card();
            card.setObjectId(c.getId());
            card.setSuit(c.getCard().getSuit());
            card.setValue(c.getCard().getValue());
            playerGameResource.addCard(card);
        });

        return playerGameResource;
    }

}