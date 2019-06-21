package com.cardmein.drawroyale.game.web.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.function.ToIntFunction;

import com.cardmein.drawroyale.game.model.GameCard;
import com.cardmein.drawroyale.game.model.PlayerGame;
import com.cardmein.drawroyale.game.web.controller.GameController;
import com.cardmein.drawroyale.game.web.controller.PlayerController;
import com.cardmein.drawroyale.game.web.model.Card;
import com.cardmein.drawroyale.game.web.model.LeaderboardPlayerGameResource;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class LeaderboardPlayerGameResourceAssembler {

    public LeaderboardPlayerGameResource convertToPlayerResource(PlayerGame playerGame, ToIntFunction<GameCard> scoringFn) {
        LeaderboardPlayerGameResource playerGameResource = new LeaderboardPlayerGameResource();

        Link selfLink = linkTo(GameController.class).slash(playerGame.getGame().getId()).slash("players").slash(playerGame.getId()).withSelfRel();
        playerGameResource.add(selfLink);

        Link viewPlayerLink = linkTo(PlayerController.class).slash(playerGame.getPlayer().getId()).withRel("view_player");
        playerGameResource.add(viewPlayerLink);

        Link viewHandLink = linkTo(GameController.class).slash(playerGame.getGame().getId()).slash("players").slash(playerGame.getId()).slash("hand").withRel("view_hand");
        playerGameResource.add(viewHandLink);

        playerGameResource.setObjectId(playerGame.getId());
        playerGameResource.setPlayerId(playerGame.getPlayer().getId());
        playerGameResource.setName(playerGame.getPlayer().getName());

        int score = 0;
        score = playerGame.getHand().stream()
                .mapToInt(scoringFn)
                .sum();

        playerGameResource.setScore(score);

        return playerGameResource;
    }

}
