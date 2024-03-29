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

/**
 * Converts a list of player game participations into a sorted list based on total hand score
 */
@Component
public class GameLeaderboardResourceAssembler {

    @Autowired
    private LeaderboardPlayerGameResourceAssembler playerAssembler;

    /**
     * Converts a list a player game participations into a sorted list using a given scoring function. Players are sorted by score descending.
     * @param game Game where players participate
     * @param playerGames Player game participations
     * @param scoringFn Function converting a game card into a numerical value
     * @return Sorted list of players by their score
     */
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