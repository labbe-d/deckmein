package com.cardmein.drawroyale.game.web.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.List;
import java.util.function.ToIntFunction;

import com.cardmein.drawroyale.game.model.Game;
import com.cardmein.drawroyale.game.model.GameCard;
import com.cardmein.drawroyale.game.model.PlayerGame;
import com.cardmein.drawroyale.game.web.controller.GameController;
import com.cardmein.drawroyale.game.web.model.GameLeaderboardResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class GameLeaderboardResourceAssembler {

    @Autowired
    private LeaderboardPlayerGameResourceAssembler playerAssembler;

    public GameLeaderboardResource convertToGameResource(Game game, List<PlayerGame> playerGames, ToIntFunction<GameCard> scoringFn) {
        GameLeaderboardResource leaderboardResource = new GameLeaderboardResource();

        Link selfLink = linkTo(GameController.class).slash(game.getId()).slash("leaderboard").withSelfRel();
        leaderboardResource.add(selfLink);

        leaderboardResource.setObjectId(game.getId());

        playerGames.forEach(p -> {
            leaderboardResource.addPlayer(playerAssembler.convertToPlayerResource(p, scoringFn));
        });

        return leaderboardResource;
    }

}