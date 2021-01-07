package com.space.service;

import com.space.model.ShipType;

import java.util.Date;

public class GetShipListParameters {

    final private String name;
    final private String planet;
    final private ShipType shipType;
    final private Date after;
    final private Date before;
    final private Boolean isUsed;
    final private Double minSpeed;
    final private Double maxSpeed;
    final private Integer minCrewSize;
    final private Integer maxCrewSize;
    final private Double minRating;
    final private Double maxRating;

    public GetShipListParameters(
            String name,
            String planet,
            Long after,
            Long before,
            ShipType shipType,
            Boolean isUsed,
            Double minSpeed,
            Double maxSpeed,
            Integer minCrewSize,
            Integer maxCrewSize,
            Double minRating,
            Double maxRating) {
        this.name = name;
        this.planet = planet;
        this.after = after == null ? null: new Date(after);
        this.before = before == null ? null: new Date(before);
        this.shipType = shipType;
        this.isUsed = isUsed;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.minCrewSize = minCrewSize;
        this.maxCrewSize = maxCrewSize;
        this.minRating = minRating;
        this.maxRating = maxRating;
    }

    public String getName() {
        return name;
    }

    public String getPlanet() {
        return planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public Date getAfter() {
        return after;
    }

    public Date getBefore() {
        return before;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public Double getMinSpeed() {
        return minSpeed;
    }

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public Integer getMinCrewSize() {
        return minCrewSize;
    }

    public Integer getMaxCrewSize() {
        return maxCrewSize;
    }

    public Double getMinRating() {
        return minRating;
    }

    public Double getMaxRating() {
        return maxRating;
    }
}