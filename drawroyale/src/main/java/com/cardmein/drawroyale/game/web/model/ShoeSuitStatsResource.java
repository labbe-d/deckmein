package com.cardmein.drawroyale.game.web.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

/**
 * RESTful representation of a shoe's remaining suit statistics
 */
public class ShoeSuitStatsResource extends ResourceSupport {

    private Long objectId;
    private List<ShoeSuitCount> suitCounts = new ArrayList<ShoeSuitCount>();

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public void addSuitCount(ShoeSuitCount suitCount) {
        suitCounts.add(suitCount);
    }

    public List<ShoeSuitCount> getSuitCounts() {
        return Collections.unmodifiableList(suitCounts);
    }

    public void setSuitCounts(List<ShoeSuitCount> suitCounts) {
        this.suitCounts.clear();
        this.suitCounts.addAll(suitCounts);
    }

}