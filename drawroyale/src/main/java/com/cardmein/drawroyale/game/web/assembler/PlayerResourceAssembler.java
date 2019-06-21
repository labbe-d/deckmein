package com.cardmein.drawroyale.game.web.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import com.cardmein.drawroyale.game.model.Player;
import com.cardmein.drawroyale.game.web.controller.PlayerController;
import com.cardmein.drawroyale.game.web.model.PlayerResource;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class PlayerResourceAssembler {

    public PlayerResource convertToPlayerResource(Player player) {
        PlayerResource gameResource = new PlayerResource();

        Link selfLink = linkTo(PlayerController.class).slash(player.getId()).withSelfRel();
        gameResource.add(selfLink);

        gameResource.setObjectId(player.getId());
        gameResource.setName(player.getName());

        return gameResource;
    }

}