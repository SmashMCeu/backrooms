package gg.maga.backrooms.room;

import lombok.Data;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
public class Room {

    private String id;
    private Set<RoomOpening> openings;

    //Replica
    private Location pivot;
    private Location min;
    private Location max;

    public Room(String id, Location min, Location max, RoomOpening... openings) {
        this.id = id;
        this.pivot = min;
        this.min = min;
        this.max = max;
        this.openings = new HashSet<>(Arrays.asList(openings));
    }
}
