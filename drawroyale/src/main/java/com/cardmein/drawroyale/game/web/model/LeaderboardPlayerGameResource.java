package com.cardmein.drawroyale.game.web.model;

import org.springframework.hateoas.ResourceSupport;

public class LeaderboardPlayerGameResource extends ResourceSupport {

    private Long objectId;
    private Long playerId;
    private String name;
    private int score;

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
