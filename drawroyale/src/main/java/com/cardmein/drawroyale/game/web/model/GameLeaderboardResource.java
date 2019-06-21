package com.cardmein.drawroyale.game.web.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

public class GameLeaderboardResource extends ResourceSupport {

    private Long objectId;
    private List<LeaderboardPlayerGameResource> players = new ArrayList<LeaderboardPlayerGameResource>();

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public void addPlayer(LeaderboardPlayerGameResource player) {
        players.add(player);

        players.sort((p1, p2) -> p2.getScore() - p1.getScore());
    }

    public List<LeaderboardPlayerGameResource> getLeaderboard() {
        return Collections.unmodifiableList(players);
    }

}