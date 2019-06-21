package com.cardmein.drawroyale.game.web.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import com.cardmein.drawroyale.game.model.PlayerGame;
import com.cardmein.drawroyale.game.web.controller.GameController;
import com.cardmein.drawroyale.game.web.controller.PlayerController;
import com.cardmein.drawroyale.game.web.model.PlayerGameResource;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class PlayerGameResourceAssembler {

    public PlayerGameResource convertToPlayerResource(PlayerGame playerGame) {
        PlayerGameResource gameResource = new PlayerGameResource();

        Link selfLink = linkTo(GameController.class).slash(playerGame.getGame().getId()).slash("players").slash(playerGame.getId()).withSelfRel();
        gameResource.add(selfLink);

        Link viewPlayerLink = linkTo(PlayerController.class).slash(playerGame.getPlayer().getId()).withRel("view_player");
        gameResource.add(viewPlayerLink);

        Link removePlayerGameLink = linkTo(GameController.class).slash(playerGame.getGame().getId()).slash("players").slash(playerGame.getId()).withRel("remove_player_game");
        gameResource.add(removePlayerGameLink);

        gameResource.setObjectId(playerGame.getId());
        gameResource.setPlayerId(playerGame.getPlayer().getId());
        gameResource.setName(playerGame.getPlayer().getName());

        return gameResource;
    }

}