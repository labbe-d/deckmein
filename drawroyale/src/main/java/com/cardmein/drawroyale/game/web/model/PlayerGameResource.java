package com.cardmein.drawroyale.game.web.model;

import org.springframework.hateoas.ResourceSupport;

public class PlayerGameResource extends ResourceSupport {

    private Long objectId;
    private Long playerId;
    private String name;

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
}
