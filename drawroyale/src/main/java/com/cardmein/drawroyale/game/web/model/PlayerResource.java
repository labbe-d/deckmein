package com.cardmein.drawroyale.game.web.model;

import org.springframework.hateoas.ResourceSupport;

public class PlayerResource extends ResourceSupport {

    private Long objectId;
    private String name;

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
