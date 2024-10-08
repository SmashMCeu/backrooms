package eu.smashmc.backrooms.generator.room;

import lombok.Data;
import org.bukkit.Location;

import java.util.Set;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
public class Room {


    private String name;
    private double chance;
    private int amount;
    private Set<RoomOpening> openings;
    private Location pivot;
    private Location min;
    private Location max;

    public Room(Location min, Location max, Set<RoomOpening> openings) {
        this.pivot = min;
        this.min = min;
        this.max = max;
        this.openings = openings;
    }

    public boolean hasName() {
        return name != null;
    }
}
