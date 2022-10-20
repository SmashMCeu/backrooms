package eu.smashmc.backrooms.generator.scanner.strategy;

import eu.smashmc.backrooms.generator.room.Room;
import org.bukkit.Location;

import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface ScannerStrategy {

    List<Room> scan(Location start);
}
