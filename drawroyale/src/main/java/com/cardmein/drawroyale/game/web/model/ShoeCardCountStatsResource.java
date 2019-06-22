package com.cardmein.drawroyale.game.web.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

/**
 * RESTful representation of a shoe's remaining card statistics
 */
public class ShoeCardCountStatsResource extends ResourceSupport {

    private Long objectId;
    private List<ShoeCardCount> cardCounts = new ArrayList<ShoeCardCount>();

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public void addCardCount(ShoeCardCount suitCount) {
        cardCounts.add(suitCount);
    }

    public List<ShoeCardCount> getCardCounts() {
        return Collections.unmodifiableList(cardCounts);
    }

    public void setCardCounts(List<ShoeCardCount> suitCounts) {
        this.cardCounts.clear();
        this.cardCounts.addAll(suitCounts);
    }

}