package com.cardmein.drawroyale.game.model;

/**
 * Person registerd in the system and can participate in games.
 */
public class Player {

    private Long id;
    private String name;

    public Player(String name) {
        this.name = name;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}