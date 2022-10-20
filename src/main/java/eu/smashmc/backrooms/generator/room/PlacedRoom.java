package eu.smashmc.backrooms.generator.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
@AllArgsConstructor
public class PlacedRoom {

    private final Room room;
    private Location center;

}
