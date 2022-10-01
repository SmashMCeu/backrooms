package gg.maga.backrooms.room;

import lombok.Data;
import org.bukkit.Location;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
public class PlacedRoom {

    private final Room room;
    private Location center;

    public PlacedRoom(Room room, Location center) {
        this.room = room;
        this.center = center;
    }
}
