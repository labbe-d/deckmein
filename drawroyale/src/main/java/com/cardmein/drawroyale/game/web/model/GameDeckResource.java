package com.cardmein.drawroyale.game.web.model;

import org.springframework.hateoas.ResourceSupport;

/**
 * RESTful representation of a game deck
 */
public class GameDeckResource extends ResourceSupport {

    private Long objectId;

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

}